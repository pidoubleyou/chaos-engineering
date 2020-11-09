package pw.chaos.tracking.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pw.chaos.tracking.domain.TrackingService;
import pw.chaos.tracking.persistence.Tracking;
import reactor.core.publisher.Mono;

@RestController
public class TrackingController {

  @Autowired TrackingService trackingService;

  @PostMapping(
      value = "/start",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<EventModel> start(@RequestBody TrackingModel trackingModel) {
    Tracking[] trackings = trackingModel.fromModel();
    return trackingService.start(trackings).map(EventModel::new);
  }

  @PostMapping(value = "/end/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<EventModel> end(@PathVariable Long eventId) {
    return trackingService.end(eventId).map(EventModel::new);
  }
}
