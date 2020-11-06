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
import pw.chaos.events.domain.EventService;
import pw.chaos.events.persistence.Event;
import pw.chaos.events.persistence.Registration;
import pw.chaos.events.persistence.RegistrationRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class RegistrationControllerTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper mapper;

  @MockBean private EventService eventService;
  @MockBean private RegistrationRepository registrationRepository;

  @Test
  @DisplayName("post creates new registration")
  void register() throws Exception {
    Event event = new Event();
    event.setId(1L);
    event.setRegistrations(new ArrayList<>());
    when(eventService.findEvent(1L)).thenReturn(Optional.of(event));
    when(registrationRepository.save(any()))
        .thenAnswer(
            i -> {
              Registration registration = i.getArgument(0);
              registration.setId(2L);
              return registration;
            });

    RegistrationModel registrationModel = new RegistrationModel();
    registrationModel.setName("Test Runner");

    mockMvc
        .perform(
            post("/events/1/registrations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registrationModel)))
        .andExpect(status().isCreated())
        .andExpect(header().exists(HttpHeaders.LOCATION))
        .andExpect(header().string(HttpHeaders.LOCATION, endsWith("/events/1/registrations/2")));
  }

  @Test
  @DisplayName("get with unknown registration id returns 404")
  void findByIdWithUnknownRegistrationIdNotFound() throws Exception {
    when(registrationRepository.findById(1L)).thenReturn(Optional.empty());
    when(eventService.findEvent(2L)).thenReturn(Optional.of(new Event()));

    mockMvc
            .perform(get("/events/2/registration/1").accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("get with unknown event id returns 404")
  void findByIdWithUnknownEventIdNotFound() throws Exception {
    when(eventService.findEvent(2L)).thenReturn(Optional.empty());

    mockMvc
            .perform(get("/events/2/registration/1").accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("get with known id returns 200 and registration")
  void findByIdOk() throws Exception {
    long eventId = 1;
    long registrationId = 3;
    Event event = new Event();
    event.setId(eventId);
    event.setName("Test");
    Registration registration = new Registration();
    registration.setName("Test Runner");
    registration.setEvent(event);
    registration.setId(registrationId);
    when(eventService.findEvent(eventId)).thenReturn(Optional.of(event));
    when(registrationRepository.findById(registrationId)).thenReturn(Optional.of(registration));

    mockMvc
            .perform(get("/events/1/registrations/3").accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is((int) registrationId)))
            .andExpect(jsonPath("$.name", is(registration.getName())))
            .andExpect(jsonPath("$._links.self.href", endsWith("/events/1/registrations/3")));
  }

}
