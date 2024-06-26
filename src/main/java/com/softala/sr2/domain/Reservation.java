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
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @NotNull
    @Column(name = "is_picked_up", nullable = false)
    private Boolean isPickedUp;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "reservation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reservation", "stockItem" }, allowSetters = true)
    private Set<ReservedItem> reservedItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reservation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReservationDate() {
        return this.reservationDate;
    }

    public Reservation reservationDate(LocalDate reservationDate) {
        this.setReservationDate(reservationDate);
        return this;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Boolean getIsPickedUp() {
        return this.isPickedUp;
    }

    public Reservation isPickedUp(Boolean isPickedUp) {
        this.setIsPickedUp(isPickedUp);
        return this;
    }

    public void setIsPickedUp(Boolean isPickedUp) {
        this.isPickedUp = isPickedUp;
    }

    public Set<ReservedItem> getReservedItems() {
        return this.reservedItems;
    }

    public void setReservedItems(Set<ReservedItem> reservedItems) {
        if (this.reservedItems != null) {
            this.reservedItems.forEach(i -> i.setReservation(null));
        }
        if (reservedItems != null) {
            reservedItems.forEach(i -> i.setReservation(this));
        }
        this.reservedItems = reservedItems;
    }

    public Reservation reservedItems(Set<ReservedItem> reservedItems) {
        this.setReservedItems(reservedItems);
        return this;
    }

    public Reservation addReservedItem(ReservedItem reservedItem) {
        this.reservedItems.add(reservedItem);
        reservedItem.setReservation(this);
        return this;
    }

    public Reservation removeReservedItem(ReservedItem reservedItem) {
        this.reservedItems.remove(reservedItem);
        reservedItem.setReservation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        return getId() != null && getId().equals(((Reservation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
            ", reservationDate='" + getReservationDate() + "'" +
            ", isPickedUp='" + getIsPickedUp() + "'" +
            "}";
    }
}
