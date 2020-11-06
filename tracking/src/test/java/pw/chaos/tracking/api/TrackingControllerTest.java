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
import pw.chaos.tracking.persistence.TrackingRepository;
import pw.chaos.tracking.persistence.Tracking;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureWebTestClient
class TrackingControllerTest {
  @Autowired private WebTestClient webTestClient;

  @MockBean private TrackingRepository trackingRepository;

  @Test
  @DisplayName("start tracking and persist registrations")
  void start() {
    TrackingModel trackingModel = new TrackingModel();
    trackingModel.setEventId(1L);
    trackingModel.setRegistrations(new Long[] {5L, 17L});

    long id = 1L;

    Mockito.when(trackingRepository.save(any())).thenAnswer(i -> {
      Tracking argument = i.getArgument(0);
      return Mono.just(argument);
    });

    webTestClient
        .post()
        .uri("/start")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(trackingModel)
        .exchange()
        .expectStatus()
        .isOk();
  }
}
