package com.softala.sr2.repository;

import com.softala.sr2.domain.Stock;
import com.softala.sr2.domain.StockItem;
import com.softala.sr2.domain.StockItemType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StockItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    List<StockItem> findByStockId(Long id);
    List<StockItem> findByStock(Stock stock);
    // Optional<StockItem> findByStockItemType(StockItemType stockItemType);
}
