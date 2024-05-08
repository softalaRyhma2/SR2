package com.softala.sr2.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class StockItemTypeCompanyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static StockItemTypeCompany getStockItemTypeCompanySample1() {
        return new StockItemTypeCompany().id(1L);
    }

    public static StockItemTypeCompany getStockItemTypeCompanySample2() {
        return new StockItemTypeCompany().id(2L);
    }

    public static StockItemTypeCompany getStockItemTypeCompanyRandomSampleGenerator() {
        return new StockItemTypeCompany().id(longCount.incrementAndGet());
    }
}
