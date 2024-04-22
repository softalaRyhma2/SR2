package com.softala.sr2.repository;

import com.softala.sr2.domain.Company;
import com.softala.sr2.domain.StockItemType;
import com.softala.sr2.domain.StockItemTypeCompany;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StockItemTypeCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockItemTypeCompanyRepository extends JpaRepository<StockItemTypeCompany, Long> {
    // List<StockItemTypeCompany> findByStockItemTypeCompanyId(Long id);
    List<StockItemTypeCompany> findByStockItemType(StockItemType stockItemType);
    List<StockItemTypeCompany> findByCompany(Company company);
}
