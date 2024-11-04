package com.LubieKakao1212.opencu.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class PlacementUtil {

    public static final float defaultDeadZone = 60f;

    public static Direction getLookDirectionForPlacement(Entity entity) {
        return getLookDirectionForPlacement(entity, defaultDeadZone);
    }

    public static Direction getLookDirectionForPlacement(Entity entity, float verticalDeadZone) {
        var vertical = getVerticalDirection(entity.getPitch(), verticalDeadZone);
        if(vertical.isPresent()) {
            return vertical.get();
        }

        var order = Direction.getEntityFacingOrder(entity);
        return order[0].getAxis() != Direction.Axis.Y ? order[0] : order[1];
    }

    public static Optional<Direction> getVerticalDirection(float pitch, float deadZone) {
        return Math.abs(pitch) < deadZone ? Optional.empty() :
                (pitch < 0) ? Optional.of(Direction.DOWN) : Optional.of(Direction.UP);
    }

}
