package com.softala.sr2.service;

import com.softala.sr2.domain.Reservation;
import com.softala.sr2.domain.ReservedItem;
import com.softala.sr2.domain.StockItem;
import com.softala.sr2.repository.ReservationRepository;
import com.softala.sr2.repository.ReservedItemRepository;
import com.softala.sr2.repository.StockItemRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.softala.sr2.domain.Reservation}.
 */
@Service
@Transactional
public class ReservationService {

    private final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;

    private final ReservedItemRepository reservedItemRepository;
    private final StockItemRepository stockItemRepository;

    public ReservationService(
        ReservationRepository reservationRepository,
        ReservedItemRepository reservedItemRepository,
        StockItemRepository stockItemRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservedItemRepository = reservedItemRepository;
        this.stockItemRepository = stockItemRepository;
    }

    /**
     * Save a reservation.
     *
     * @param reservation the entity to save.
     * @return the persisted entity.
     */
    public Reservation save(Reservation reservation) {
        log.debug("Request to save Reservation : {}", reservation);
        return reservationRepository.save(reservation);
    }

    /**
     * Update a reservation.
     *
     * @param reservation the entity to save.
     * @return the persisted entity.
     */
    public Reservation update(Reservation reservation) {
        log.debug("Request to update Reservation : {}", reservation);
        Reservation updatedReservation = reservationRepository.save(reservation);

        if (updatedReservation.getIsPickedUp()) {
            List<ReservedItem> reservedItems = reservedItemRepository.findByReservation(updatedReservation);
            for (ReservedItem reservedItem : reservedItems) {
                StockItem stockItem = reservedItem.getStockItem();
                int reservedQuantity = reservedItem.getQuantity();
                int currentQuantity = stockItem.getQuantity();

                int newQuantity = currentQuantity - reservedQuantity;
                stockItem.setQuantity(newQuantity);
                stockItem.setAvailable(newQuantity);

                stockItemRepository.save(stockItem);
            }
        }
        return updatedReservation;
    }

    /**
     * Partially update a reservation.
     *
     * @param reservation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Reservation> partialUpdate(Reservation reservation) {
        log.debug("Request to partially update Reservation : {}", reservation);

        return reservationRepository
            .findById(reservation.getId())
            .map(existingReservation -> {
                if (reservation.getReservationDate() != null) {
                    existingReservation.setReservationDate(reservation.getReservationDate());
                }
                if (reservation.getIsPickedUp() != null) {
                    existingReservation.setIsPickedUp(reservation.getIsPickedUp());
                }

                return existingReservation;
            })
            .map(reservationRepository::save);
    }

    /**
     * Get all the reservations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Reservation> findAll(Pageable pageable) {
        log.debug("Request to get all Reservations");
        return reservationRepository.findAll(pageable);
    }

    /**
     * Get one reservation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Reservation> findOne(Long id) {
        log.debug("Request to get Reservation : {}", id);
        return reservationRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ReservedItem> getReservedItemsByReservationId(Reservation reservation) {
        log.debug("Request to get ReservedItems for one Reservation");
        return reservedItemRepository.findByReservation(reservation);
    }

    /**
     * Delete the reservation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reservation : {}", id);
        reservationRepository.deleteById(id);
    }
}
