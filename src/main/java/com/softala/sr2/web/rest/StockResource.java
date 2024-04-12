package com.softala.sr2.web.rest;

import com.softala.sr2.domain.Invoice;
import com.softala.sr2.domain.Stock;
import com.softala.sr2.domain.StockItem;
import com.softala.sr2.repository.StockItemRepository;
import com.softala.sr2.repository.StockRepository;
import com.softala.sr2.service.StockService;
import com.softala.sr2.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.softala.sr2.domain.Stock}.
 */
@RestController
@RequestMapping("/api/stocks")
public class StockResource {

    private final Logger log = LoggerFactory.getLogger(StockResource.class);

    private static final String ENTITY_NAME = "stock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockService stockService;

    private final StockRepository stockRepository;

    public StockResource(StockService stockService, StockRepository stockRepository) {
        this.stockService = stockService;
        this.stockRepository = stockRepository;
    }

    @GetMapping("/stocks/current")
    public ResponseEntity<List<Stock>> findAllStocksByLoggedInUser() {
        List<Stock> stocks = stockService.findStocksForLoggedInUser();
        return ResponseEntity.ok().body(stocks);
    }

    /**
     * {@code POST  /stocks} : Create a new stock.
     *
     * @param stock the stock to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new stock, or with status {@code 400 (Bad Request)} if the
     *         stock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Stock> createStock(@Valid @RequestBody Stock stock) throws URISyntaxException {
        log.debug("REST request to save Stock : {}", stock);
        if (stock.getId() != null) {
            throw new BadRequestAlertException("A new stock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Stock result = stockService.save(stock);
        return ResponseEntity
            .created(new URI("/api/stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stocks/:id} : Updates an existing stock.
     *
     * @param id    the id of the stock to save.
     * @param stock the stock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated stock,
     *         or with status {@code 400 (Bad Request)} if the stock is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the stock
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Stock> updateStock(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Stock stock)
        throws URISyntaxException {
        log.debug("REST request to update Stock : {}, {}", id, stock);
        if (stock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Stock result = stockService.update(stock);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stock.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stocks/:id} : Partial updates given fields of an existing
     * stock, field will ignore if it is null
     *
     * @param id    the id of the stock to save.
     * @param stock the stock to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated stock,
     *         or with status {@code 400 (Bad Request)} if the stock is not valid,
     *         or with status {@code 404 (Not Found)} if the stock is not found,
     *         or with status {@code 500 (Internal Server Error)} if the stock
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Stock> partialUpdateStock(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Stock stock
    ) throws URISyntaxException {
        log.debug("REST request to partial update Stock partially : {}, {}", id, stock);
        if (stock.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stock.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Stock> result = stockService.partialUpdate(stock);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stock.getId().toString())
        );
    }

    /**
     * {@code GET  /stocks} : get all the stocks by logged in user.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of stocks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Stock>> getAllStocksByLoggedInUser() {
        List<Stock> stocks = stockService.findStocksForLoggedInUser();
        return ResponseEntity.ok().body(stocks);
    }

    /**
     * {@code GET  /stocks/:id} : get the "id" stock.
     *
     * @param id the id of the stock to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the stock, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStock(@PathVariable("id") Long id) {
        log.debug("REST request to get Stock : {}", id);
        Optional<Stock> stock = stockService.findOne(id);
        if (stock.isPresent()) {
            Stock fetchedStock = stock.get();
            List<StockItem> stockItems = stockService.getStockItemsByStockId(fetchedStock);
            Set<StockItem> stockItemSet = new HashSet<>(stockItems);
            fetchedStock.setStockItems(stockItemSet);
        }
        return ResponseUtil.wrapOrNotFound(stock);
    }

    /**
     * {@code DELETE  /stocks/:id} : delete the "id" stock.
     *
     * @param id the id of the stock to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_RECSER', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteStock(@PathVariable("id") Long id) {
        log.debug("REST request to delete Stock : {}", id);
        stockService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
