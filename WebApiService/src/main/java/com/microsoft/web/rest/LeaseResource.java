package com.microsoft.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.microsoft.domain.Lease;

import com.microsoft.repository.LeaseRepository;
import com.microsoft.web.rest.util.HeaderUtil;
import com.microsoft.web.rest.util.PaginationUtil;
import com.microsoft.service.dto.LeaseDTO;
import com.microsoft.service.mapper.LeaseMapper;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Lease.
 */
@RestController
@RequestMapping("/api")
public class LeaseResource {

    private final Logger log = LoggerFactory.getLogger(LeaseResource.class);

    private static final String ENTITY_NAME = "lease";
        
    private final LeaseRepository leaseRepository;

    private final LeaseMapper leaseMapper;

    public LeaseResource(LeaseRepository leaseRepository, LeaseMapper leaseMapper) {
        this.leaseRepository = leaseRepository;
        this.leaseMapper = leaseMapper;
    }

    /**
     * POST  /leases : Create a new lease.
     *
     * @param leaseDTO the leaseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new leaseDTO, or with status 400 (Bad Request) if the lease has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/leases")
    @Timed
    public ResponseEntity<LeaseDTO> createLease(@Valid @RequestBody LeaseDTO leaseDTO) throws URISyntaxException {
        log.debug("REST request to save Lease : {}", leaseDTO);
        if (leaseDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new lease cannot already have an ID")).body(null);
        }
        Lease lease = leaseMapper.leaseDTOToLease(leaseDTO);
        lease = leaseRepository.save(lease);
        LeaseDTO result = leaseMapper.leaseToLeaseDTO(lease);
        return ResponseEntity.created(new URI("/api/leases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /leases : Updates an existing lease.
     *
     * @param leaseDTO the leaseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated leaseDTO,
     * or with status 400 (Bad Request) if the leaseDTO is not valid,
     * or with status 500 (Internal Server Error) if the leaseDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/leases")
    @Timed
    public ResponseEntity<LeaseDTO> updateLease(@Valid @RequestBody LeaseDTO leaseDTO) throws URISyntaxException {
        log.debug("REST request to update Lease : {}", leaseDTO);
        if (leaseDTO.getId() == null) {
            return createLease(leaseDTO);
        }
        Lease lease = leaseMapper.leaseDTOToLease(leaseDTO);
        lease = leaseRepository.save(lease);
        LeaseDTO result = leaseMapper.leaseToLeaseDTO(lease);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, leaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /leases : get all the leases.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of leases in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/leases")
    @Timed
    public ResponseEntity<List<LeaseDTO>> getAllLeases(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Leases");
        Page<Lease> page = leaseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/leases");
        return new ResponseEntity<>(leaseMapper.leasesToLeaseDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /leases/:id : get the "id" lease.
     *
     * @param id the id of the leaseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the leaseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/leases/{id}")
    @Timed
    public ResponseEntity<LeaseDTO> getLease(@PathVariable Long id) {
        log.debug("REST request to get Lease : {}", id);
        Lease lease = leaseRepository.findOne(id);
        LeaseDTO leaseDTO = leaseMapper.leaseToLeaseDTO(lease);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(leaseDTO));
    }

    /**
     * DELETE  /leases/:id : delete the "id" lease.
     *
     * @param id the id of the leaseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/leases/{id}")
    @Timed
    public ResponseEntity<Void> deleteLease(@PathVariable Long id) {
        log.debug("REST request to delete Lease : {}", id);
        leaseRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
