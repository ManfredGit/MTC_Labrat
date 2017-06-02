package com.microsoft.labrat.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.microsoft.labrat.domain.Intervention;

import com.microsoft.labrat.repository.InterventionRepository;
import com.microsoft.labrat.web.rest.util.HeaderUtil;
import com.microsoft.labrat.service.dto.InterventionDTO;
import com.microsoft.labrat.service.mapper.InterventionMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Intervention.
 */
@RestController
@RequestMapping("/api")
public class InterventionResource {

    private final Logger log = LoggerFactory.getLogger(InterventionResource.class);

    private static final String ENTITY_NAME = "intervention";

    private final InterventionRepository interventionRepository;

    private final InterventionMapper interventionMapper;

    public InterventionResource(InterventionRepository interventionRepository, InterventionMapper interventionMapper) {
        this.interventionRepository = interventionRepository;
        this.interventionMapper = interventionMapper;
    }

    /**
     * POST  /interventions : Create a new intervention.
     *
     * @param interventionDTO the interventionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new interventionDTO, or with status 400 (Bad Request) if the intervention has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/interventions")
    @Timed
    public ResponseEntity<InterventionDTO> createIntervention(@Valid @RequestBody InterventionDTO interventionDTO) throws URISyntaxException {
        log.debug("REST request to save Intervention : {}", interventionDTO);
        if (interventionDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new intervention cannot already have an ID")).body(null);
        }
        Intervention intervention = interventionMapper.toEntity(interventionDTO);
        intervention = interventionRepository.save(intervention);
        InterventionDTO result = interventionMapper.toDto(intervention);
        return ResponseEntity.created(new URI("/api/interventions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /interventions : Updates an existing intervention.
     *
     * @param interventionDTO the interventionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated interventionDTO,
     * or with status 400 (Bad Request) if the interventionDTO is not valid,
     * or with status 500 (Internal Server Error) if the interventionDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/interventions")
    @Timed
    public ResponseEntity<InterventionDTO> updateIntervention(@Valid @RequestBody InterventionDTO interventionDTO) throws URISyntaxException {
        log.debug("REST request to update Intervention : {}", interventionDTO);
        if (interventionDTO.getId() == null) {
            return createIntervention(interventionDTO);
        }
        Intervention intervention = interventionMapper.toEntity(interventionDTO);
        intervention = interventionRepository.save(intervention);
        InterventionDTO result = interventionMapper.toDto(intervention);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, interventionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /interventions : get all the interventions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of interventions in body
     */
    @GetMapping("/interventions")
    @Timed
    public List<InterventionDTO> getAllInterventions() {
        log.debug("REST request to get all Interventions");
        List<Intervention> interventions = interventionRepository.findAll();
        return interventionMapper.toDto(interventions);
    }

    /**
     * GET  /interventions/:id : get the "id" intervention.
     *
     * @param id the id of the interventionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the interventionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/interventions/{id}")
    @Timed
    public ResponseEntity<InterventionDTO> getIntervention(@PathVariable Long id) {
        log.debug("REST request to get Intervention : {}", id);
        Intervention intervention = interventionRepository.findOne(id);
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(interventionDTO));
    }

    /**
     * DELETE  /interventions/:id : delete the "id" intervention.
     *
     * @param id the id of the interventionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/interventions/{id}")
    @Timed
    public ResponseEntity<Void> deleteIntervention(@PathVariable Long id) {
        log.debug("REST request to delete Intervention : {}", id);
        interventionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
