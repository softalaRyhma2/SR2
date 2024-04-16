package com.softala.sr2.repository;

import com.softala.sr2.domain.StockItemTypeCompany;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StockItemTypeCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockItemTypeCompanyRepository extends JpaRepository<StockItemTypeCompany, Long> {}
