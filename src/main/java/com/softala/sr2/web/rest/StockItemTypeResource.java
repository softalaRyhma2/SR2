package com.softala.sr2.web.rest;

import com.softala.sr2.domain.StockItemType;
import com.softala.sr2.repository.StockItemTypeRepository;
import com.softala.sr2.service.StockItemTypeService;
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
 * REST controller for managing {@link com.softala.sr2.domain.StockItemType}.
 */
@RestController
@RequestMapping("/api/stock-item-types")
public class StockItemTypeResource {

    private final Logger log = LoggerFactory.getLogger(StockItemTypeResource.class);

    private static final String ENTITY_NAME = "stockItemType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockItemTypeService stockItemTypeService;

    private final StockItemTypeRepository stockItemTypeRepository;

    public StockItemTypeResource(StockItemTypeService stockItemTypeService, StockItemTypeRepository stockItemTypeRepository) {
        this.stockItemTypeService = stockItemTypeService;
        this.stockItemTypeRepository = stockItemTypeRepository;
    }

    /**
     * {@code POST  /stock-item-types} : Create a new stockItemType.
     *
     * @param stockItemType the stockItemType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockItemType, or with status {@code 400 (Bad Request)} if the stockItemType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StockItemType> createStockItemType(@Valid @RequestBody StockItemType stockItemType) throws URISyntaxException {
        log.debug("REST request to save StockItemType : {}", stockItemType);
        if (stockItemType.getId() != null) {
            throw new BadRequestAlertException("A new stockItemType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockItemType result = stockItemTypeService.save(stockItemType);
        return ResponseEntity
            .created(new URI("/api/stock-item-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stock-item-types/:id} : Updates an existing stockItemType.
     *
     * @param id the id of the stockItemType to save.
     * @param stockItemType the stockItemType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockItemType,
     * or with status {@code 400 (Bad Request)} if the stockItemType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockItemType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StockItemType> updateStockItemType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StockItemType stockItemType
    ) throws URISyntaxException {
        log.debug("REST request to update StockItemType : {}, {}", id, stockItemType);
        if (stockItemType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockItemType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockItemTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StockItemType result = stockItemTypeService.update(stockItemType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockItemType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stock-item-types/:id} : Partial updates given fields of an existing stockItemType, field will ignore if it is null
     *
     * @param id the id of the stockItemType to save.
     * @param stockItemType the stockItemType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockItemType,
     * or with status {@code 400 (Bad Request)} if the stockItemType is not valid,
     * or with status {@code 404 (Not Found)} if the stockItemType is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockItemType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StockItemType> partialUpdateStockItemType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StockItemType stockItemType
    ) throws URISyntaxException {
        log.debug("REST request to partial update StockItemType partially : {}, {}", id, stockItemType);
        if (stockItemType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockItemType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockItemTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockItemType> result = stockItemTypeService.partialUpdate(stockItemType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockItemType.getId().toString())
        );
    }

    /**
     * {@code GET  /stock-item-types} : get all the stockItemTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockItemTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<StockItemType>> getAllStockItemTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of StockItemTypes");
        Page<StockItemType> page = stockItemTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stock-item-types/:id} : get the "id" stockItemType.
     *
     * @param id the id of the stockItemType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockItemType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockItemType> getStockItemType(@PathVariable("id") Long id) {
        log.debug("REST request to get StockItemType : {}", id);
        Optional<StockItemType> stockItemType = stockItemTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockItemType);
    }

    /**
     * {@code DELETE  /stock-item-types/:id} : delete the "id" stockItemType.
     *
     * @param id the id of the stockItemType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStockItemType(@PathVariable("id") Long id) {
        log.debug("REST request to delete StockItemType : {}", id);
        stockItemTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
