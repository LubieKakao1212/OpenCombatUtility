package com.LubieKakao1212.opencu.common.rendering;

import com.LubieKakao1212.opencu.common.block.entity.renderer.Color;
import com.google.common.collect.Lists;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RenderingUtil {

    public final static List<Direction> dirs = Lists.newArrayList(Direction.values());

    static {
        dirs.add(null);
    }

    //TODO apply ambient occlusion
    public static void renderModel(@NotNull BakedModel model, @NotNull VertexConsumer vc, @NotNull MatrixStack matrixStack, @NotNull Color color, int packedLight, int packedOverlay) {
        for(Direction dir : dirs) {
            for(BakedQuad quad : model.getQuads(null, dir, null)) {
                vc.quad(matrixStack.peek(), quad, color.r, color.g, color.b, packedLight, packedOverlay);
            }
        }
    }

}
