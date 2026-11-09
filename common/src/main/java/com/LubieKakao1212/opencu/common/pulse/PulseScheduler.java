package com.LubieKakao1212.opencu.common.pulse;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PulseScheduler {

    private static final Map<World, List<Entry>> scheduledPulses = new HashMap<>();

    public static void onEndOfTick(ServerWorld world) {
        var pulses = getForWorld(world);
        for (var pulse : pulses) {
            pulse.type.executePulse(world, pulse.pos, pulse.dataCpy);
        }
        pulses.clear();
    }

    public static void schedulePulse(EntityPulseType type, World world, Vector3d pos, PulseData.Directional data) {
        getForWorld(world).add(new Entry(type, pos, data));
    }

    private static List<Entry> getForWorld(World world) {
        return scheduledPulses.computeIfAbsent(world, world1 -> new ArrayList<>());
    }

    private static class Entry {
        public EntityPulseType type;
        public Vector3d pos;
        PulseData.Directional dataCpy;

        public Entry(EntityPulseType type, Vector3d pos, PulseData.Directional dataCpy) {
            this.pos = pos;
            this.dataCpy = dataCpy;
            this.type = type;
        }
    }

}
