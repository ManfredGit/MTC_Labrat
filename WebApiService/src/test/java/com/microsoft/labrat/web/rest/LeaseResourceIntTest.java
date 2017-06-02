package com.microsoft.labrat.web.rest;

import com.microsoft.labrat.LabratApp;

import com.microsoft.labrat.domain.Lease;
import com.microsoft.labrat.repository.LeaseRepository;
import com.microsoft.labrat.service.dto.LeaseDTO;
import com.microsoft.labrat.service.mapper.LeaseMapper;
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

import com.microsoft.labrat.domain.enumeration.LeaseStatus;
/**
 * Test class for the LeaseResource REST controller.
 *
 * @see LeaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LabratApp.class)
public class LeaseResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final LeaseStatus DEFAULT_STATUS = LeaseStatus.ACTIVE;
    private static final LeaseStatus UPDATED_STATUS = LeaseStatus.COMPLETED;

    @Autowired
    private LeaseRepository leaseRepository;

    @Autowired
    private LeaseMapper leaseMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLeaseMockMvc;

    private Lease lease;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LeaseResource leaseResource = new LeaseResource(leaseRepository, leaseMapper);
        this.restLeaseMockMvc = MockMvcBuilders.standaloneSetup(leaseResource)
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
    public static Lease createEntity(EntityManager em) {
        Lease lease = new Lease()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS);
        return lease;
    }

    @Before
    public void initTest() {
        lease = createEntity(em);
    }

    @Test
    @Transactional
    public void createLease() throws Exception {
        int databaseSizeBeforeCreate = leaseRepository.findAll().size();

        // Create the Lease
        LeaseDTO leaseDTO = leaseMapper.toDto(lease);
        restLeaseMockMvc.perform(post("/api/leases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaseDTO)))
            .andExpect(status().isCreated());

        // Validate the Lease in the database
        List<Lease> leaseList = leaseRepository.findAll();
        assertThat(leaseList).hasSize(databaseSizeBeforeCreate + 1);
        Lease testLease = leaseList.get(leaseList.size() - 1);
        assertThat(testLease.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testLease.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testLease.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createLeaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leaseRepository.findAll().size();

        // Create the Lease with an existing ID
        lease.setId(1L);
        LeaseDTO leaseDTO = leaseMapper.toDto(lease);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeaseMockMvc.perform(post("/api/leases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Lease> leaseList = leaseRepository.findAll();
        assertThat(leaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = leaseRepository.findAll().size();
        // set the field null
        lease.setStartDate(null);

        // Create the Lease, which fails.
        LeaseDTO leaseDTO = leaseMapper.toDto(lease);

        restLeaseMockMvc.perform(post("/api/leases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaseDTO)))
            .andExpect(status().isBadRequest());

        List<Lease> leaseList = leaseRepository.findAll();
        assertThat(leaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLeases() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get all the leaseList
        restLeaseMockMvc.perform(get("/api/leases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lease.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getLease() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);

        // Get the lease
        restLeaseMockMvc.perform(get("/api/leases/{id}", lease.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lease.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLease() throws Exception {
        // Get the lease
        restLeaseMockMvc.perform(get("/api/leases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLease() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);
        int databaseSizeBeforeUpdate = leaseRepository.findAll().size();

        // Update the lease
        Lease updatedLease = leaseRepository.findOne(lease.getId());
        updatedLease
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS);
        LeaseDTO leaseDTO = leaseMapper.toDto(updatedLease);

        restLeaseMockMvc.perform(put("/api/leases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaseDTO)))
            .andExpect(status().isOk());

        // Validate the Lease in the database
        List<Lease> leaseList = leaseRepository.findAll();
        assertThat(leaseList).hasSize(databaseSizeBeforeUpdate);
        Lease testLease = leaseList.get(leaseList.size() - 1);
        assertThat(testLease.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testLease.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testLease.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingLease() throws Exception {
        int databaseSizeBeforeUpdate = leaseRepository.findAll().size();

        // Create the Lease
        LeaseDTO leaseDTO = leaseMapper.toDto(lease);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLeaseMockMvc.perform(put("/api/leases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leaseDTO)))
            .andExpect(status().isCreated());

        // Validate the Lease in the database
        List<Lease> leaseList = leaseRepository.findAll();
        assertThat(leaseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLease() throws Exception {
        // Initialize the database
        leaseRepository.saveAndFlush(lease);
        int databaseSizeBeforeDelete = leaseRepository.findAll().size();

        // Get the lease
        restLeaseMockMvc.perform(delete("/api/leases/{id}", lease.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Lease> leaseList = leaseRepository.findAll();
        assertThat(leaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lease.class);
        Lease lease1 = new Lease();
        lease1.setId(1L);
        Lease lease2 = new Lease();
        lease2.setId(lease1.getId());
        assertThat(lease1).isEqualTo(lease2);
        lease2.setId(2L);
        assertThat(lease1).isNotEqualTo(lease2);
        lease1.setId(null);
        assertThat(lease1).isNotEqualTo(lease2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeaseDTO.class);
        LeaseDTO leaseDTO1 = new LeaseDTO();
        leaseDTO1.setId(1L);
        LeaseDTO leaseDTO2 = new LeaseDTO();
        assertThat(leaseDTO1).isNotEqualTo(leaseDTO2);
        leaseDTO2.setId(leaseDTO1.getId());
        assertThat(leaseDTO1).isEqualTo(leaseDTO2);
        leaseDTO2.setId(2L);
        assertThat(leaseDTO1).isNotEqualTo(leaseDTO2);
        leaseDTO1.setId(null);
        assertThat(leaseDTO1).isNotEqualTo(leaseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(leaseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(leaseMapper.fromId(null)).isNull();
    }
}
