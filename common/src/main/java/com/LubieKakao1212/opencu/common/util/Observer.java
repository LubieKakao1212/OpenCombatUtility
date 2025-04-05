package com.LubieKakao1212.opencu.common.util;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Observer<T> {

    private T value;
    private Supplier<T> valueSupplier;
    private BiFunction<T, T, Boolean> changedPredicate;
    private Consumer<T> listener;
    private boolean forcedDirty;

    public Observer(Supplier<T> valueSupplier, BiFunction<T, T, Boolean> changedPredicate, Consumer<T> listener) {
        this.valueSupplier = valueSupplier;
        this.changedPredicate = changedPredicate;
        this.listener = listener;
        this.value = valueSupplier.get();
    }

    public void update() {
        var v = valueSupplier.get();

        if(forcedDirty || changedPredicate.apply(value, v)) {
            value = v;
            listener.accept(value);
            forcedDirty = false;
        }
    }

    public void forceMarkDirty() {
        forcedDirty = true;
    }


}
