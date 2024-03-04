package com.softala.sr2.service;

import com.softala.sr2.domain.Company;
import com.softala.sr2.domain.Invoice;
import com.softala.sr2.domain.Stock;
import com.softala.sr2.domain.User;
import com.softala.sr2.repository.InvoiceRepository;
import com.softala.sr2.repository.StockRepository;
import com.softala.sr2.repository.UserRepository;
import com.softala.sr2.security.SecurityUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.softala.sr2.domain.Stock}.
 */
@Service
@Transactional
public class StockService {

    private final Logger log = LoggerFactory.getLogger(StockService.class);

    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;

    public StockService(
        StockRepository stockRepository,
        InvoiceRepository invoiceRepository,
        UserRepository userRepository,
        InvoiceService invoiceService
    ) {
        this.stockRepository = stockRepository;
        this.invoiceRepository = invoiceRepository;
        this.userRepository = userRepository;
        this.invoiceService = invoiceService;
    }

    public List<Stock> findStocksByInvoices() {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isPresent()) {
            String login = currentUserLogin.get();
            Optional<User> user = userRepository.findOneByLogin(login);
            if (user.isPresent()) {
                // Tarkistetaan, onko käyttäjä admin
                if (isAdmin(user.get()) || isRecser(user.get())) {
                    // Palautetaan kaikki varastot
                    return stockRepository.findAll();
                } else {
                    // Haetaan käyttäjän yritys
                    Company userCompany = user.get().getCompany();
                    if (userCompany != null) {
                        // Haetaan yrityksen varasto
                        List<Invoice> companyInvoices = invoiceRepository.findByCompany(userCompany);
                        return stockRepository.findByInvoices(companyInvoices);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private boolean isRecser(User user) {
        return user.getAuthorities().stream().anyMatch(authority -> authority.getName().equals("ROLE_RECSER"));
    }

    private boolean isAdmin(User user) {
        return user.getAuthorities().stream().anyMatch(authority -> authority.getName().equals("ROLE_ADMIN"));
    }

    /**
     * Save a stock.
     *
     * @param stock the entity to save.
     * @return the persisted entity.
     */
    public Stock save(Stock stock) {
        log.debug("Request to save Stock : {}", stock);
        return stockRepository.save(stock);
    }

    /**
     * Update a stock.
     *
     * @param stock the entity to save.
     * @return the persisted entity.
     */
    public Stock update(Stock stock) {
        log.debug("Request to update Stock : {}", stock);
        return stockRepository.save(stock);
    }

    /**
     * Partially update a stock.
     *
     * @param stock the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Stock> partialUpdate(Stock stock) {
        log.debug("Request to partially update Stock : {}", stock);

        return stockRepository
            .findById(stock.getId())
            .map(existingStock -> {
                if (stock.getQuantity() != null) {
                    existingStock.setQuantity(stock.getQuantity());
                }
                if (stock.getAvailable() != null) {
                    existingStock.setAvailable(stock.getAvailable());
                }
                if (stock.getPrice() != null) {
                    existingStock.setPrice(stock.getPrice());
                }
                if (stock.getStockDate() != null) {
                    existingStock.setStockDate(stock.getStockDate());
                }

                return existingStock;
            })
            .map(stockRepository::save);
    }

    /**
     * Get all the stocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Stock> findAll(Pageable pageable) {
        log.debug("Request to get all Stocks");
        return stockRepository.findAll(pageable);
    }

    /**
     * Get one stock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Stock> findOne(Long id) {
        log.debug("Request to get Stock : {}", id);
        return stockRepository.findById(id);
    }

    /**
     * Delete the stock by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Stock : {}", id);
        stockRepository.deleteById(id);
    }
}
