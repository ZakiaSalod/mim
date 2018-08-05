package org.jhipster.mim.web.rest;

import org.jhipster.mim.MimApp;

import org.jhipster.mim.domain.Event;
import org.jhipster.mim.repository.EventRepository;
import org.jhipster.mim.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


import static org.jhipster.mim.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.jhipster.mim.domain.enumeration.Province;
/**
 * Test class for the EventResource REST controller.
 *
 * @see EventResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MimApp.class)
public class EventResourceIntTest {

    private static final String DEFAULT_POSTED_BY = "AAAAAAAAAA";
    private static final String UPDATED_POSTED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BRIEF_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_BRIEF_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_EVENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Province DEFAULT_PROVINCE = Province.EASTERN_CAPE;
    private static final Province UPDATED_PROVINCE = Province.FREE_STATE;

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_WEBSITE = "BBBBBBBBBB";

    @Autowired
    private EventRepository eventRepository;
    @Mock
    private EventRepository eventRepositoryMock;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEventMockMvc;

    private Event event;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventResource eventResource = new EventResource(eventRepository);
        this.restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .postedBy(DEFAULT_POSTED_BY)
            .eventName(DEFAULT_EVENT_NAME)
            .briefDescription(DEFAULT_BRIEF_DESCRIPTION)
            .eventDate(DEFAULT_EVENT_DATE)
            .province(DEFAULT_PROVINCE)
            .region(DEFAULT_REGION)
            .address(DEFAULT_ADDRESS)
            .eventWebsite(DEFAULT_EVENT_WEBSITE);
        return event;
    }

    @Before
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getPostedBy()).isEqualTo(DEFAULT_POSTED_BY);
        assertThat(testEvent.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testEvent.getBriefDescription()).isEqualTo(DEFAULT_BRIEF_DESCRIPTION);
        assertThat(testEvent.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(testEvent.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testEvent.getRegion()).isEqualTo(DEFAULT_REGION);
        assertThat(testEvent.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEvent.getEventWebsite()).isEqualTo(DEFAULT_EVENT_WEBSITE);
    }

    @Test
    @Transactional
    public void createEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event with an existing ID
        event.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPostedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setPostedBy(null);

        // Create the Event, which fails.

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEventNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEventName(null);

        // Create the Event, which fails.

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEventDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEventDate(null);

        // Create the Event, which fails.

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setRegion(null);

        // Create the Event, which fails.

        restEventMockMvc.perform(post("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc.perform(get("/api/events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].postedBy").value(hasItem(DEFAULT_POSTED_BY.toString())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME.toString())))
            .andExpect(jsonPath("$.[*].briefDescription").value(hasItem(DEFAULT_BRIEF_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE.toString())))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].eventWebsite").value(hasItem(DEFAULT_EVENT_WEBSITE.toString())));
    }
    
    public void getAllEventsWithEagerRelationshipsIsEnabled() throws Exception {
        EventResource eventResource = new EventResource(eventRepositoryMock);
        when(eventRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEventMockMvc.perform(get("/api/events?eagerload=true"))
        .andExpect(status().isOk());

        verify(eventRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllEventsWithEagerRelationshipsIsNotEnabled() throws Exception {
        EventResource eventResource = new EventResource(eventRepositoryMock);
            when(eventRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restEventMockMvc = MockMvcBuilders.standaloneSetup(eventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEventMockMvc.perform(get("/api/events?eagerload=true"))
        .andExpect(status().isOk());

            verify(eventRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.postedBy").value(DEFAULT_POSTED_BY.toString()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME.toString()))
            .andExpect(jsonPath("$.briefDescription").value(DEFAULT_BRIEF_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE.toString()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.eventWebsite").value(DEFAULT_EVENT_WEBSITE.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).get();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .postedBy(UPDATED_POSTED_BY)
            .eventName(UPDATED_EVENT_NAME)
            .briefDescription(UPDATED_BRIEF_DESCRIPTION)
            .eventDate(UPDATED_EVENT_DATE)
            .province(UPDATED_PROVINCE)
            .region(UPDATED_REGION)
            .address(UPDATED_ADDRESS)
            .eventWebsite(UPDATED_EVENT_WEBSITE);

        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvent)))
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getPostedBy()).isEqualTo(UPDATED_POSTED_BY);
        assertThat(testEvent.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testEvent.getBriefDescription()).isEqualTo(UPDATED_BRIEF_DESCRIPTION);
        assertThat(testEvent.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testEvent.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testEvent.getRegion()).isEqualTo(UPDATED_REGION);
        assertThat(testEvent.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEvent.getEventWebsite()).isEqualTo(UPDATED_EVENT_WEBSITE);
    }

    @Test
    @Transactional
    public void updateNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Create the Event

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEventMockMvc.perform(put("/api/events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Get the event
        restEventMockMvc.perform(delete("/api/events/{id}", event.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = new Event();
        event1.setId(1L);
        Event event2 = new Event();
        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);
        event2.setId(2L);
        assertThat(event1).isNotEqualTo(event2);
        event1.setId(null);
        assertThat(event1).isNotEqualTo(event2);
    }
}
