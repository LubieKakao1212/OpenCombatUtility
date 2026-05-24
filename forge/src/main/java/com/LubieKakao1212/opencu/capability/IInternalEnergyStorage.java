package com.LubieKakao1212.opencu.capability;

import net.minecraftforge.energy.IEnergyStorage;

public interface IInternalEnergyStorage extends IEnergyStorage {

    int extractEnergyInternal(int maxExtract, boolean simulate);

    int receiveEnergyInternal(int maxReceive, boolean simulate);

    void setEnergyInternal(int energy);

}
