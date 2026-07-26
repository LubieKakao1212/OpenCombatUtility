package com.LubieKakao1212.opencu.common.pulse;

import com.LubieKakao1212.opencu.common.compat.valkyrienskies.VS2SoftUtil;
import com.LubieKakao1212.opencu.common.config.api.IOpenCUConfig;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Vector3d;

import java.util.List;


public class PulseUtil {

    public static List<Entity> getAffectedEntities(World level, Vector3d pos, double radius) {
        double radiusSqr = radius * radius;
        return level.getOtherEntities(null,
                new Box(0,0,0,0,0,0)
                        .offset(new Vec3d(pos.x, pos.y, pos.z))
                        .expand(radius)
                , entity ->
                        VS2SoftUtil.getDistanceSqr(level,
                                entity.getBoundingBox(),
                                pos
                        ) < radiusSqr);
    }

    public static double getScaledForce(double baseForce) {
        return baseForce * IOpenCUConfig.CONFIG.repulsorDevice().forceScale();
    }

    @ApiStatus.Experimental
    public static List<Entity> applyFilter(List<Entity> entities, List<String> filters) {
        return entities;
    }

}
