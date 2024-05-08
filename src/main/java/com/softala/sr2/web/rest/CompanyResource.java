package com.softala.sr2.web.rest;

import com.softala.sr2.domain.Company;
import com.softala.sr2.domain.Invoice;
import com.softala.sr2.domain.User;
import com.softala.sr2.repository.CompanyRepository;
import com.softala.sr2.repository.StockItemTypeCompanyRepository;
import com.softala.sr2.repository.UserRepository;
import com.softala.sr2.security.SecurityUtils;
import com.softala.sr2.service.CompanyService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.softala.sr2.domain.Company}.
 */
@RestController
@RequestMapping("/api/companies")
public class CompanyResource {

    private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

    private static final String ENTITY_NAME = "company";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyService companyService;

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    private final InvoiceService invoiceService;

    private final StockItemTypeCompanyRepository stockItemTypeCompanyRepository;

    public CompanyResource(
        CompanyService companyService,
        CompanyRepository companyRepository,
        UserRepository userRepository,
        InvoiceService invoiceService,
        StockItemTypeCompanyRepository stockItemTypeCompanyRepository
    ) {
        this.companyService = companyService;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.invoiceService = invoiceService;
        this.stockItemTypeCompanyRepository = stockItemTypeCompanyRepository;
    }

    @GetMapping("/companies/current")
    public ResponseEntity<List<Company>> findAllCompaniesByLoggedInUser() {
        List<Company> companies = companyService.findAllCompaniesByLoggedInUser();
        return ResponseEntity.ok().body(companies);
    }

    @GetMapping("/currentUserCompany")
    public ResponseEntity<Company> getCurrentUserCompany() {
        String currentUserLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Current user login not found"));

        User user = userRepository
            .findOneByLogin(currentUserLogin)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + currentUserLogin));

        Company userCompany = user.getCompany();
        if (userCompany != null) {
            return ResponseEntity.ok().body(userCompany);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * {@code POST  /companies} : Create a new company.
     *
     * @param company the company to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new company, or with status {@code 400 (Bad Request)} if the
     *         company has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) throws URISyntaxException {
        log.debug("REST request to save Company : {}", company);
        if (company.getId() != null) {
            throw new BadRequestAlertException("A new company cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Company result = companyService.save(company);
        return ResponseEntity
            .created(new URI("/api/companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /companies/:id} : Updates an existing company.
     *
     * @param id      the id of the company to save.
     * @param company the company to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated company,
     *         or with status {@code 400 (Bad Request)} if the company is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the company
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Company company
    ) throws URISyntaxException {
        log.debug("REST request to update Company : {}, {}", id, company);
        if (company.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, company.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Company result = companyService.update(company);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, company.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /companies/:id} : Partial updates given fields of an existing
     * company, field will ignore if it is null
     *
     * @param id      the id of the company to save.
     * @param company the company to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated company,
     *         or with status {@code 400 (Bad Request)} if the company is not valid,
     *         or with status {@code 404 (Not Found)} if the company is not found,
     *         or with status {@code 500 (Internal Server Error)} if the company
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Company> partialUpdateCompany(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Company company
    ) throws URISyntaxException {
        log.debug("REST request to partial update Company partially : {}, {}", id, company);
        if (company.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, company.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!companyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Company> result = companyService.partialUpdate(company);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, company.getId().toString())
        );
    }

    /**
     * {@code GET  /companies} : get all the companies.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of companies in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Company>> getAllCompaniesForLoggedInUser() {
        List<Company> companies = companyService.findAllCompaniesByLoggedInUser();
        return ResponseEntity.ok().body(companies);
    }

    /**
     * {@code GET  /companies/:id} : get the "id" company.
     *
     * @param id the id of the company to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the company, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable("id") Long id) {
        log.debug("REST request to get Company : {}", id);
        List<Company> companies = companyService.findAllCompaniesByLoggedInUser();
        Optional<Company> requestedCompany = companies.stream().filter(company -> company.getId().equals(id)).findFirst();
        if (requestedCompany.isPresent()) {
            Company fetchedCompany = requestedCompany.get();
            List<Invoice> invoices = invoiceService.getInvoicesByCompanyId(fetchedCompany);
            Set<Invoice> invoiceSet = new HashSet<>(invoices);
            fetchedCompany.setInvoices(invoiceSet);
        }

        return requestedCompany.map(company -> ResponseEntity.ok().body(company)).orElse(ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /companies/:id} : delete the "id" company.
     *
     * @param id the id of the company to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_RECSER', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") Long id) {
        log.debug("REST request to delete Company : {}", id);
        companyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
