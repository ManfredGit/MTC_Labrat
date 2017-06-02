package com.microsoft.labrat.web.rest;

import com.microsoft.labrat.LabratApp;

import com.microsoft.labrat.domain.Intervention;
import com.microsoft.labrat.repository.InterventionRepository;
import com.microsoft.labrat.service.dto.InterventionDTO;
import com.microsoft.labrat.service.mapper.InterventionMapper;
import com.microsoft.labrat.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.microsoft.labrat.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InterventionResource REST controller.
 *
 * @see InterventionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LabratApp.class)
public class InterventionResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private InterventionMapper interventionMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restInterventionMockMvc;

    private Intervention intervention;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InterventionResource interventionResource = new InterventionResource(interventionRepository, interventionMapper);
        this.restInterventionMockMvc = MockMvcBuilders.standaloneSetup(interventionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervention createEntity(EntityManager em) {
        Intervention intervention = new Intervention()
            .description(DEFAULT_DESCRIPTION)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME);
        return intervention;
    }

    @Before
    public void initTest() {
        intervention = createEntity(em);
    }

    @Test
    @Transactional
    public void createIntervention() throws Exception {
        int databaseSizeBeforeCreate = interventionRepository.findAll().size();

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);
        restInterventionMockMvc.perform(post("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isCreated());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeCreate + 1);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIntervention.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testIntervention.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    public void createInterventionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interventionRepository.findAll().size();

        // Create the Intervention with an existing ID
        intervention.setId(1L);
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterventionMockMvc.perform(post("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = interventionRepository.findAll().size();
        // set the field null
        intervention.setStartTime(null);

        // Create the Intervention, which fails.
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        restInterventionMockMvc.perform(post("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isBadRequest());

        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInterventions() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList
        restInterventionMockMvc.perform(get("/api/interventions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intervention.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))));
    }

    @Test
    @Transactional
    public void getIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get the intervention
        restInterventionMockMvc.perform(get("/api/interventions/{id}", intervention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(intervention.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)));
    }

    @Test
    @Transactional
    public void getNonExistingIntervention() throws Exception {
        // Get the intervention
        restInterventionMockMvc.perform(get("/api/interventions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Update the intervention
        Intervention updatedIntervention = interventionRepository.findOne(intervention.getId());
        updatedIntervention
            .description(UPDATED_DESCRIPTION)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);
        InterventionDTO interventionDTO = interventionMapper.toDto(updatedIntervention);

        restInterventionMockMvc.perform(put("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIntervention.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testIntervention.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restInterventionMockMvc.perform(put("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isCreated());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);
        int databaseSizeBeforeDelete = interventionRepository.findAll().size();

        // Get the intervention
        restInterventionMockMvc.perform(delete("/api/interventions/{id}", intervention.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intervention.class);
        Intervention intervention1 = new Intervention();
        intervention1.setId(1L);
        Intervention intervention2 = new Intervention();
        intervention2.setId(intervention1.getId());
        assertThat(intervention1).isEqualTo(intervention2);
        intervention2.setId(2L);
        assertThat(intervention1).isNotEqualTo(intervention2);
        intervention1.setId(null);
        assertThat(intervention1).isNotEqualTo(intervention2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterventionDTO.class);
        InterventionDTO interventionDTO1 = new InterventionDTO();
        interventionDTO1.setId(1L);
        InterventionDTO interventionDTO2 = new InterventionDTO();
        assertThat(interventionDTO1).isNotEqualTo(interventionDTO2);
        interventionDTO2.setId(interventionDTO1.getId());
        assertThat(interventionDTO1).isEqualTo(interventionDTO2);
        interventionDTO2.setId(2L);
        assertThat(interventionDTO1).isNotEqualTo(interventionDTO2);
        interventionDTO1.setId(null);
        assertThat(interventionDTO1).isNotEqualTo(interventionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(interventionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(interventionMapper.fromId(null)).isNull();
    }
}
