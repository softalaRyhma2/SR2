package com.softala.sr2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StockItemTypeCompany.
 */
@Entity
@Table(name = "stock_item_type_company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockItemTypeCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "type_price", precision = 21, scale = 2, nullable = false)
    private BigDecimal typePrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stock_item_type_id")
    @JsonIgnoreProperties(value = { "stockItems" }, allowSetters = true)
    private StockItemType stockItemType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties(value = { "invoices" }, allowSetters = true)
    private Company company;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StockItemTypeCompany id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTypePrice() {
        return this.typePrice;
    }

    public StockItemTypeCompany typePrice(BigDecimal typePrice) {
        this.setTypePrice(typePrice);
        return this;
    }

    public void setTypePrice(BigDecimal typePrice) {
        this.typePrice = typePrice;
    }

    public StockItemType getStockItemType() {
        return this.stockItemType;
    }

    public void setStockItemType(StockItemType stockItemType) {
        this.stockItemType = stockItemType;
    }

    public StockItemTypeCompany stockItemType(StockItemType stockItemType) {
        this.setStockItemType(stockItemType);
        return this;
    }

    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public StockItemTypeCompany company(Company company) {
        this.setCompany(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockItemTypeCompany)) {
            return false;
        }
        return getId() != null && getId().equals(((StockItemTypeCompany) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockItemTypeCompany [id=" + id + ", typePrice=" + typePrice + ", stockItemType=" + stockItemType
                + ", company=" + company + "]";
    }
    /*    @Override
    public String toString() {
        return "StockItemTypeCompany{" +
            "id=" + getId() +
            ", typePrice=" + getTypePrice() +
            "}";
     } */
}
