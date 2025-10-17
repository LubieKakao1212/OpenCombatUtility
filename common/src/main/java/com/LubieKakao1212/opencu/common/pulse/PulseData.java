package com.LubieKakao1212.opencu.common.pulse;

import com.lubiekakao1212.qulib.math.extensions.Vector3dExtensionsKt;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.joml.Vector3d;

public class PulseData {
    public double radius;
    public double forceMagnitude;
    public double forceSign = 1.;

    public PulseData() {
    }

    public PulseData(PulseData source) {
        this.forceMagnitude = source.forceMagnitude;
        this.radius = source.radius;
        this.forceSign = source.forceSign;
    }

    public NbtCompound serialize() {
        NbtCompound nbt = new NbtCompound();
        nbt.putDouble("radius", radius);
        nbt.putDouble("force", forceMagnitude * forceSign);
        return nbt;
    }

    public void deserialize(NbtCompound nbt) {
        radius = nbt.getDouble("radius");
        forceMagnitude = nbt.getDouble("force");
        forceSign = Math.copySign(1., forceMagnitude);
        forceMagnitude = Math.abs(forceMagnitude);
    }

    public double getForce() {
        return forceMagnitude * forceSign;
    }

    public static class Directional extends PulseData {
        public Vector3d direction;

        public Directional(PulseData source, Vector3d direction) {
            super(source);
            this.direction = direction;
        }

        @Override
        public NbtCompound serialize() {
            var nbt = super.serialize();

            nbt.put("direction", Vector3dExtensionsKt.serializeNBT(direction));
            return nbt;
        }

        @Override
        public void deserialize(NbtCompound nbt) {
            super.deserialize(nbt);
            direction = Vector3dExtensionsKt.deserializeNBT(new Vector3d(), nbt.getList("direction", NbtElement.DOUBLE_TYPE));
        }
    }
}
