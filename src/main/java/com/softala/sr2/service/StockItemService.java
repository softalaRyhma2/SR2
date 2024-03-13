package com.softala.sr2.service;

import com.softala.sr2.domain.StockItem;
import com.softala.sr2.repository.StockItemRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.softala.sr2.domain.StockItem}.
 */
@Service
@Transactional
public class StockItemService {

    private final Logger log = LoggerFactory.getLogger(StockItemService.class);

    private final StockItemRepository stockItemRepository;

    public StockItemService(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    /**
     * Save a stockItem.
     *
     * @param stockItem the entity to save.
     * @return the persisted entity.
     */
    public StockItem save(StockItem stockItem) {
        log.debug("Request to save StockItem : {}", stockItem);
        return stockItemRepository.save(stockItem);
    }

    /**
     * Update a stockItem.
     *
     * @param stockItem the entity to save.
     * @return the persisted entity.
     */
    public StockItem update(StockItem stockItem) {
        log.debug("Request to update StockItem : {}", stockItem);
        return stockItemRepository.save(stockItem);
    }

    /**
     * Partially update a stockItem.
     *
     * @param stockItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StockItem> partialUpdate(StockItem stockItem) {
        log.debug("Request to partially update StockItem : {}", stockItem);

        return stockItemRepository
            .findById(stockItem.getId())
            .map(existingStockItem -> {
                if (stockItem.getQuantity() != null) {
                    existingStockItem.setQuantity(stockItem.getQuantity());
                }
                if (stockItem.getAvailable() != null) {
                    existingStockItem.setAvailable(stockItem.getAvailable());
                }
                if (stockItem.getPrice() != null) {
                    existingStockItem.setPrice(stockItem.getPrice());
                }

                return existingStockItem;
            })
            .map(stockItemRepository::save);
    }

    /**
     * Get all the stockItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StockItem> findAll(Pageable pageable) {
        log.debug("Request to get all StockItems");
        return stockItemRepository.findAll(pageable);
    }

    /**
     * Get one stockItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StockItem> findOne(Long id) {
        log.debug("Request to get StockItem : {}", id);
        return stockItemRepository.findById(id);
    }

    /**
     * Delete the stockItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StockItem : {}", id);
        stockItemRepository.deleteById(id);
    }
}
