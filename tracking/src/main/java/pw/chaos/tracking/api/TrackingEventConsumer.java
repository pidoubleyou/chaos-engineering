package pw.chaos.tracking.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pw.chaos.tracking.domain.TrackingEvent;
import pw.chaos.tracking.domain.TrackingService;

import java.util.function.Consumer;

@Component
@Slf4j
public class TrackingEventConsumer {
  public static final String HEADER_CHECKPOINT = "CheckPoint";
  public static final String CHECKPOINT_START = "start";
  public static final String CHECKPOINT_FINISH = "finish";

  @Autowired private TrackingService trackingService;

  @Bean
  public Consumer<TrackingEvent> start() {
    return event ->
        trackingService
            .runnerStarted(event)
            .doOnError(x -> log.error("Consumer error - start tracking: ", x))
            .log()
            .subscribe();
  }

  @Bean
  public Consumer<TrackingEvent> finish() {
    return event ->
        trackingService
            .runnerFinished(event)
            .doOnError(x -> log.error("Consumer error - end tracking: ", x))
            .log()
            .subscribe();
  }
}
