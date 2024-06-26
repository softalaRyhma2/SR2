package com.softala.sr2.service;

import com.softala.sr2.domain.Invoice;
import com.softala.sr2.domain.Stock;
import com.softala.sr2.domain.StockItem;
import com.softala.sr2.repository.StockItemRepository;
import com.softala.sr2.repository.StockRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.softala.sr2.domain.Stock}.
 */
@Service
@Transactional
public class StockService {

    private final Logger log = LoggerFactory.getLogger(StockService.class);

    private final StockRepository stockRepository;

    private final InvoiceService invoiceService;
    private final StockItemRepository stockItemRepository;

    public StockService(StockRepository stockRepository, InvoiceService invoiceService, StockItemRepository stockItemRepository) {
        this.stockRepository = stockRepository;
        this.invoiceService = invoiceService;
        this.stockItemRepository = stockItemRepository;
    }

    public List<Stock> findStocksForLoggedInUser() {
        List<Invoice> invoices = invoiceService.findAllInvoicesByLoggedInUser();
        return stockRepository.findByInvoiceIn(invoices);
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

    @Transactional(readOnly = true)
    public List<StockItem> getStockItemsByStockId(Stock stock) {
        log.debug("Request to get Stock Items for one Stock");
        return stockItemRepository.findByStock(stock);
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
