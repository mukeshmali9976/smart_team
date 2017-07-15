package com.techmali.smartteam.network;

import java.util.concurrent.atomic.AtomicInteger;


public class UniqueNumberUtils {

    private static UniqueNumberUtils INSTANCE = new UniqueNumberUtils();

    private AtomicInteger seq;

    private UniqueNumberUtils() {
        seq = new AtomicInteger(0);
    }

    public int getUniqueId() {
        return seq.incrementAndGet();
    }

    public static UniqueNumberUtils getInstance() {
        return INSTANCE;
    }
}