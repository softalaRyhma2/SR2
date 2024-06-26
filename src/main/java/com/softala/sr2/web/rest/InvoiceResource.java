package com.softala.sr2.web.rest;

import com.softala.sr2.domain.Company;
import com.softala.sr2.domain.Invoice;
import com.softala.sr2.domain.Stock;
import com.softala.sr2.repository.InvoiceRepository;
import com.softala.sr2.service.InvoiceService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.softala.sr2.domain.Invoice}.
 */
@RestController
@RequestMapping("/api/invoices")
public class InvoiceResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceResource.class);

    private static final String ENTITY_NAME = "invoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceService invoiceService;

    private final InvoiceRepository invoiceRepository;

    public InvoiceResource(InvoiceService invoiceService, InvoiceRepository invoiceRepository) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping("/invoices/current")
    public ResponseEntity<List<Invoice>> findAllInvoicesByLoggedInUser() {
        List<Invoice> invoices = invoiceService.findAllInvoicesByLoggedInUser();
        return ResponseEntity.ok().body(invoices);
    }

    /**
     * {@code POST  /invoices} : Create a new invoice.
     *
     * @param invoice the invoice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new invoice, or with status {@code 400 (Bad Request)} if the
     *         invoice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice) throws URISyntaxException {
        log.debug("REST request to save Invoice : {}", invoice);
        if (invoice.getId() != null) {
            throw new BadRequestAlertException("A new invoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Invoice result = invoiceService.save(invoice);
        return ResponseEntity
            .created(new URI("/api/invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /invoices/:id} : Updates an existing invoice.
     *
     * @param id      the id of the invoice to save.
     * @param invoice the invoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated invoice,
     *         or with status {@code 400 (Bad Request)} if the invoice is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the invoice
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Invoice invoice
    ) throws URISyntaxException {
        log.debug("REST request to update Invoice : {}, {}", id, invoice);
        if (invoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Invoice result = invoiceService.update(invoice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /invoices/:id} : Partial updates given fields of an existing
     * invoice, field will ignore if it is null
     *
     * @param id      the id of the invoice to save.
     * @param invoice the invoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated invoice,
     *         or with status {@code 400 (Bad Request)} if the invoice is not valid,
     *         or with status {@code 404 (Not Found)} if the invoice is not found,
     *         or with status {@code 500 (Internal Server Error)} if the invoice
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Invoice> partialUpdateInvoice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Invoice invoice
    ) throws URISyntaxException {
        log.debug("REST request to partial update Invoice partially : {}, {}", id, invoice);
        if (invoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Invoice> result = invoiceService.partialUpdate(invoice);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoice.getId().toString())
        );
    }

    /**
     * {@code GET  /invoices} : get all the invoices by logged in user.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of invoices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Invoice>> getAllInvoicesForLoggedInUser() {
        List<Invoice> invoices = invoiceService.findAllInvoicesByLoggedInUser();
        return ResponseEntity.ok().body(invoices);
    }

    /**
     * {@code GET  /invoices/:id} : get the "id" invoice.
     *
     * @param id the id of the invoice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the invoice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable("id") Long id) {
        log.debug("REST request to get Invoice : {}", id);
        Optional<Invoice> invoiceOptional = invoiceService.findOne(id);

        return invoiceOptional
            .map(invoice -> {
                List<Stock> stocks = invoiceService.getStocksByInvoiceId(invoice);
                Set<Stock> stockSet = new HashSet<>(stocks);
                invoice.setStocks(stockSet);
                return ResponseEntity.ok(invoice);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /invoices/:id} : delete the "id" invoice.
     *
     * @param id the id of the invoice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable("id") Long id) {
        log.debug("REST request to delete Invoice : {}", id);

        // Check if there are stocks associated with the invoice
        Invoice invoice = invoiceService.findOne(id).orElseThrow(() -> new IllegalArgumentException("Invoice not found with id: " + id));

        if (!invoice.getStocks().isEmpty()) {
            throw new BadRequestAlertException("Invoice cannot be deleted because it has associated stocks", ENTITY_NAME, "cannotdelete");
        }

        // Proceed with deletion if no associated stocks are found
        invoiceService.delete(id);

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
