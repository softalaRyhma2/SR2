package com.softala.sr2.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReservedItem.
 */
@Entity
@Table(name = "reserved_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReservedItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "reservedItems" }, allowSetters = true)
    private Reservation reservation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "reservedItems", "stock", "stockItemType" }, allowSetters = true)
    private StockItem stockItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReservedItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public ReservedItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Reservation getReservation() {
        return this.reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public ReservedItem reservation(Reservation reservation) {
        this.setReservation(reservation);
        return this;
    }

    public StockItem getStockItem() {
        return this.stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public ReservedItem stockItem(StockItem stockItem) {
        this.setStockItem(stockItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReservedItem)) {
            return false;
        }
        return getId() != null && getId().equals(((ReservedItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReservedItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
