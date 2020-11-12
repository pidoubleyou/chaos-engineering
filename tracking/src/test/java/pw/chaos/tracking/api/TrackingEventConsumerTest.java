package pw.chaos.tracking.api;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import pw.chaos.tracking.domain.TrackingEvent;
import pw.chaos.tracking.persistence.Tracking;
import pw.chaos.tracking.persistence.TrackingRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class TrackingEventConsumerTest {
  @Autowired private InputDestination input;

  @MockBean private TrackingRepository trackingRepository;

  @Test
  void start() {
    Long eventId = 29L;
    Long registrationId = 893L;
    LocalDateTime timestamp = LocalDateTime.of(2020, 11, 11, 11, 11, 11);

    TrackingEvent event = new TrackingEvent();
    event.setEventId(eventId);
    event.setRegistrationId(registrationId);
    event.setTimestamp(timestamp);

    Tracking tracking = new Tracking();
    tracking.setEventId(eventId);
    tracking.setRegistrationId(registrationId);

    Mockito.when(trackingRepository.findByEventIdAndRegistrationId(eventId, registrationId))
        .thenReturn(Mono.just(tracking));
    Mockito.when(trackingRepository.save(any())).thenAnswer(i -> Mono.just(i.getArgument(0)));

    Message<TrackingEvent> message =
        MessageBuilder.withPayload(event)
            .setHeader(
                TrackingEventConsumer.HEADER_CHECKPOINT, TrackingEventConsumer.CHECKPOINT_START)
            .build();
    input.send(message);

    ArgumentCaptor<Tracking> trackingCaptor = ArgumentCaptor.forClass(Tracking.class);
    Mockito.verify(trackingRepository).save(trackingCaptor.capture());
    Tracking actualTracking = trackingCaptor.getValue();
    assertEquals(timestamp, actualTracking.getStart());
    assertNull(actualTracking.getFinish());
    assertEquals(eventId, actualTracking.getEventId());
    assertEquals(registrationId, actualTracking.getRegistrationId());
  }

  @Test
  void end() {
    Long eventId = 29L;
    Long registrationId = 893L;
    LocalDateTime start = LocalDateTime.of(2020, 11, 11, 10, 54, 0);
    LocalDateTime timestamp = LocalDateTime.of(2020, 11, 11, 11, 11, 11);

    TrackingEvent event = new TrackingEvent();
    event.setEventId(eventId);
    event.setRegistrationId(registrationId);
    event.setTimestamp(timestamp);

    Tracking tracking = new Tracking();
    tracking.setEventId(eventId);
    tracking.setRegistrationId(registrationId);
    tracking.setStart(start);

    Mockito.when(trackingRepository.findByEventIdAndRegistrationId(eventId, registrationId))
        .thenReturn(Mono.just(tracking));
    Mockito.when(trackingRepository.save(any())).thenAnswer(i -> Mono.just(i.getArgument(0)));

    Message<TrackingEvent> message =
        MessageBuilder.withPayload(event)
            .setHeader(
                TrackingEventConsumer.HEADER_CHECKPOINT, TrackingEventConsumer.CHECKPOINT_FINISH)
            .build();
    input.send(message);

    ArgumentCaptor<Tracking> trackingCaptor = ArgumentCaptor.forClass(Tracking.class);
    Mockito.verify(trackingRepository).save(trackingCaptor.capture());
    Tracking actualTracking = trackingCaptor.getValue();
    assertEquals(eventId, actualTracking.getEventId());
    assertEquals(registrationId, actualTracking.getRegistrationId());
    assertEquals(start, actualTracking.getStart());
    assertEquals(timestamp, actualTracking.getFinish());
  }
}
