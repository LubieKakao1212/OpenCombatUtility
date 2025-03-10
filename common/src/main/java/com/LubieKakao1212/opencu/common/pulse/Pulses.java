package com.LubieKakao1212.opencu.common.pulse;

import com.LubieKakao1212.opencu.common.util.EntityUtil;
import com.lubiekakao1212.qulib.math.extensions.Vector3dExtensionsKt;
import com.lubiekakao1212.qulib.math.mc.Vector3m;
import org.joml.Vector3d;

import static java.lang.Math.*;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class Pulses {
    private static final float epsilon = 0.0001f;
    private static final float epsilonSqr = epsilon * epsilon;

    @Deprecated
    public static void repulsorPulse(World world, Vector3d pos, Vector3d direction, double directionBlend, double radius, double force) {
        List<Entity> entityList = PulseUtil.getAffectedEntities(world, pos, radius);

        /*if(OpenCUMod.hasValkyrienSkies()) {
            position = VSGameUtilsKt.toWorldCoordinates(world, position);
        }*/

        force = PulseUtil.getScaledForce(force);

        for(Entity e : entityList) {
            var delta = new Vector3m(e.getBoundingBox().getCenter())
                    .sub(pos);

            var dstSq = delta.lengthSquared();

            //if we have a very small delta we consider it to have an up direction to avoid floating point precision errors
            if(dstSq < epsilonSqr) {
                EntityUtil.addVelocity(e, 0, force, 0);
                continue;
            }

            double distance = Math.sqrt(dstSq);

            var deltaV =
                    new Vector3m(e.getBoundingBox().getCenter())
                            .sub(pos)
                            .mul(force / distance);

            EntityUtil.addVelocity(e, deltaV);
        }
    }

    @Deprecated
    public static void vectorPulse(World world, Vector3d pos, Vector3d direction, double directionBlend, double radius, double force) {
        List<Entity> entityList = PulseUtil.getAffectedEntities(world, pos, radius);
        Vector3d directionForce = direction.mul(PulseUtil.getScaledForce(force));

        /*if(OpenCUMod.hasValkyrienSkies()) {
            Ship ship = VSGameUtilsKt.getShipManagingPos(level, position);
            if(ship != null){
                ShipTransform transform = ship.getTransform();
                directionForce = transform.transformDirectionNoScalingFromShipToWorld(directionForce, directionForce);
            }
        }*/

        for(Entity e : entityList)
        {
            EntityUtil.addVelocity(e, directionForce);
        }
    }

    public static void generalPulse(World world, Vector3d pos, Vector3d direction, double directionBlend, double radius, double force) {
        List<Entity> entityList = PulseUtil.getAffectedEntities(world, pos, radius);

        force = PulseUtil.getScaledForce(force);

        for(Entity e : entityList) {
            var delta = new Vector3m(e.getBoundingBox().getCenter())
                    .sub(pos);

            var dstSq = delta.lengthSquared();

            //if we have a very small delta we consider it to have an up direction to avoid floating point precision errors
            if (dstSq < epsilonSqr) {
                EntityUtil.addVelocity(e, 0, force, 0);
                continue;
            }

            double distance = Math.sqrt(dstSq);

            var deltaV = delta.div(distance);
            deltaV.lerp(direction, directionBlend);

            deltaV.mul(force);
            EntityUtil.addVelocity(e, deltaV);
        }
    }

    //TODO Refine logic to take direction into account
    public static void stasisPulse(World world, Vector3d pos, Vector3d direction, double directionBlend, double radius, double force) {
        List<Entity> entityList = PulseUtil.getAffectedEntities(world, pos, radius);

        double stasisFactor = min(1.0, abs(force));

        stasisFactor = 1 - stasisFactor;

        for(Entity e : entityList) {
            Vector3d movement = Vector3dExtensionsKt.from(new Vector3d(), e.getVelocity());

            if(movement.lengthSquared() < epsilonSqr) {
                EntityUtil.scaleVelocity(e, 0);
                continue;
            }

            EntityUtil.scaleVelocity(e, stasisFactor);
        }
    }
}
