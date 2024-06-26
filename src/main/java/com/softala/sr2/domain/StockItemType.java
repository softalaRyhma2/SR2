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
 * A StockItemType.
 */
@Entity
@Table(name = "stock_item_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockItemType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "type_name", nullable = false)
    private String typeName;

    /*
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "stockItemType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reservedItems", "stock", "stockItemType" }, allowSetters = true)
    private Set<StockItem> stockItems = new HashSet<>();
 */
    @OneToMany(mappedBy = "stockItemType")
    //@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "stockItemType", "stockItem" }, allowSetters = true)
    private Set<StockItemTypeCompany> stockItemTypeCompanies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Set<StockItemTypeCompany> getStockItemTypeCompanies() {
        return stockItemTypeCompanies;
    }

    public void setStockItemTypeCompanies(Set<StockItemTypeCompany> stockItemTypeCompanies) {
        this.stockItemTypeCompanies = stockItemTypeCompanies;
    }

    public Long getId() {
        return this.id;
    }

    public StockItemType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public StockItemType typeName(String typeName) {
        this.setTypeName(typeName);
        return this;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /*
    public Set<StockItem> getStockItems() {
        return this.stockItems;
    }

    public void setStockItems(Set<StockItem> stockItems) {
        if (this.stockItems != null) {
            this.stockItems.forEach(i -> i.setStockItemType(null));
        }
        if (stockItems != null) {
            stockItems.forEach(i -> i.setStockItemType(this));
        }
        this.stockItems = stockItems;
    }

    public StockItemType stockItems(Set<StockItem> stockItems) {
        this.setStockItems(stockItems);
        return this;
    }

    public StockItemType addStockItem(StockItem stockItem) {
        this.stockItems.add(stockItem);
        stockItem.setStockItemType(this);
        return this;
    }

    public StockItemType removeStockItem(StockItem stockItem) {
        this.stockItems.remove(stockItem);
        stockItem.setStockItemType(null);
        return this;
    }
 */
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockItemType)) {
            return false;
        }
        return getId() != null && getId().equals(((StockItemType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockItemType{" +
            "id=" + getId() +
            ", typeName='" + getTypeName() + "'" +
            "}";
    }
}
