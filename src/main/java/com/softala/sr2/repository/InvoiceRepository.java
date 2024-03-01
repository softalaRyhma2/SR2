package com.softala.sr2.repository;

import com.softala.sr2.domain.Company;
import com.softala.sr2.domain.Invoice;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Invoice entity.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query("SELECT i FROM Invoice i WHERE i.company.id = (SELECT u.company.id FROM User u WHERE u.login = :login)")
    Page<Invoice> findAllByCompanyId(@Param("login") String login, Pageable pageable);

    List<Invoice> findByCompany(Company company);
}
