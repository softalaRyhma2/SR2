package com.softala.sr2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StockItem.
 */
@Entity
@Table(name = "stock_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StockItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "available", nullable = false)
    private Integer available;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "stockItem")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reservation", "stockItem" }, allowSetters = true)
    private Set<ReservedItem> reservedItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "stockItems", "invoice" }, allowSetters = true)
    private Stock stock;

    /*
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "stockItems" }, allowSetters = true)
    private StockItemType stockItemType;
*/
    @ManyToOne(fetch = FetchType.EAGER)
    private StockItemTypeCompany stockItemTypeCompany;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public StockItemTypeCompany getStockItemTypeCompany() {
        return stockItemTypeCompany;
    }

    public void setStockItemTypeCompany(StockItemTypeCompany stockItemTypeCompany) {
        this.stockItemTypeCompany = stockItemTypeCompany;
    }

    public Long getId() {
        return this.id;
    }

    public StockItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public StockItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getAvailable() {
        return this.available;
    }

    public StockItem available(Integer available) {
        this.setAvailable(available);
        return this;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public StockItem price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<ReservedItem> getReservedItems() {
        return this.reservedItems;
    }

    public void setReservedItems(Set<ReservedItem> reservedItems) {
        if (this.reservedItems != null) {
            this.reservedItems.forEach(i -> i.setStockItem(null));
        }
        if (reservedItems != null) {
            reservedItems.forEach(i -> i.setStockItem(this));
        }
        this.reservedItems = reservedItems;
    }

    public StockItem reservedItems(Set<ReservedItem> reservedItems) {
        this.setReservedItems(reservedItems);
        return this;
    }

    public StockItem addReservedItem(ReservedItem reservedItem) {
        this.reservedItems.add(reservedItem);
        reservedItem.setStockItem(this);
        return this;
    }

    public StockItem removeReservedItem(ReservedItem reservedItem) {
        this.reservedItems.remove(reservedItem);
        reservedItem.setStockItem(null);
        return this;
    }

    public Stock getStock() {
        return this.stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public StockItem stock(Stock stock) {
        this.setStock(stock);
        return this;
    }

    /*    public StockItemType getStockItemType() {
        return this.stockItemType;
    }

    public void setStockItemType(StockItemType stockItemType) {
        this.stockItemType = stockItemType;
    }

    public StockItem stockItemType(StockItemType stockItemType) {
        this.setStockItemType(stockItemType);
        return this;
    }
 */
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockItem)) {
            return false;
        }
        return getId() != null && getId().equals(((StockItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StockItem{" +
                "id=" + getId() +
                ", quantity=" + getQuantity() +
                ", available=" + getAvailable() +
                ", price=" + getPrice() +
                "}";
    }
}
