package pw.chaos.events.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pw.chaos.events.persistence.Event;
import pw.chaos.events.persistence.EventRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EventRepository mockRepository;

  @Autowired
  private ObjectMapper mapper;

  @Test
  @DisplayName("post creates new event")
  void create() throws Exception {
    when(mockRepository.save(any(Event.class))).thenAnswer(i -> {
      Event argument = i.getArgument(0);
      argument.setId(1L);
      return argument;
    });

    EventModel event =
        new EventModel() {
          {
            setName("sample run");
          }
        };

    mockMvc
        .perform(
            post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(event)))
        .andExpect(status().isCreated())
        .andExpect(header().exists(HttpHeaders.LOCATION))
        .andExpect(header().string(HttpHeaders.LOCATION, endsWith("/events/1")));
  }

  @Test
  @DisplayName("get with unknown id returns 404")
  void findByIdNotFound() throws Exception {
    when(mockRepository.findById(1L)).thenReturn(Optional.empty());

    mockMvc
        .perform(get("/events/1").accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("get with known id returns 200 and event")
  void findByIdOk() throws Exception {
    long id = 1;
    Event event = new Event();
    event.setId(id);
    event.setName("Test");
    when(mockRepository.findById(id)).thenReturn(Optional.of(event));

    mockMvc
        .perform(get("/events/1").accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is((int) id)))
        .andExpect(jsonPath("$.name", is(event.getName())))
        .andExpect(jsonPath("$._links.self.href", endsWith("/events/1")));
  }

  @Test
  @DisplayName("get all events")
  void getAll() throws Exception {
    Event event1 = new Event();
    event1.setId(1L);
    Event event2 = new Event();
    event2.setId(2L);
    Event[] events = new Event[] { event1, event2 };
    EventModel[] eventModels = new EventModel[] { new EventModel(event1), new EventModel(event2) };
    when(mockRepository.findAll()).thenReturn(Arrays.asList(events.clone()));

    mockMvc.perform(get("/events").accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(eventModels)));
  }

  @Test
  @DisplayName("starts event and returns start time")
  void start() throws Exception {
    long id = 1;
    Event event = new Event();
    event.setId(id);
    event.setName("Test");
    when(mockRepository.findById(id)).thenReturn(Optional.of(event));
    when(mockRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    mockMvc
            .perform(post("/events/1/start").accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is((int) id)))
            .andExpect(jsonPath("$.name", is(event.getName())))
            .andExpect(jsonPath("$.startTime", is(not(nullValue()))))
            .andExpect(jsonPath("$._links.self.href", endsWith("/events/1")));
  }

  @Test
  @DisplayName("start event again does nothing")
  void startEventAgain() throws Exception {
    long id = 1;
    LocalDateTime initialStart = LocalDateTime.now();
    Event event = new Event();
    event.setId(id);
    event.setName("Test");
    event.setStart(initialStart);
    when(mockRepository.findById(id)).thenReturn(Optional.of(event));
    when(mockRepository.save(any())).thenAnswer(i -> i.getArgument(0));

    mockMvc
            .perform(post("/events/1/start").accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is((int) id)))
            .andExpect(jsonPath("$.name", is(event.getName())))
            .andExpect(jsonPath("$.startTime", is(initialStart.toString())))
            .andExpect(jsonPath("$._links.self.href", endsWith("/events/1")));
  }
}
