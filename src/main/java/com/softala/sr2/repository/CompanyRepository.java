package com.softala.sr2.repository;

import com.softala.sr2.domain.Company;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Company entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT c FROM Company c WHERE c.id = (SELECT u.company.id FROM User u WHERE u.login = :login)")
    List<Company> findAllByLoggedInUser(@Param("login") String login);
}
