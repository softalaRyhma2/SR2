package com.softala.sr2.service;

import com.softala.sr2.domain.Reservation;
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
        StockItem stockItem = optionalStockItem.orElseThrow(() -> new IllegalArgumentException("StockItem not found"));

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
    }

    /**
     * Update a reservedItem.
     *
     * @param reservedItem the entity to save.
     * @return the persisted entity.
     */
    public ReservedItem update(ReservedItem reservedItem) {
        log.debug("Request to update ReservedItem : {}", reservedItem);
        Optional<ReservedItem> existingReservedItemOptional = reservedItemRepository.findById(reservedItem.getId());

        existingReservedItemOptional.ifPresent(existingReservedItem -> {
            Reservation reservation = existingReservedItem.getReservation();
            if (reservation != null && reservation.getIsPickedUp()) {
                throw new IllegalArgumentException("Cannot update a ReservedItem associated with a closed Reservation.");
            }
            int oldQuantity = existingReservedItem.getQuantity();
            log.info("Request to update ReservedItem oldQ : {}", oldQuantity);
            int newQuantity = reservedItem.getQuantity();
            log.info("Request to update ReservedItem newQ : {}", newQuantity);

            // Fetching related StockItem
            StockItem stockItem = existingReservedItem.getStockItem();
            if (stockItem != null) {
                int stockQuantity = stockItem.getQuantity();
                int stockAvailable = stockItem.getAvailable();
                log.info("Request to update ReservedItem available in stock : {}", stockAvailable);
                int difference = newQuantity - oldQuantity;
                log.info("Request to update ReservedItem difference : {}", difference);
                int newAmount;
                if (difference >= 0) {
                    newAmount = difference + oldQuantity;
                } else {
                    newAmount = oldQuantity + difference;
                }
                // check if it's enough available items in stock
                if (stockAvailable - difference >= 0 && stockAvailable - difference <= stockQuantity) {
                    // update ReservedItem
                    existingReservedItem.setQuantity(newAmount);
                    reservedItemRepository.save(existingReservedItem);

                    // update StockItem
                    stockItem.setAvailable(stockAvailable - difference);
                    stockItemRepository.save(stockItem);
                } else {
                    throw new IllegalArgumentException("Not enough quantity available in stock");
                }
            } else {
                throw new IllegalArgumentException("StockItem not found");
            }
        });

        return reservedItem;
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
                    update(existingReservedItem);
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
        log.debug("Request to delete ReservedItem: {}", id);

        // Fetch the ReservedItem by id
        Optional<ReservedItem> reservedItemOptional = reservedItemRepository.findById(id);

        reservedItemOptional.ifPresent(reservedItem -> {
            // Check if the ReservedItem is associated with a StockItem
            StockItem stockItem = reservedItem.getStockItem();
            if (stockItem != null) {
                // Iterate over the associated ReservedItems of the StockItem
                for (ReservedItem associatedReservedItem : stockItem.getReservedItems()) {
                    // Check if the current ReservedItem matches any associated ReservedItem by ID
                    if (associatedReservedItem.getId().equals(reservedItem.getId())) {
                        // The ReservedItem is associated with an active StockItem
                        throw new IllegalStateException("Cannot delete ReservedItem because it is associated with an active StockItem.");
                    }
                }
                //if reserved item is in reservation which is not picked up, when deleting reserved amount is returned back to the stock
                stockItem.setAvailable(stockItem.getAvailable() + reservedItem.getQuantity());
                stockItemRepository.save(stockItem);
            }
            // If the ReservedItem is not associated with any StockItem
            // or if it is not associated with the same ReservedItem as any associated ReservedItem,
            // then proceed with deletion
            reservedItemRepository.deleteById(id);
        });
    }
}
