package com.LubieKakao1212.opencu.common.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SyncDouble {

    private final Supplier<Double> value;
    private double anchor;
    private final double sensitivity;

    public SyncDouble(double anchor, double sensitivity, Supplier<Double> value) {
        this.anchor = anchor;
        this.sensitivity = sensitivity;
        this.value = value;
    }

    public void sync(Consumer<Double> callback) {
        var value = this.value.get();
        if(Math.abs(value - anchor) > sensitivity) {
            callback.accept(value);
            anchor = value;
        }
    }

}
