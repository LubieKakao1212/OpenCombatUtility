package com.LubieKakao1212.opencu.common.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Observer<T> {

    private T value;
    private final Supplier<T> valueSupplier;
    private final BiFunction<T, T, Boolean> changedPredicate;
    private final Consumer<T> callback;
    private boolean forcedDirty;

    public static Observer<Double> numeric(@NotNull Supplier<Double> valueSupplier, double tolerance) {
        return numericDefault(valueSupplier, tolerance, a -> { });
    }
    public static Observer<Double> numericDefault(@NotNull Supplier<Double> valueSupplier, double tolerance, @NotNull Consumer<Double> callback) {
        return new Observer<>(valueSupplier, (a, b) -> Math.abs(a - b) > tolerance, callback);
    }

    public Observer(Supplier<T> valueSupplier, BiFunction<T, T, Boolean> changedPredicate, Consumer<T> listener) {
        this.valueSupplier = valueSupplier;
        this.changedPredicate = changedPredicate;
        this.callback = listener;
        this.value = valueSupplier.get();
    }

    public void update(Consumer<T> listener) {
        var v = valueSupplier.get();

        if(forcedDirty || changedPredicate.apply(value, v)) {
            value = v;
            listener.accept(value);
            forcedDirty = false;
        }
    }

    public void update() {
        update(callback);
    }

    public void forceMarkDirty() {
        forcedDirty = true;
    }
}
