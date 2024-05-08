package com.softala.sr2.domain;

import static com.softala.sr2.domain.StockItemTestSamples.*;
import static com.softala.sr2.domain.StockItemTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.softala.sr2.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StockItemTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockItemType.class);
        StockItemType stockItemType1 = getStockItemTypeSample1();
        StockItemType stockItemType2 = new StockItemType();
        assertThat(stockItemType1).isNotEqualTo(stockItemType2);

        stockItemType2.setId(stockItemType1.getId());
        assertThat(stockItemType1).isEqualTo(stockItemType2);

        stockItemType2 = getStockItemTypeSample2();
        assertThat(stockItemType1).isNotEqualTo(stockItemType2);
    }
    /*@Test
    void stockItemTest() throws Exception {
        StockItemType stockItemType = getStockItemTypeRandomSampleGenerator();
        StockItem stockItemBack = getStockItemRandomSampleGenerator();

        stockItemType.addStockItem(stockItemBack);
        assertThat(stockItemType.getStockItems()).containsOnly(stockItemBack);
        assertThat(stockItemBack.getStockItemType()).isEqualTo(stockItemType);

        stockItemType.removeStockItem(stockItemBack);
        assertThat(stockItemType.getStockItems()).doesNotContain(stockItemBack);
        assertThat(stockItemBack.getStockItemType()).isNull();

        stockItemType.stockItems(new HashSet<>(Set.of(stockItemBack)));
        assertThat(stockItemType.getStockItems()).containsOnly(stockItemBack);
        assertThat(stockItemBack.getStockItemType()).isEqualTo(stockItemType);

        stockItemType.setStockItems(new HashSet<>());
        assertThat(stockItemType.getStockItems()).doesNotContain(stockItemBack);
        assertThat(stockItemBack.getStockItemType()).isNull();
    }*/
}
