package com.softala.sr2.domain;

import static com.softala.sr2.domain.ReservationTestSamples.*;
import static com.softala.sr2.domain.ReservedItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.softala.sr2.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReservationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservation.class);
        Reservation reservation1 = getReservationSample1();
        Reservation reservation2 = new Reservation();
        assertThat(reservation1).isNotEqualTo(reservation2);

        reservation2.setId(reservation1.getId());
        assertThat(reservation1).isEqualTo(reservation2);

        reservation2 = getReservationSample2();
        assertThat(reservation1).isNotEqualTo(reservation2);
    }

    @Test
    void reservedItemTest() throws Exception {
        Reservation reservation = getReservationRandomSampleGenerator();
        ReservedItem reservedItemBack = getReservedItemRandomSampleGenerator();

        reservation.addReservedItem(reservedItemBack);
        assertThat(reservation.getReservedItems()).containsOnly(reservedItemBack);
        assertThat(reservedItemBack.getReservation()).isEqualTo(reservation);

        reservation.removeReservedItem(reservedItemBack);
        assertThat(reservation.getReservedItems()).doesNotContain(reservedItemBack);
        assertThat(reservedItemBack.getReservation()).isNull();

        reservation.reservedItems(new HashSet<>(Set.of(reservedItemBack)));
        assertThat(reservation.getReservedItems()).containsOnly(reservedItemBack);
        assertThat(reservedItemBack.getReservation()).isEqualTo(reservation);

        reservation.setReservedItems(new HashSet<>());
        assertThat(reservation.getReservedItems()).doesNotContain(reservedItemBack);
        assertThat(reservedItemBack.getReservation()).isNull();
    }
}
