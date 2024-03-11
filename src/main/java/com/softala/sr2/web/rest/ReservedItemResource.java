package com.softala.sr2.web.rest;

import com.softala.sr2.domain.ReservedItem;
import com.softala.sr2.repository.ReservedItemRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.softala.sr2.domain.ReservedItem}.
 */
@RestController
@RequestMapping("/api/reserved-items")
@Transactional
public class ReservedItemResource {

    private final Logger log = LoggerFactory.getLogger(ReservedItemResource.class);

    private static final String ENTITY_NAME = "reservedItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReservedItemRepository reservedItemRepository;

    public ReservedItemResource(ReservedItemRepository reservedItemRepository) {
        this.reservedItemRepository = reservedItemRepository;
    }

    /**
     * {@code POST  /reserved-items} : Create a new reservedItem.
     *
     * @param reservedItem the reservedItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reservedItem, or with status {@code 400 (Bad Request)} if the reservedItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReservedItem> createReservedItem(@Valid @RequestBody ReservedItem reservedItem) throws URISyntaxException {
        log.debug("REST request to save ReservedItem : {}", reservedItem);
        if (reservedItem.getId() != null) {
            throw new BadRequestAlertException("A new reservedItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReservedItem result = reservedItemRepository.save(reservedItem);
        return ResponseEntity
            .created(new URI("/api/reserved-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reserved-items/:id} : Updates an existing reservedItem.
     *
     * @param id the id of the reservedItem to save.
     * @param reservedItem the reservedItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservedItem,
     * or with status {@code 400 (Bad Request)} if the reservedItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reservedItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReservedItem> updateReservedItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReservedItem reservedItem
    ) throws URISyntaxException {
        log.debug("REST request to update ReservedItem : {}, {}", id, reservedItem);
        if (reservedItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservedItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservedItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReservedItem result = reservedItemRepository.save(reservedItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservedItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reserved-items/:id} : Partial updates given fields of an existing reservedItem, field will ignore if it is null
     *
     * @param id the id of the reservedItem to save.
     * @param reservedItem the reservedItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reservedItem,
     * or with status {@code 400 (Bad Request)} if the reservedItem is not valid,
     * or with status {@code 404 (Not Found)} if the reservedItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the reservedItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReservedItem> partialUpdateReservedItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReservedItem reservedItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReservedItem partially : {}, {}", id, reservedItem);
        if (reservedItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reservedItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reservedItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReservedItem> result = reservedItemRepository
            .findById(reservedItem.getId())
            .map(existingReservedItem -> {
                if (reservedItem.getQuantity() != null) {
                    existingReservedItem.setQuantity(reservedItem.getQuantity());
                }

                return existingReservedItem;
            })
            .map(reservedItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reservedItem.getId().toString())
        );
    }

    /**
     * {@code GET  /reserved-items} : get all the reservedItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reservedItems in body.
     */
    @GetMapping("")
    public List<ReservedItem> getAllReservedItems() {
        log.debug("REST request to get all ReservedItems");
        return reservedItemRepository.findAll();
    }

    /**
     * {@code GET  /reserved-items/:id} : get the "id" reservedItem.
     *
     * @param id the id of the reservedItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reservedItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservedItem> getReservedItem(@PathVariable("id") Long id) {
        log.debug("REST request to get ReservedItem : {}", id);
        Optional<ReservedItem> reservedItem = reservedItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reservedItem);
    }

    /**
     * {@code DELETE  /reserved-items/:id} : delete the "id" reservedItem.
     *
     * @param id the id of the reservedItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservedItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete ReservedItem : {}", id);
        reservedItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
