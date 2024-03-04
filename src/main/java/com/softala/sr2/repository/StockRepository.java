package com.softala.sr2.repository;

import com.softala.sr2.domain.Company;
import com.softala.sr2.domain.Invoice;
import com.softala.sr2.domain.Stock;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Stock entity.
 */
@SuppressWarnings("unused")
public interface StockRepository extends JpaRepository<Stock, Long> {
    //  @Query("SELECT i FROM Invoice i WHERE i.company.id = (SELECT u.company.id FROM User u WHERE u.login = :login)")

    @Query("SELECT s FROM Stock s WHERE s.invoice.company.id = (SELECT u.company.id FROM User u WHERE u.login = :login)")
    List<Stock> findByCurrentUserCompany(@Param("login") String login);

    @Query("SELECT s FROM Stock s WHERE s.invoice IN :invoices")
    List<Stock> findByInvoice(@Param("invoices") List<Invoice> invoices);

    @Query("SELECT s FROM Stock s WHERE s.invoice IN :invoices")
    List<Stock> findByInvoices(@Param("invoices") List<Invoice> invoices);
}
