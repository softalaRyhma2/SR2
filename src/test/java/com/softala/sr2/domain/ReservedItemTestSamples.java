package com.softala.sr2.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReservedItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ReservedItem getReservedItemSample1() {
        return new ReservedItem().id(1L).quantity(1);
    }

    public static ReservedItem getReservedItemSample2() {
        return new ReservedItem().id(2L).quantity(2);
    }

    public static ReservedItem getReservedItemRandomSampleGenerator() {
        return new ReservedItem().id(longCount.incrementAndGet()).quantity(intCount.incrementAndGet());
    }
}
