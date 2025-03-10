package com.LubieKakao1212.opencu.common.pulse;

import net.minecraft.world.World;
import org.joml.Vector3d;

@FunctionalInterface
public interface IPulse {

    /**
     *
     * @param level
     * @param pos
     * @param direction
     * @param directionBlend how much should the direction affect the result
     * @param radius
     * @param force
     */
    void doPulse(World level, Vector3d pos, Vector3d direction, double directionBlend, double radius, double force);

}
