package com.LubieKakao1212.opencu.capability;

import net.minecraftforge.energy.IEnergyStorage;

public class InfiniteEnergyStorage implements IInternalEnergyStorage {

    public static final InfiniteEnergyStorage SOURCE = new InfiniteEnergyStorage(true, false);
    public static final InfiniteEnergyStorage SINK = new InfiniteEnergyStorage(false, true);
    public static final InfiniteEnergyStorage DUAL = new InfiniteEnergyStorage(true, true);

    private final boolean canExtract;
    private final boolean canReceive;

    public InfiniteEnergyStorage(boolean canExtract, boolean canReceive) {
        this.canExtract = canExtract;
        this.canReceive = canReceive;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) { return maxReceive; }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return maxExtract;
    }

    @Override
    public int getEnergyStored() { return Integer.MAX_VALUE; }

    @Override
    public int getMaxEnergyStored() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canExtract() { return canExtract; }

    @Override
    public boolean canReceive() { return canReceive; }

    @Override
    public int extractEnergyInternal(int maxExtract, boolean simulate) {
        return maxExtract;
    }

    @Override
    public int receiveEnergyInternal(int maxReceive, boolean simulate) {
        return maxReceive;
    }

    @Override
    public void setEnergyInternal(int energy) {}
}
