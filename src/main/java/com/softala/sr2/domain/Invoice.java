package com.softala.sr2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "total_sum", precision = 21, scale = 2)
    private BigDecimal totalSum;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @NotNull
    @Column(name = "is_closed", nullable = false)
    private Boolean isClosed;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "stockItems", "invoice" }, allowSetters = true)
    private Set<Stock> stocks = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "invoices" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalSum() {
        return this.totalSum;
    }

    public Invoice totalSum(BigDecimal totalSum) {
        this.setTotalSum(totalSum);
        return this;
    }

    public void setTotalSum(BigDecimal totalSum) {
        this.totalSum = totalSum;
    }

    public LocalDate getInvoiceDate() {
        return this.invoiceDate;
    }

    public Invoice invoiceDate(LocalDate invoiceDate) {
        this.setInvoiceDate(invoiceDate);
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Boolean getIsClosed() {
        return this.isClosed;
    }

    public Invoice isClosed(Boolean isClosed) {
        this.setIsClosed(isClosed);
        return this;
    }

    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    public Set<Stock> getStocks() {
        return this.stocks;
    }

    public void setStocks(Set<Stock> stocks) {
        if (this.stocks != null) {
            this.stocks.forEach(i -> i.setInvoice(null));
        }
        if (stocks != null) {
            stocks.forEach(i -> i.setInvoice(this));
        }
        this.stocks = stocks;
    }

    public Invoice stocks(Set<Stock> stocks) {
        this.setStocks(stocks);
        return this;
    }

    public Invoice addStock(Stock stock) {
        this.stocks.add(stock);
        stock.setInvoice(this);
        return this;
    }

    public Invoice removeStock(Stock stock) {
        this.stocks.remove(stock);
        stock.setInvoice(null);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Invoice company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return getId() != null && getId().equals(((Invoice) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", totalSum=" + getTotalSum() +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", isClosed='" + getIsClosed() + "'" +
            "}";
    }
}
