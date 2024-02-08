package com.softala.sr2.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "reserved_quantity")
    private Integer reservedQuantity;

    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    @Column(name = "is_picked_up")
    private Boolean isPickedUp;

    @ManyToOne(fetch = FetchType.LAZY)
    private Stock stock;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getReservedQuantity() {
        return this.reservedQuantity;
    }

    public Reservation reservedQuantity(Integer reservedQuantity) {
        this.setReservedQuantity(reservedQuantity);
        return this;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
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

    public Stock getStock() {
        return this.stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public Reservation stock(Stock stock) {
        this.setStock(stock);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and
    // setters here

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
        // see
        // https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + getId() +
                ", reservedQuantity=" + getReservedQuantity() +
                ", reservationDate='" + getReservationDate() + "'" +
                ", isPickedUp='" + getIsPickedUp() + "'" +
                "}";
    }
}
