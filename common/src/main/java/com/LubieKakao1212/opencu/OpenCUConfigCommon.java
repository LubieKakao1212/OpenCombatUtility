//package com.LubieKakao1212.opencu;
//
//import dev.architectury.injectables.annotations.ExpectPlatform;
//import org.jetbrains.annotations.ApiStatus;
//import org.jetbrains.annotations.NotNull;
//
//public class OpenCUConfigCommon {
//
//    @NotNull
//    @ExpectPlatform
//    public static ModularFrameConfig modularFrame() {
//        return Defaults.MODULAR_FRAME;
//    }
//
//    @NotNull
//    @ExpectPlatform
//    public static RepulsorDeviceConfig repulsorDevice() {
//        return Defaults.REPULSOR_DEVICE;
//    }
//
//    @NotNull
//    @ExpectPlatform
//    public static DispenserDeviceConfig vanillaDispenserDevice() {
//        return Defaults.VANILLA_DISPENSER_DEVICE;
//    }
//    @NotNull
//    @ExpectPlatform
//    public static DispenserDeviceConfig goldenDispenserDevice() {
//        return Defaults.GOLDEN_DISPENSER_DEVICE;
//    }
//    @NotNull
//    @ExpectPlatform
//    public static DispenserDeviceConfig diamondDispenserDevice() {
//        return Defaults.DIAMOND_DISPENSER_DEVICE;
//    }
//    @NotNull
//    @ExpectPlatform
//    public static DispenserDeviceConfig netheriteDispenserDevice() {
//        return Defaults.DIAMOND_DISPENSER_DEVICE;
//    }
//
//    @NotNull
//    @ExpectPlatform
//    public static TrackerDeviceConfig trackerDevice() { return Defaults.TRACKER_DEVICE; }
//
//    public static class Defaults {
//
//        public static final OpenCUConfigCommon.GeneralConfig GENERAL = new GeneralConfig(true);
//
//        public static final OpenCUConfigCommon.CapacitorConfig CAPACITOR = new CapacitorConfig(10000, true, GENERAL);
//        public static final OpenCUConfigCommon.IEnergyToggle ENERGY_TOGGLE_ENABLED = new EnergyToggleConfig(true, GENERAL);
//
//        public static final OpenCUConfigCommon.ModularFrameConfig MODULAR_FRAME = new ModularFrameConfig(CAPACITOR);
//        public static final OpenCUConfigCommon.RepulsorDeviceConfig REPULSOR_DEVICE = new RepulsorDeviceConfig(5.0, 5.0, 25.0, 20000, 1000, CAPACITOR);
//
//        public static final OpenCUConfigCommon.DispenserDeviceConfig VANILLA_DISPENSER_DEVICE = new DispenserDeviceConfig(90, 5.0, 5.0, 1.5, 500, 1.0, ENERGY_TOGGLE_ENABLED);
//        public static final OpenCUConfigCommon.DispenserDeviceConfig GOLDEN_DISPENSER_DEVICE = new DispenserDeviceConfig(180, 10.0, 360.0, 1.5, 200, 1.0, ENERGY_TOGGLE_ENABLED);
//        public static final OpenCUConfigCommon.DispenserDeviceConfig DIAMOND_DISPENSER_DEVICE = new DispenserDeviceConfig(120, 2.5, 360.0, 2.0, 750, 1.5, ENERGY_TOGGLE_ENABLED);
//        public static final OpenCUConfigCommon.DispenserDeviceConfig NETHERITE_DISPENSER_DEVICE = new DispenserDeviceConfig(15, 1.5, 10.0, 3.0, 1500, 3.0, ENERGY_TOGGLE_ENABLED);
//        public static final OpenCUConfigCommon.TrackerDeviceConfig TRACKER_DEVICE = new TrackerDeviceConfig(8.0, 0.75, 5.0);
//
//        public record GeneralConfig(boolean energyEnabled) implements OpenCUConfigCommon.GeneralConfig { }
//
//        public record CapacitorConfig(int energyCapacity, boolean energyEnabled, IEnergyToggle parent) implements OpenCUConfigCommon.CapacitorConfig { }
//
//        public record EnergyToggleConfig(boolean energyEnabled, IEnergyToggle parent) implements IEnergyToggle { }
//
//        public record ModularFrameConfig(OpenCUConfigCommon.CapacitorConfig energy) implements OpenCUConfigCommon.ModularFrameConfig { }
//
//        public record RepulsorDeviceConfig(double maxOffset, double maxRadius, double forceScale, double powerCost, double distanceCost, OpenCUConfigCommon.CapacitorConfig energy) implements OpenCUConfigCommon.RepulsorDeviceConfig { }
//
//        public record DispenserDeviceConfig(double rotationSpeed, double spread, double maxSpread, double force, double baseEnergy, double power, IEnergyToggle energy) implements OpenCUConfigCommon.DispenserDeviceConfig {  }
//
//        public record TrackerDeviceConfig(double range, double energyPerTick, double energyPerActiveConnectionPerTick) implements OpenCUConfigCommon.TrackerDeviceConfig { }
//
//    }
//
//}
