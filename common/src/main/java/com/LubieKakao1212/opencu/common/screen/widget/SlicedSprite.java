package com.LubieKakao1212.opencu.common.screen.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class SlicedSprite implements Drawable {

    private final Identifier texture;
    private int x, y, width, height;
    private final int u, v, tW, tH;

    private final int right, left, top, bottom;

    @NotNull
    public static SlicedSprite horizontal() {
        return null;
    }

    public SlicedSprite(Identifier texture, int x, int y, int width, int height, int u, int v, int tW, int tH, int right, int left, int top, int bottom) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.tW = tW;
        this.tH = tH;
        this.right = right;
        this.left = left;
        this.top = top;
        this.bottom = bottom;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean r = right > 0;
        boolean l = left > 0;
        boolean t = top > 0;
        boolean b = bottom > 0;

        int topV = v;
        int centerV = v + top;
        int bottomV = v + tH - bottom;

        int leftU = u;
        int centerU = u + left;
        int rightU = u + tW - right;

        int centerTexW = tW - (right + left);
        int centerTexH = tH - (bottom + top);

        int topY = y - top;
        int bottomY = y + height;
        int leftX = x - left;
        int rightX = x + width;
        int centerX = x;
        int centerY = y;

        context.drawTexture(texture, centerX, centerY, width, height,
                centerU, centerV,
                centerTexW, centerTexH,
                256, 256);

        //Left
        draw(context, l,
                leftX, centerY,
                left, height,
                leftU, centerV,
                left, centerTexH);
        //Right
        draw(context, r,
                rightX, centerY,
                right, height,
                rightU, centerV,
                right, centerTexH);

        //Top
        draw(context, t,
                centerX, topY,
                width, top,
                centerU, topV,
                centerTexW, top);
        //Bottom
        draw(context, t,
                centerX, bottomY,
                width, bottom,
                centerU, bottomV,
                centerTexW, bottom);

        //TopLeft
        draw(context, l && t,
                leftX, topY,
                left, top,
                leftU, topV,
                left, top);
        //Top Right
        draw(context, r && t,
                rightX, topY,
                right, top,
                rightU, topV,
                right, top);

        //Bottom Left
        draw(context, t,
                leftX, bottomY,
                left, bottom,
                leftU, bottomV,
                left, bottom);
        //Bottom Right
        draw(context, t,
                rightX, bottomY,
                right, bottom,
                rightU, bottomV,
                right, bottom);

//        if(l) {
//            context.drawTexture(texture, leftX, centerY, left, height,
//                    leftU, centerV,
//                    left, centerTexH,
//                    256,256);
//        }
//        if(r) {
//            context.drawTexture(texture, rightX, centerY, right, height,
//                    rightU, centerV,
//                    right, centerTexH,
//                    256, 256);
//        }
//
//        if(t) {
//            context.drawTexture(texture, centerX, topY, width, top,
//                    centerU, topV,
//                    centerTexW, top,
//                    256, 256);
//        }
//        if(b) {
//            context.drawTexture(texture, centerX, bottomY, width, bottom,
//                    centerU, bottomV,
//                    centerTexW, bottom,
//                    256, 256);
//        }
//
//        if(l && t) {
//            context.drawTexture(texture, leftX, topY, left, top,
//                    leftU, topV,
//                    left, top,
//                    256, 256);
//        }
//        if(r && t) {
//            context.drawTexture(texture, rightX, topY, right, top,
//                    rightU, topV,
//                    left, top,
//                    256, 256);
//        }
//
//        if(l && b) {
//            context.drawTexture(texture, leftX, bottomY, left, bottom,
//                    leftU, bottomV,
//                    left, bottom,
//                    256, 256);
//        }
//        if(r && b) {
//            context.drawTexture(texture, rightX, bottomY, right, bottom,
//                    rightU, bottomV,
//                    left, bottom,
//                    256, 256);
//        }
    }

    public SlicedSprite setX(int value) {
        this.x = value;
        return this;
    }

    public SlicedSprite setY(int value) {
        this.y = value;
        return this;
    }

    public SlicedSprite setWidth(int value) {
        this.width = value;
        return this;
    }

    public SlicedSprite setHeight(int value) {
        this.height = value;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private void draw(DrawContext ctx, boolean when, int x, int y, int w, int h, int u, int v, int rw, int rh) {
        if(when) {
            ctx.drawTexture(texture, x, y, w, h,
                    u, v,
                    rw, rh,
                    256, 256);
        }
    }

}
