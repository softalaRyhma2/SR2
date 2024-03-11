package com.softala.sr2.domain;

import static com.softala.sr2.domain.ReservedItemTestSamples.*;
import static com.softala.sr2.domain.StockItemTestSamples.*;
import static com.softala.sr2.domain.StockItemTypeTestSamples.*;
import static com.softala.sr2.domain.StockTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.softala.sr2.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StockItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockItem.class);
        StockItem stockItem1 = getStockItemSample1();
        StockItem stockItem2 = new StockItem();
        assertThat(stockItem1).isNotEqualTo(stockItem2);

        stockItem2.setId(stockItem1.getId());
        assertThat(stockItem1).isEqualTo(stockItem2);

        stockItem2 = getStockItemSample2();
        assertThat(stockItem1).isNotEqualTo(stockItem2);
    }

    @Test
    void reservedItemTest() throws Exception {
        StockItem stockItem = getStockItemRandomSampleGenerator();
        ReservedItem reservedItemBack = getReservedItemRandomSampleGenerator();

        stockItem.addReservedItem(reservedItemBack);
        assertThat(stockItem.getReservedItems()).containsOnly(reservedItemBack);
        assertThat(reservedItemBack.getStockItem()).isEqualTo(stockItem);

        stockItem.removeReservedItem(reservedItemBack);
        assertThat(stockItem.getReservedItems()).doesNotContain(reservedItemBack);
        assertThat(reservedItemBack.getStockItem()).isNull();

        stockItem.reservedItems(new HashSet<>(Set.of(reservedItemBack)));
        assertThat(stockItem.getReservedItems()).containsOnly(reservedItemBack);
        assertThat(reservedItemBack.getStockItem()).isEqualTo(stockItem);

        stockItem.setReservedItems(new HashSet<>());
        assertThat(stockItem.getReservedItems()).doesNotContain(reservedItemBack);
        assertThat(reservedItemBack.getStockItem()).isNull();
    }

    @Test
    void stockTest() throws Exception {
        StockItem stockItem = getStockItemRandomSampleGenerator();
        Stock stockBack = getStockRandomSampleGenerator();

        stockItem.setStock(stockBack);
        assertThat(stockItem.getStock()).isEqualTo(stockBack);

        stockItem.stock(null);
        assertThat(stockItem.getStock()).isNull();
    }

    @Test
    void stockItemTypeTest() throws Exception {
        StockItem stockItem = getStockItemRandomSampleGenerator();
        StockItemType stockItemTypeBack = getStockItemTypeRandomSampleGenerator();

        stockItem.setStockItemType(stockItemTypeBack);
        assertThat(stockItem.getStockItemType()).isEqualTo(stockItemTypeBack);

        stockItem.stockItemType(null);
        assertThat(stockItem.getStockItemType()).isNull();
    }
}
