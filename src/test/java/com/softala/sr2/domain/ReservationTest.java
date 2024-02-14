package com.softala.sr2.domain;

import static com.softala.sr2.domain.ReservationTestSamples.*;
import static com.softala.sr2.domain.StockTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.softala.sr2.web.rest.TestUtil;
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
    void stockTest() throws Exception {
        Reservation reservation = getReservationRandomSampleGenerator();
        Stock stockBack = getStockRandomSampleGenerator();

        reservation.setStock(stockBack);
        assertThat(reservation.getStock()).isEqualTo(stockBack);

        reservation.stock(null);
        assertThat(reservation.getStock()).isNull();
    }
}
