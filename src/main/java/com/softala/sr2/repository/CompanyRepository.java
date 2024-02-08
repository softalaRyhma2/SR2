package com.softala.sr2.repository;

import com.softala.sr2.domain.Company;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByUsers_Id(Long userId);
}
