package com.softala.sr2.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StockItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static StockItem getStockItemSample1() {
        return new StockItem().id(1L).quantity(1).availability(1).stockItemId(1L);
    }

    public static StockItem getStockItemSample2() {
        return new StockItem().id(2L).quantity(2).availability(2).stockItemId(2L);
    }

    public static StockItem getStockItemRandomSampleGenerator() {
        return new StockItem()
            .id(longCount.incrementAndGet())
            .quantity(intCount.incrementAndGet())
            .availability(intCount.incrementAndGet())
            .stockItemId(longCount.incrementAndGet());
    }
}
