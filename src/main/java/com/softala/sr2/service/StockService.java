package com.softala.sr2.service;

import com.softala.sr2.domain.Stock;
import com.softala.sr2.repository.StockRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.softala.sr2.service.InvoiceService;
import com.softala.sr2.domain.Invoice;
import java.util.List;
import com.softala.sr2.domain.User;
import com.softala.sr2.repository.UserRepository;
import com.softala.sr2.security.SecurityUtils;
import com.softala.sr2.domain.Company;
import com.softala.sr2.repository.InvoiceRepository;
import java.util.Collections;
import org.springframework.stereotype.Service;  




/**
 * Service Implementation for managing {@link com.softala.sr2.domain.Stock}.
 */
@Service
@Transactional
public class StockService {

    private final Logger log = LoggerFactory.getLogger(StockService.class);

    private final StockRepository stockRepository;
    private final InvoiceService invoiceService;
    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;


    public StockService(StockRepository stockRepository, InvoiceService invoiceService, UserRepository userRepository, InvoiceRepository invoiceRepository) {
        this.stockRepository = stockRepository;
        this.invoiceService = invoiceService;
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
    }



    /**
     * Method to find all stocks linked to the current user's company's invoices.
     *
     * @return the list of stocks linked to the current user's company's invoices.
     */
    public List<Stock> findAllStocksByLoggedInUserCompany() {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isPresent()) {
            String login = currentUserLogin.get();
            Optional<User> user = userRepository.findOneByLogin(login);
            if (user.isPresent()) {
                // Check if the user is admin or recser
                if (isAdmin(user.get()) || isRecser(user.get())) {
                    // Return all stocks if the user is admin or recser
                    return stockRepository.findAll();
                } else {
                    // Get the user's company
                    Company userCompany = user.get().getCompany();
                    if (userCompany != null) {
                        // Return all stocks associated with the user's company
                        List<Invoice> invoices = invoiceRepository.findByCompany(userCompany);
                    return stockRepository.findAllByInvoice(invoices.get(0));
                        
                       
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
        // Voit toteuttaa adminin tarkistuksen tarpeidesi mukaan.
        // Tässä esimerkissä tarkistetaan, onko käyttäjällä ADMIN-rooli.
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
