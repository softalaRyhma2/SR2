package com.softala.sr2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "company_name", length = 50, nullable = false)
    private String companyName;

    @NotNull
    @Size(max = 60)
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+.[^@\\s]+$")
    @Column(name = "company_email", length = 60, nullable = false)
    private String companyEmail;

    @Size(max = 500)
    @Column(name = "company_details", length = 500)
    private String companyDetails;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "stocks", "company" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Company companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return this.companyEmail;
    }

    public Company companyEmail(String companyEmail) {
        this.setCompanyEmail(companyEmail);
        return this;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyDetails() {
        return this.companyDetails;
    }

    public Company companyDetails(String companyDetails) {
        this.setCompanyDetails(companyDetails);
        return this;
    }

    public void setCompanyDetails(String companyDetails) {
        this.companyDetails = companyDetails;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.setCompany(null));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.setCompany(this));
        }
        this.invoices = invoices;
    }

    public Company invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Company addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.setCompany(this);
        return this;
    }

    public Company removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.setCompany(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return getId() != null && getId().equals(((Company) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", companyEmail='" + getCompanyEmail() + "'" +
            ", companyDetails='" + getCompanyDetails() + "'" +
            "}";
    }
}
