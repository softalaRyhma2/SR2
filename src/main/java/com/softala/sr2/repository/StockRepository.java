package com.softala.sr2.repository;

import com.softala.sr2.domain.Stock;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import com.softala.sr2.domain.Invoice;
import java.util.List;
import org.springframework.data.repository.query.Param;



/**
 * Spring Data JPA repository for the Stock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

     // Method to find all stocks linked to the current user's company's invoices
     @Query("SELECT s FROM Stock s WHERE s.invoice IN (SELECT i FROM Invoice i WHERE i.company.id = (SELECT u.company.id FROM User u WHERE u.login = :login))")
     List<Stock> findAllByLoggedInUserCompany(@Param("login") String login);

     List<Stock> findAllByInvoiceIn(List<Invoice> invoices);
    
     
    
     List<Stock> findAllByInvoice(Invoice invoice);

}
