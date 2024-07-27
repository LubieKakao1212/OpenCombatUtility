package com.LubieKakao1212.opencu.common.transaction;

public record DeviceActivationContext(IScopedContext ctx, IEnergyContext energy, IAmmoContext ammo, ILeftoverItemContext leftover) implements AutoCloseable {
    @Override
    public void close() {
        ctx.close();
        leftover.close();
    }
}