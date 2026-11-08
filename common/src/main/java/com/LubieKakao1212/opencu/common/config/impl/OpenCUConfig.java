package com.LubieKakao1212.opencu.common.config.impl;

import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.config.api.*;
import com.LubieKakao1212.opencu.common.config.api.base.ICapacitorConfig;
import com.LubieKakao1212.opencu.common.config.api.base.IEnergyToggle;
import com.LubieKakao1212.opencu.common.config.impl.sections.*;
import com.LubieKakao1212.opencu.common.device.RepulsorDevice;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.IgnoreVisibility;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.FileType;
import me.fzzyhmstrs.fzzy_config.api.SaveType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigSection;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@RequiresAction(action = Action.RESTART)
public class OpenCUConfig extends Config implements IOpenCUConfig {

    @Desc("General Mod-Wide values e.g. Enable/Disable energy requirement for all devices")
    public GeneralConfig generalConfig = new GeneralConfig(Defaults.GENERAL);

    public ModularFrameConfig modularFrame = new ModularFrameConfig(Defaults.MODULAR_FRAME);

    public DevicesSection devices = new DevicesSection();

    public OpenCUConfig() {
        super(Objects.requireNonNull(Identifier.of(OpenCUModCommon.MODID, "main")));
    }

    @Override
    public IGeneralConfig general() {
        return generalConfig;
    }

    @Override
    public IModularFrameConfig modularFrame() {
        return modularFrame;
    }

    @Override
    public IRepulsorDeviceConfig repulsorDevice() {
        return devices.repulsor;
    }

    @Override
    public IDispenserDeviceConfig vanillaDispenserDevice() {
        return devices.vanillaDispenser;
    }

    @Override
    public IDispenserDeviceConfig goldenDispenserDevice() {
        return devices.goldenDispenser;
    }

    @Override
    public IDispenserDeviceConfig diamondDispenserDevice() {
        return devices.diamondDispenser;
    }

    @Override
    public IDispenserDeviceConfig netheriteDispenserDevice() {
        return devices.netheriteDispenser;
    }

    @Override
    public ITrackerDeviceConfig trackerDevice() {
        return devices.tracker;
    }

    @Override
    public int defaultPermLevel() {
        return 4;
    }

    @Override
    public @NotNull FileType fileType() {
        return FileType.JSON5;
    }

    @Override
    public @NotNull SaveType saveType() {
        return SaveType.OVERWRITE;
    }

    @RequiresAction(action = Action.RESTART)
    public static class DevicesSection extends ConfigSection {
        @Desc("Config for the pushy-pully thingy called \"Repulsor\"")
        public RepulsorDeviceConfig repulsor = new RepulsorDeviceConfig(Defaults.REPULSOR_DEVICE);

        public DispenserDeviceConfig vanillaDispenser = new DispenserDeviceConfig(Defaults.VANILLA_DISPENSER_DEVICE);
        public DispenserDeviceConfig goldenDispenser = new DispenserDeviceConfig(Defaults.GOLDEN_DISPENSER_DEVICE);
        public DispenserDeviceConfig diamondDispenser = new DispenserDeviceConfig(Defaults.DIAMOND_DISPENSER_DEVICE);
        public DispenserDeviceConfig netheriteDispenser = new DispenserDeviceConfig(Defaults.NETHERITE_DISPENSER_DEVICE);

        @Desc("Config values for the all-seeing Tracker device\ndoes not contain energy capacity config since it is not a standalone device")
        public TrackerDeviceConfig tracker = new TrackerDeviceConfig(Defaults.TRACKER_DEVICE);
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
        public static final ITrackerDeviceConfig TRACKER_DEVICE = new TrackerDeviceConfig(8.0, 0.75, 5.0, 3600);

        public record GeneralConfig(IEnergyToggle energy) implements IGeneralConfig { }

        public record CapacitorConfig(int energyCapacity, boolean energyEnabledInternal, IEnergyToggle parent) implements ICapacitorConfig { }

        public record EnergyToggleConfig(boolean energyEnabledInternal, IEnergyToggle parent) implements IEnergyToggle { }

        public record ModularFrameConfig(ICapacitorConfig energy) implements IModularFrameConfig { }

        public record RepulsorDeviceConfig(double rotationSpeed, double maxOffset, double maxRadius, double forceScale, double powerCost, double distanceCost, ICapacitorConfig energy) implements IRepulsorDeviceConfig { }

        public record DispenserDeviceConfig(double rotationSpeed, double spread, double maxSpread, double force, double baseEnergy, double power, ICapacitorConfig energy) implements IDispenserDeviceConfig {  }

        public record TrackerDeviceConfig(double range, double energyPerTick, double energyPerActiveConnectionPerTick, double rotationSpeed) implements ITrackerDeviceConfig { }

    }
}
