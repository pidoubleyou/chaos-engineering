package pw.chaos.tracking.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import pw.chaos.tracking.persistence.Event;
import pw.chaos.tracking.persistence.EventRepository;
import pw.chaos.tracking.persistence.Tracking;
import pw.chaos.tracking.persistence.TrackingRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureWebTestClient
class TrackingControllerTest {
  @Autowired private WebTestClient webTestClient;

  @MockBean private EventRepository eventRepository;
  @MockBean private TrackingRepository trackingRepository;

  @Test
  @DisplayName("start tracking and persist registrations")
  void start() {
    TrackingModel trackingModel = new TrackingModel();
    trackingModel.setEventId(1L);
    trackingModel.setRegistrations(new Long[] {5L, 17L});

    Mockito.when(trackingRepository.save(any()))
        .thenAnswer(
            i -> {
              Tracking argument = i.getArgument(0);
              return Mono.just(argument);
            });
    Mockito.when(eventRepository.save(any()))
        .thenAnswer(
            i -> {
              Event argument = i.getArgument(0);
              return Mono.just(argument);
            });

    webTestClient
        .post()
        .uri("/start")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(trackingModel)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo(1)
        .jsonPath("$.start")
        .isNotEmpty();
  }

  @Test
  @DisplayName("end tracking")
  void end() {
    long id = 1L;
    LocalDateTime start = LocalDateTime.now();

    Event event = new Event();
    event.setId(id);
    event.setStart(start);

    Mockito.when(eventRepository.findById(id)).thenReturn(Mono.just(event));
    Mockito.when(eventRepository.save(any()))
        .thenAnswer(
            i -> {
              Event argument = i.getArgument(0);
              return Mono.just(argument);
            });

    webTestClient
        .post()
        .uri("/end/" + id)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.id")
        .isEqualTo(id)
        .jsonPath("$.start")
        .isEqualTo(start.toString())
        .jsonPath("$.end")
        .isNotEmpty();
  }
}
