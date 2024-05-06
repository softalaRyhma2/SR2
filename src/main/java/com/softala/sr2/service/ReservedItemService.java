package com.softala.sr2.service;

import com.softala.sr2.domain.ReservedItem;
import com.softala.sr2.domain.StockItem;
import com.softala.sr2.domain.StockItemTypeCompany;
import com.softala.sr2.repository.ReservedItemRepository;
import com.softala.sr2.repository.StockItemRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.softala.sr2.domain.ReservedItem}.
 */
@Service
@Transactional
public class ReservedItemService {

    private final Logger log = LoggerFactory.getLogger(ReservedItemService.class);

    private final ReservedItemRepository reservedItemRepository;
    private final StockItemRepository stockItemRepository;

    public ReservedItemService(ReservedItemRepository reservedItemRepository, StockItemRepository stockItemRepository) {
        this.reservedItemRepository = reservedItemRepository;
        this.stockItemRepository = stockItemRepository;
    }

    public ReservedItem save(ReservedItem reservedItem) {
        log.debug("Request to save ReservedItem : {}", reservedItem);

        Optional<StockItem> optionalStockItem = stockItemRepository.findById(reservedItem.getStockItem().getId());
        if (optionalStockItem.isPresent()) {
            StockItem stockItem = optionalStockItem.get();

            int reservedQuantity = reservedItem.getQuantity();
            int currentQuantity = stockItem.getQuantity();
            int currentAvailable = stockItem.getAvailable();
            StockItemTypeCompany stockItemTypeCompany = reservedItem.getStockItem().getStockItemTypeCompany();

            if (currentQuantity >= reservedQuantity && currentAvailable >= reservedQuantity) {
                // If reservation has no items:
                if (stockItemTypeCompany == null) {
                    int newQuantity = currentQuantity - reservedQuantity;
                    stockItem.setAvailable(newQuantity);
                    stockItemRepository.save(stockItem);
                    return reservedItemRepository.save(reservedItem);
                }

                // If reservation has items, check for saving right amount to stock:
                if (stockItemTypeCompany.equals(stockItem.getStockItemTypeCompany())) {
                    int newQuantity = currentAvailable - reservedQuantity;
                    stockItem.setAvailable(newQuantity);
                    stockItemRepository.save(stockItem);
                    return reservedItemRepository.save(reservedItem);
                } else {
                    throw new IllegalArgumentException("StockItemTypeCompany does not match for the reserved item");
                }
            } else {
                throw new IllegalArgumentException("Not enough quantity available in stock");
            }
        } else {
            throw new IllegalArgumentException("StockItem not found");
        }
    }

    /**
     * Update a reservedItem.
     *
     * @param reservedItem the entity to save.
     * @return the persisted entity.
     */
    public ReservedItem update(ReservedItem reservedItem) {
        log.debug("Request to update ReservedItem : {}", reservedItem);
        return reservedItemRepository.save(reservedItem);
    }

    /**
     * Partially update a reservedItem.
     *
     * @param reservedItem the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReservedItem> partialUpdate(ReservedItem reservedItem) {
        log.debug("Request to partially update ReservedItem : {}", reservedItem);

        return reservedItemRepository
            .findById(reservedItem.getId())
            .map(existingReservedItem -> {
                if (reservedItem.getQuantity() != null) {
                    existingReservedItem.setQuantity(reservedItem.getQuantity());
                }

                return existingReservedItem;
            })
            .map(reservedItemRepository::save);
    }

    /**
     * Get all the reservedItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ReservedItem> findAll(Pageable pageable) {
        log.debug("Request to get all ReservedItems");
        return reservedItemRepository.findAll(pageable);
    }

    /**
     * Get one reservedItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReservedItem> findOne(Long id) {
        log.debug("Request to get ReservedItem : {}", id);
        return reservedItemRepository.findById(id);
    }

    /**
     * Delete the reservedItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ReservedItem : {}", id);
        reservedItemRepository.deleteById(id);
    }
}
