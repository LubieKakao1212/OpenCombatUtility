package com.LubieKakao1212.opencu.common.config.impl;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.config.api.*;
import com.LubieKakao1212.opencu.common.config.api.base.ICapacitorConfig;
import com.LubieKakao1212.opencu.common.config.api.base.IEnergyToggle;
import com.LubieKakao1212.opencu.common.config.impl.sections.GeneralConfig;
import me.fzzyhmstrs.fzzy_config.annotations.IgnoreVisibility;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.FileType;
import me.fzzyhmstrs.fzzy_config.api.SaveType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OpenCUConfig extends Config implements IOpenCUConfig {

    public GeneralConfig generalConfig = new GeneralConfig(Defaults.GENERAL);

    public OpenCUConfig() {
        super(Objects.requireNonNull(Identifier.of(OpenCUModCommon.MODID, "main")));
    }

    @Override
    public IGeneralConfig general() {
        return Defaults.GENERAL;
    }

    @Override
    public IModularFrameConfig modularFrame() {
        return Defaults.MODULAR_FRAME;
    }

    @Override
    public IRepulsorDeviceConfig repulsorDevice() {
        return Defaults.REPULSOR_DEVICE;
    }

    @Override
    public IDispenserDeviceConfig vanillaDispenserDevice() {
        return Defaults.VANILLA_DISPENSER_DEVICE;
    }

    @Override
    public IDispenserDeviceConfig goldenDispenserDevice() {
        return Defaults.GOLDEN_DISPENSER_DEVICE;
    }

    @Override
    public IDispenserDeviceConfig diamondDispenserDevice() {
        return Defaults.DIAMOND_DISPENSER_DEVICE;
    }

    @Override
    public IDispenserDeviceConfig netheriteDispenserDevice() {
        return Defaults.NETHERITE_DISPENSER_DEVICE;
    }

    @Override
    public ITrackerDeviceConfig trackerDevice() {
        return Defaults.TRACKER_DEVICE;
    }

    @Override
    public int defaultPermLevel() {
        return 4;
    }

    @Override
    public @NotNull FileType fileType() {
        return FileType.JSON;
    }

    @Override
    public @NotNull SaveType saveType() {
        return SaveType.SEPARATE;
    }

    public static class Defaults {

        public static final GeneralConfig GENERAL = new GeneralConfig(new MasterEnergyToggle());

        public static final ICapacitorConfig CAPACITOR = new CapacitorConfig(10000, true, GENERAL.energy());
        public static final IEnergyToggle ENERGY_TOGGLE_ENABLED = new EnergyToggleConfig(true, GENERAL.energy());

        public static final IModularFrameConfig MODULAR_FRAME = new ModularFrameConfig(CAPACITOR);
        public static final IRepulsorDeviceConfig REPULSOR_DEVICE = new RepulsorDeviceConfig(90, 5.0, 5.0, 25.0, 20000, 1000, CAPACITOR);

        public static final IDispenserDeviceConfig VANILLA_DISPENSER_DEVICE = new DispenserDeviceConfig(90, 5.0, 5.0, 1.5, 500, 1.0, CAPACITOR);
        public static final IDispenserDeviceConfig GOLDEN_DISPENSER_DEVICE = new DispenserDeviceConfig(180, 10.0, 360.0, 1.5, 200, 1.0, CAPACITOR);
        public static final IDispenserDeviceConfig DIAMOND_DISPENSER_DEVICE = new DispenserDeviceConfig(120, 2.5, 360.0, 2.0, 750, 1.5, CAPACITOR);
        public static final IDispenserDeviceConfig NETHERITE_DISPENSER_DEVICE = new DispenserDeviceConfig(15, 1.5, 10.0, 3.0, 1500, 3.0, CAPACITOR);
        public static final ITrackerDeviceConfig TRACKER_DEVICE = new TrackerDeviceConfig(8.0, 0.75, 5.0);

        public record GeneralConfig(IEnergyToggle energy) implements IGeneralConfig { }

        public record CapacitorConfig(int energyCapacity, boolean energyEnabledInternal, IEnergyToggle parent) implements ICapacitorConfig { }

        public record EnergyToggleConfig(boolean energyEnabledInternal, IEnergyToggle parent) implements IEnergyToggle { }

        public record ModularFrameConfig(ICapacitorConfig energy) implements IModularFrameConfig { }

        public record RepulsorDeviceConfig(double rotationSpeed, double maxOffset, double maxRadius, double forceScale, double powerCost, double distanceCost, ICapacitorConfig energy) implements IRepulsorDeviceConfig { }

        public record DispenserDeviceConfig(double rotationSpeed, double spread, double maxSpread, double force, double baseEnergy, double power, ICapacitorConfig energy) implements IDispenserDeviceConfig {  }

        public record TrackerDeviceConfig(double range, double energyPerTick, double energyPerActiveConnectionPerTick) implements ITrackerDeviceConfig { }

    }
}
