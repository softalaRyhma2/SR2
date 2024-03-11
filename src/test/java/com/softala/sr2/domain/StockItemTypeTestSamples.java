package com.softala.sr2.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StockItemTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StockItemType getStockItemTypeSample1() {
        return new StockItemType().id(1L).name("name1").stockItemTypeId(1L);
    }

    public static StockItemType getStockItemTypeSample2() {
        return new StockItemType().id(2L).name("name2").stockItemTypeId(2L);
    }

    public static StockItemType getStockItemTypeRandomSampleGenerator() {
        return new StockItemType()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .stockItemTypeId(longCount.incrementAndGet());
    }
}
