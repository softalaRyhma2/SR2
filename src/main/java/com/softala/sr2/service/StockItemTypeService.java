package com.softala.sr2.service;

import com.softala.sr2.domain.StockItemType;
import com.softala.sr2.repository.StockItemTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.softala.sr2.domain.StockItemType}.
 */
@Service
@Transactional
public class StockItemTypeService {

    private final Logger log = LoggerFactory.getLogger(StockItemTypeService.class);

    private final StockItemTypeRepository stockItemTypeRepository;

    public StockItemTypeService(StockItemTypeRepository stockItemTypeRepository) {
        this.stockItemTypeRepository = stockItemTypeRepository;
    }

    /**
     * Save a stockItemType.
     *
     * @param stockItemType the entity to save.
     * @return the persisted entity.
     */
    public StockItemType save(StockItemType stockItemType) {
        log.debug("Request to save StockItemType : {}", stockItemType);
        return stockItemTypeRepository.save(stockItemType);
    }

    /**
     * Update a stockItemType.
     *
     * @param stockItemType the entity to save.
     * @return the persisted entity.
     */
    public StockItemType update(StockItemType stockItemType) {
        log.debug("Request to update StockItemType : {}", stockItemType);
        return stockItemTypeRepository.save(stockItemType);
    }

    /**
     * Partially update a stockItemType.
     *
     * @param stockItemType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StockItemType> partialUpdate(StockItemType stockItemType) {
        log.debug("Request to partially update StockItemType : {}", stockItemType);

        return stockItemTypeRepository
            .findById(stockItemType.getId())
            .map(existingStockItemType -> {
                if (stockItemType.getTypeName() != null) {
                    existingStockItemType.setTypeName(stockItemType.getTypeName());
                }

                return existingStockItemType;
            })
            .map(stockItemTypeRepository::save);
    }

    /**
     * Get all the stockItemTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StockItemType> findAll(Pageable pageable) {
        log.debug("Request to get all StockItemTypes");
        return stockItemTypeRepository.findAll(pageable);
    }

    /**
     * Get one stockItemType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StockItemType> findOne(Long id) {
        log.debug("Request to get StockItemType : {}", id);
        return stockItemTypeRepository.findById(id);
    }

    /**
     * Delete the stockItemType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StockItemType : {}", id);
        stockItemTypeRepository.deleteById(id);
    }
}
