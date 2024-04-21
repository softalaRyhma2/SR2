package com.softala.sr2.web.rest;

import com.softala.sr2.domain.StockItemTypeCompany;
import com.softala.sr2.repository.StockItemTypeCompanyRepository;
import com.softala.sr2.service.StockItemTypeCompanyService;
import com.softala.sr2.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.softala.sr2.domain.StockItemTypeCompany}.
 */
@RestController
@RequestMapping("/api/stock-item-type-companies")
public class StockItemTypeCompanyResource {

    private final Logger log = LoggerFactory.getLogger(StockItemTypeCompanyResource.class);

    private static final String ENTITY_NAME = "stockItemTypeCompany";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockItemTypeCompanyService stockItemTypeCompanyService;

    private final StockItemTypeCompanyRepository stockItemTypeCompanyRepository;

    public StockItemTypeCompanyResource(
        StockItemTypeCompanyService stockItemTypeCompanyService,
        StockItemTypeCompanyRepository stockItemTypeCompanyRepository
    ) {
        this.stockItemTypeCompanyService = stockItemTypeCompanyService;
        this.stockItemTypeCompanyRepository = stockItemTypeCompanyRepository;
    }

    /**
     * {@code POST  /stock-item-type-companies} : Create a new stockItemTypeCompany.
     *
     * @param stockItemTypeCompany the stockItemTypeCompany to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockItemTypeCompany, or with status {@code 400 (Bad Request)} if the stockItemTypeCompany has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StockItemTypeCompany> createStockItemTypeCompany(@Valid @RequestBody StockItemTypeCompany stockItemTypeCompany)
        throws URISyntaxException {
        log.debug("REST request to save StockItemTypeCompany : {}", stockItemTypeCompany);
        if (stockItemTypeCompany.getId() != null) {
            throw new BadRequestAlertException("A new stockItemTypeCompany cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockItemTypeCompany result = stockItemTypeCompanyService.save(stockItemTypeCompany);
        return ResponseEntity
            .created(new URI("/api/stock-item-type-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stock-item-type-companies/:id} : Updates an existing stockItemTypeCompany.
     *
     * @param id the id of the stockItemTypeCompany to save.
     * @param stockItemTypeCompany the stockItemTypeCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockItemTypeCompany,
     * or with status {@code 400 (Bad Request)} if the stockItemTypeCompany is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockItemTypeCompany couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StockItemTypeCompany> updateStockItemTypeCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StockItemTypeCompany stockItemTypeCompany
    ) throws URISyntaxException {
        log.debug("REST request to update StockItemTypeCompany : {}, {}", id, stockItemTypeCompany);
        if (stockItemTypeCompany.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockItemTypeCompany.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockItemTypeCompanyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StockItemTypeCompany result = stockItemTypeCompanyService.update(stockItemTypeCompany);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockItemTypeCompany.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stock-item-type-companies/:id} : Partial updates given fields of an existing stockItemTypeCompany, field will ignore if it is null
     *
     * @param id the id of the stockItemTypeCompany to save.
     * @param stockItemTypeCompany the stockItemTypeCompany to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockItemTypeCompany,
     * or with status {@code 400 (Bad Request)} if the stockItemTypeCompany is not valid,
     * or with status {@code 404 (Not Found)} if the stockItemTypeCompany is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockItemTypeCompany couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StockItemTypeCompany> partialUpdateStockItemTypeCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StockItemTypeCompany stockItemTypeCompany
    ) throws URISyntaxException {
        log.debug("REST request to partial update StockItemTypeCompany partially : {}, {}", id, stockItemTypeCompany);
        if (stockItemTypeCompany.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockItemTypeCompany.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockItemTypeCompanyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockItemTypeCompany> result = stockItemTypeCompanyService.partialUpdate(stockItemTypeCompany);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockItemTypeCompany.getId().toString())
        );
    }

    /**
     * {@code GET  /stock-item-type-companies} : get all the stockItemTypeCompanies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockItemTypeCompanies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StockItemTypeCompany>> getAllStockItemTypeCompanies(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of StockItemTypeCompanies");
        Page<StockItemTypeCompany> page = stockItemTypeCompanyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stock-item-type-companies/:id} : get the "id" stockItemTypeCompany.
     *
     * @param id the id of the stockItemTypeCompany to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockItemTypeCompany, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockItemTypeCompany> getStockItemTypeCompany(@PathVariable("id") Long id) {
        log.debug("REST request to get StockItemTypeCompany : {}", id);
        Optional<StockItemTypeCompany> stockItemTypeCompany = stockItemTypeCompanyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockItemTypeCompany);
    }

    /**
     * {@code DELETE  /stock-item-type-companies/:id} : delete the "id" stockItemTypeCompany.
     *
     * @param id the id of the stockItemTypeCompany to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockItemTypeCompany(@PathVariable("id") Long id) {
        log.debug("REST request to delete StockItemTypeCompany : {}", id);
        stockItemTypeCompanyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
