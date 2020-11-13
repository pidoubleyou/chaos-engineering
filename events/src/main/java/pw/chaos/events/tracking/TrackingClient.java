package pw.chaos.events.tracking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import pw.chaos.events.persistence.Event;

import java.time.Duration;

@Service
@Slf4j
public class TrackingClient {
  @Value("${chaos.tracking.url}")
  String trackingUrl;

  public void onStarted(Event event) {
    TrackingModel trackingModel = new TrackingModel(event);
    try {
      createWebClient()
          .post()
          .uri("/start")
          .body(BodyInserters.fromValue(trackingModel))
          .retrieve()
          .bodyToMono(String.class)
          .block(Duration.ofSeconds(5L));
    } catch (WebClientException e) {
      log.error("Tracking client did not accept request: {}", e.getMessage());
    }
  }

  public void onEnded(Event event) {
    try {
      createWebClient()
          .post()
          .uri("/end/" + event.getId())
          .retrieve()
          .bodyToMono(String.class)
          .block(Duration.ofSeconds(5L));
    } catch (WebClientException e) {
      log.error("Tracking client did not accept request: {}", e.getMessage());
    }
  }

  private WebClient createWebClient() {
    return WebClient.builder()
        .baseUrl(trackingUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }
}
