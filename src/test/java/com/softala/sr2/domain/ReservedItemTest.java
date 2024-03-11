package com.softala.sr2.domain;

import static com.softala.sr2.domain.ReservationTestSamples.*;
import static com.softala.sr2.domain.ReservedItemTestSamples.*;
import static com.softala.sr2.domain.StockItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.softala.sr2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReservedItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservedItem.class);
        ReservedItem reservedItem1 = getReservedItemSample1();
        ReservedItem reservedItem2 = new ReservedItem();
        assertThat(reservedItem1).isNotEqualTo(reservedItem2);

        reservedItem2.setId(reservedItem1.getId());
        assertThat(reservedItem1).isEqualTo(reservedItem2);

        reservedItem2 = getReservedItemSample2();
        assertThat(reservedItem1).isNotEqualTo(reservedItem2);
    }

    @Test
    void reservationTest() throws Exception {
        ReservedItem reservedItem = getReservedItemRandomSampleGenerator();
        Reservation reservationBack = getReservationRandomSampleGenerator();

        reservedItem.setReservation(reservationBack);
        assertThat(reservedItem.getReservation()).isEqualTo(reservationBack);

        reservedItem.reservation(null);
        assertThat(reservedItem.getReservation()).isNull();
    }

    @Test
    void stockItemTest() throws Exception {
        ReservedItem reservedItem = getReservedItemRandomSampleGenerator();
        StockItem stockItemBack = getStockItemRandomSampleGenerator();

        reservedItem.setStockItem(stockItemBack);
        assertThat(reservedItem.getStockItem()).isEqualTo(stockItemBack);

        reservedItem.stockItem(null);
        assertThat(reservedItem.getStockItem()).isNull();
    }
}
