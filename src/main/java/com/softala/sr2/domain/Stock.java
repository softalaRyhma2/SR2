package com.softala.sr2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Stock.
 */
@Entity
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "stock_date", nullable = false)
    private LocalDate stockDate;

    @Column(name = "stock_id")
    private Long stockId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stock")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reservedItems", "stock", "stockItemType" }, allowSetters = true)
    private Set<StockItem> stockItems = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "stocks", "company" }, allowSetters = true)
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Stock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStockDate() {
        return this.stockDate;
    }

    public Stock stockDate(LocalDate stockDate) {
        this.setStockDate(stockDate);
        return this;
    }

    public void setStockDate(LocalDate stockDate) {
        this.stockDate = stockDate;
    }

    public Long getStockId() {
        return this.stockId;
    }

    public Stock stockId(Long stockId) {
        this.setStockId(stockId);
        return this;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public Set<StockItem> getStockItems() {
        return this.stockItems;
    }

    public void setStockItems(Set<StockItem> stockItems) {
        if (this.stockItems != null) {
            this.stockItems.forEach(i -> i.setStock(null));
        }
        if (stockItems != null) {
            stockItems.forEach(i -> i.setStock(this));
        }
        this.stockItems = stockItems;
    }

    public Stock stockItems(Set<StockItem> stockItems) {
        this.setStockItems(stockItems);
        return this;
    }

    public Stock addStockItem(StockItem stockItem) {
        this.stockItems.add(stockItem);
        stockItem.setStock(this);
        return this;
    }

    public Stock removeStockItem(StockItem stockItem) {
        this.stockItems.remove(stockItem);
        stockItem.setStock(null);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Stock invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stock)) {
            return false;
        }
        return getId() != null && getId().equals(((Stock) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", stockDate='" + getStockDate() + "'" +
            ", stockId=" + getStockId() +
            "}";
    }
}
