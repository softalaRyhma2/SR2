package com.softala.sr2.service;

import com.softala.sr2.domain.Company;
import com.softala.sr2.domain.User;
import com.softala.sr2.repository.CompanyRepository;
import com.softala.sr2.repository.UserRepository;
import com.softala.sr2.security.SecurityUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.softala.sr2.domain.Company}.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public List<Company> findAllCompaniesByLoggedInUser() {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isPresent()) {
            String login = currentUserLogin.get();
            Optional<User> user = userRepository.findOneByLogin(login);
            if (user.isPresent()) {
                if (isAdmin(user.get()) || isRecser(user.get())) {
                    return companyRepository.findAll();
                } else {
                    Company userCompany = user.get().getCompany();
                    if (userCompany != null) {
                        return Collections.singletonList(userCompany);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private boolean isRecser(User user) {
        return user.getAuthorities().stream().anyMatch(authority -> authority.getName().equals("ROLE_RECSER"));
    }

    private boolean isAdmin(User user) {
        return user.getAuthorities().stream().anyMatch(authority -> authority.getName().equals("ROLE_ADMIN"));
    }

    /**
     * Save a company.
     *
     * @param company the entity to save.
     * @return the persisted entity.
     */
    public Company save(Company company) {
        log.debug("Request to save Company : {}", company);
        return companyRepository.save(company);
    }

    /**
     * Update a company.
     *
     * @param company the entity to save.
     * @return the persisted entity.
     */
    public Company update(Company company) {
        log.debug("Request to update Company : {}", company);
        return companyRepository.save(company);
    }

    /**
     * Partially update a company.
     *
     * @param company the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Company> partialUpdate(Company company) {
        log.debug("Request to partially update Company : {}", company);

        return companyRepository
            .findById(company.getId())
            .map(existingCompany -> {
                if (company.getCompanyName() != null) {
                    existingCompany.setCompanyName(company.getCompanyName());
                }
                if (company.getCompanyEmail() != null) {
                    existingCompany.setCompanyEmail(company.getCompanyEmail());
                }
                if (company.getCompanyDetails() != null) {
                    existingCompany.setCompanyDetails(company.getCompanyDetails());
                }

                return existingCompany;
            })
            .map(companyRepository::save);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Company> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAll(pageable);
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Company> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);

        Optional<Company> companyOptional = companyRepository.findById(id);
        companyOptional.ifPresent(company -> {
            List<User> users = userRepository.findByCompany(company);
            users.forEach(user -> user.setCompany(null));
        });

        companyRepository.deleteById(id);
    }
}
