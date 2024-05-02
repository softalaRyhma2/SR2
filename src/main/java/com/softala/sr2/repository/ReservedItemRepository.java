package com.softala.sr2.repository;

import com.softala.sr2.domain.Reservation;
import com.softala.sr2.domain.ReservedItem;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReservedItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservedItemRepository extends JpaRepository<ReservedItem, Long> {
    List<ReservedItem> findByReservationId(Long id);
    List<ReservedItem> findByReservation(Reservation reservation);
}
