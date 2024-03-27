package com.softala.sr2.repository;

import com.softala.sr2.domain.StockItemType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StockItemType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockItemTypeRepository extends JpaRepository<StockItemType, Long> {}
