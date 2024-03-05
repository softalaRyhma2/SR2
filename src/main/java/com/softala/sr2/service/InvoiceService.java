package com.softala.sr2.service;

import com.softala.sr2.domain.Company;
import com.softala.sr2.domain.Invoice;
import com.softala.sr2.domain.User;
import com.softala.sr2.repository.InvoiceRepository;
import com.softala.sr2.repository.UserRepository;
import com.softala.sr2.security.SecurityUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.softala.sr2.domain.Invoice}.
 */
@Service
@Transactional
public class InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceService.class);

    private final InvoiceRepository invoiceRepository;

    private final UserRepository userRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, UserRepository userRepository) {
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
    }

    public List<Invoice> findAllInvoicesByLoggedInUser() {
        String currentUserLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Current user login not found"));

        User user = userRepository.findOneByLogin(currentUserLogin).orElseThrow(() -> new IllegalStateException("User not found"));

        if (isAdmin(user) || isRecser(user)) {
            return invoiceRepository.findAll();
        } else {
            Company userCompany = user.getCompany();
            if (userCompany != null) {
                return invoiceRepository.findByCompany(userCompany);
            }
        }
        return Collections.emptyList();
    }

    private boolean isRecser(User user) {
        return user.getAuthorities().stream().anyMatch(authority -> authority.getName().equals("ROLE_RECSER"));
    }

    private boolean isAdmin(User user) {
        // Voit toteuttaa adminin tarkistuksen tarpeidesi mukaan.
        // Tässä esimerkissä tarkistetaan, onko käyttäjällä ADMIN-rooli.
        return user.getAuthorities().stream().anyMatch(authority -> authority.getName().equals("ROLE_ADMIN"));
    }

    /**
     * Save a invoice.
     *
     * @param invoice the entity to save.
     * @return the persisted entity.
     */
    public Invoice save(Invoice invoice) {
        log.debug("Request to save Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    /**
     * Update a invoice.
     *
     * @param invoice the entity to save.
     * @return the persisted entity.
     */
    public Invoice update(Invoice invoice) {
        log.debug("Request to update Invoice : {}", invoice);
        return invoiceRepository.save(invoice);
    }

    /**
     * Partially update a invoice.
     *
     * @param invoice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Invoice> partialUpdate(Invoice invoice) {
        log.debug("Request to partially update Invoice : {}", invoice);

        return invoiceRepository
            .findById(invoice.getId())
            .map(existingInvoice -> {
                if (invoice.getTotalSum() != null) {
                    existingInvoice.setTotalSum(invoice.getTotalSum());
                }
                if (invoice.getInvoiceDate() != null) {
                    existingInvoice.setInvoiceDate(invoice.getInvoiceDate());
                }

                return existingInvoice;
            })
            .map(invoiceRepository::save);
    }

    /**
     * Get all the invoices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Invoice> findAll(Pageable pageable) {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAll(pageable);
    }

    /**
     * Get one invoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Invoice> findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findById(id);
    }

    /**
     * Delete the invoice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.deleteById(id);
    }
}
