package com.softala.sr2.service;

import com.softala.sr2.domain.StockItemTypeCompany;
import com.softala.sr2.repository.StockItemTypeCompanyRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.softala.sr2.domain.StockItemTypeCompany}.
 */
@Service
@Transactional
public class StockItemTypeCompanyService {

    private final Logger log = LoggerFactory.getLogger(StockItemTypeCompanyService.class);

    private final StockItemTypeCompanyRepository stockItemTypeCompanyRepository;

    public StockItemTypeCompanyService(StockItemTypeCompanyRepository stockItemTypeCompanyRepository) {
        this.stockItemTypeCompanyRepository = stockItemTypeCompanyRepository;
    }

    /**
     * Save a stockItemTypeCompany.
     *
     * @param stockItemTypeCompany the entity to save.
     * @return the persisted entity.
     */
    public StockItemTypeCompany save(StockItemTypeCompany stockItemTypeCompany) {
        log.debug("Request to save StockItemTypeCompany : {}", stockItemTypeCompany);
        return stockItemTypeCompanyRepository.save(stockItemTypeCompany);
    }

    /**
     * Update a stockItemTypeCompany.
     *
     * @param stockItemTypeCompany the entity to save.
     * @return the persisted entity.
     */
    public StockItemTypeCompany update(StockItemTypeCompany stockItemTypeCompany) {
        log.debug("Request to update StockItemTypeCompany : {}", stockItemTypeCompany);
        return stockItemTypeCompanyRepository.save(stockItemTypeCompany);
    }

    /**
     * Partially update a stockItemTypeCompany.
     *
     * @param stockItemTypeCompany the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StockItemTypeCompany> partialUpdate(StockItemTypeCompany stockItemTypeCompany) {
        log.debug("Request to partially update StockItemTypeCompany : {}", stockItemTypeCompany);

        return stockItemTypeCompanyRepository
            .findById(stockItemTypeCompany.getId())
            .map(existingStockItemTypeCompany -> {
                if (stockItemTypeCompany.getTypePrice() != null) {
                    existingStockItemTypeCompany.setTypePrice(stockItemTypeCompany.getTypePrice());
                }

                return existingStockItemTypeCompany;
            })
            .map(stockItemTypeCompanyRepository::save);
    }

    /**
     * Get all the stockItemTypeCompanies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StockItemTypeCompany> findAll(Pageable pageable) {
        log.debug("Request to get all StockItemTypeCompanies");
        return stockItemTypeCompanyRepository.findAll(pageable);
    }

    /**
     * Get one stockItemTypeCompany by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StockItemTypeCompany> findOne(Long id) {
        log.debug("Request to get StockItemTypeCompany : {}", id);
        return stockItemTypeCompanyRepository.findById(id);
    }

    /**
     * Delete the stockItemTypeCompany by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StockItemTypeCompany : {}", id);
        stockItemTypeCompanyRepository.deleteById(id);
    }
}
