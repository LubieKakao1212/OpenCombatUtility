package com.LubieKakao1212.opencu.common.util;

import com.lubiekakao1212.qulib.math.mc.Vector3m;
import com.lubiekakao1212.qulib.raycast.RaycastUtilKt;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

public class EntityUtil {

    public static void addVelocity(Entity e, Vector3d deltaV) {
        addVelocity(e, deltaV.x, deltaV.y, deltaV.z);
    }

    public static void addVelocity(Entity e, double vX, double vY, double vZ) {
        if(e instanceof PersistentProjectileEntity arrow) {
            if(arrow.inGround) {
                var pos = new Vector3m(arrow.getPos());

                //TODO test modified logic
                var world = e.getWorld();
                var dir = safeNormalize(new Vector3d(vX, vY, vZ));

                var wall = RaycastUtilKt.raycastBlocksAll(world, pos, dir, 0.1);
                for (var w : wall) {
                    var blockStatePos = w.getTarget();
                    if(!blockStatePos.getState().getCollisionShape(world, blockStatePos.getPos()).isEmpty()) {
                        return;
                    }
                }
                arrow.inGround = false;
                arrow.setVelocity(0, 0, 0);
            }
        }
        Vec3d movement = e.getVelocity().add(vX, vY, vZ);
        e.setVelocity(movement);
        if(e instanceof LivingEntity && movement.y > 0)
        {
            e.fallDistance = 0;
        }
        e.velocityDirty = true;
        e.velocityModified = true;
    }

    public static void scaleVelocity(Entity e, double scale) {
        Vec3d movement = e.getVelocity();
        e.setVelocity(movement.multiply(scale, scale, scale));
        if(e instanceof LivingEntity && movement.y > 0)
        {
            e.fallDistance = 0;
        }
        e.velocityDirty = true;
        e.velocityModified = true;
    }

    public static double perSecond2perTick(double perSecond) {
        return perSecond / 20.0;
    }

    private static Vector3m blockFaceToNormal(Vector3m surfacePos) {
        var abs = surfacePos.absolute(new Vector3d());
        var sign = new Vector3d(
                Math.signum(surfacePos.x),
                Math.signum(surfacePos.y),
                Math.signum(surfacePos.z)
        );
        if(abs.x > abs.y && abs.x > abs.z) {
            return new Vector3m(sign.x, 0, 0);
        }
        else if(abs.y > abs.x && abs.y > abs.z) {
            return new Vector3m(0, sign.y, 0);
        }
        else if(abs.z > abs.x && abs.z > abs.y) {
            return new Vector3m(0, 0, sign.z);
        }

        //Impossible
        throw new RuntimeException();
    }

    public static Vector3d safeNormalize(Vector3d vec) {
        var l = vec.length();

        if(l > 1f / 64f) {
            vec.normalize();
        }
        else {
            vec.set(0, 1, 0);
        }

        return vec;
    }
}
