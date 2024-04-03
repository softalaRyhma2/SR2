package com.softala.sr2.repository;

import com.softala.sr2.domain.Company;
import com.softala.sr2.domain.Invoice;
import com.softala.sr2.domain.Stock;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Stock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByInvoiceCompany(Company company);

    List<Stock> findByInvoiceIn(List<Invoice> invoices);
}
