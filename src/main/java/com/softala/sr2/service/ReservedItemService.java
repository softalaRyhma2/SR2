package com.softala.sr2.service;

import com.softala.sr2.domain.ReservedItem;
import com.softala.sr2.repository.ReservedItemRepository;
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

    public ReservedItemService(ReservedItemRepository reservedItemRepository) {
        this.reservedItemRepository = reservedItemRepository;
    }

    /**
     * Save a reservedItem.
     *
     * @param reservedItem the entity to save.
     * @return the persisted entity.
     */
    public ReservedItem save(ReservedItem reservedItem) {
        log.debug("Request to save ReservedItem : {}", reservedItem);
        return reservedItemRepository.save(reservedItem);
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
