package com.softala.sr2.service;

import com.softala.sr2.domain.Reservation;
import com.softala.sr2.domain.User;
import com.softala.sr2.repository.ReservationRepository;
import com.softala.sr2.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.softala.testiapp.domain.Reservation}.
 */
@Service
@Transactional
public class ReservationService {

    private final Logger log = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
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
        return reservationRepository.save(reservation);
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
                if (reservation.getReservedQuantity() != null) {
                    existingReservation.setReservedQuantity(reservation.getReservedQuantity());
                }
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

    /**
     * Delete the reservation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reservation : {}", id);
        reservationRepository.deleteById(id);
    }

    /**
     * Assign a reservation to a user.
     *
     * @param reservationId the ID of the reservation.
     * @param userId        the ID of the user.
     * @return the updated reservation entity.
     */
    public Reservation assignReservationToUser(Long reservationId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found with id " + userId));
        return reservationRepository
            .findById(reservationId)
            .map(reservation -> {
                reservation.setUser(user); // Assuming Reservation class has setUser method.
                return reservationRepository.save(reservation);
            })
            .orElseThrow(() -> new IllegalStateException("Reservation not found with id " + reservationId));
    }

    /**
     * Get all reservations for a specific user.
     *
     * @param userId the ID of the user.
     * @return a list of reservations.
     */
    public List<Reservation> findAllReservationsForUser(Long userId) {
        return reservationRepository.findByUser_Id(userId);
    }
}
