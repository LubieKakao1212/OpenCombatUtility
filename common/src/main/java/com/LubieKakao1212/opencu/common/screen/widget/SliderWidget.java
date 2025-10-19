package com.LubieKakao1212.opencu.common.screen.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SliderWidget extends ClickableWidget {

    private final Axis movementAxis;

    private int resolution = -1;
    private double value;

    private boolean noSync;

    private final Supplier<Double> valueSupplier;

    private final Consumer<Double> syncDelegate;
    private final Consumer<Double> localSyncDelegate;

    private final int knobWidth;
    private final int knobHeight;
    private final int u;
    private final int v;

    private Identifier texture;

    public SliderWidget(Axis axis, Identifier texture, int x, int y, int width, int height, int knobWidth, int knobHeight, int u, int v, Supplier<Double> valueSupplier, Consumer<Double> syncDelegate, Consumer<Double> localSyncDelegate, int resolution) {
        super(x, y, width, height, null);
        this.u = u;
        this.v = v;
        this.texture = texture;
        this.valueSupplier = valueSupplier;
        this.syncDelegate = syncDelegate;
        this.localSyncDelegate = localSyncDelegate;
        this.resolution = resolution;
        this.knobWidth = knobWidth;
        this.knobHeight = knobHeight;
        this.movementAxis = axis;

    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        if(!noSync) {
            value = valueSupplier.get();
        }

        var size = movementAxis.get(width, height);
        var d = value * size - movementAxis.get(knobWidth, knobHeight) / 2.;
        context.drawTexture(texture, getX() + (int)movementAxis.x(d), getY() + (int)movementAxis.y(d), u, v, knobWidth, knobHeight);

    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) { }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        setValueFromMouse(mouseX, mouseY);
        noSync = true;
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        super.onDrag(mouseX, mouseY, deltaX, deltaY);
        setValueFromMouse(mouseX, mouseY);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        syncDelegate.accept(value);
        noSync = false;
    }

    private void setValueFromMouse(double mouseX, double mouseY) {
        var mouseV = movementAxis.get(mouseX, mouseY);
        var posV = movementAxis.get(getX(), getY());
        var size = movementAxis.get(width, height);

        var value = (mouseV - posV) / size;
        value = MathHelper.clamp(value, 0, 1);
        this.value = quantise(value, resolution);
        if(localSyncDelegate != null) {
            localSyncDelegate.accept(this.value);
        }
    }

    private static double quantise(double value, int resolution) {
        return resolution > 0 ? (double) Math.round(value * resolution) / (double)resolution : value;
    }

    public enum Axis {
        Vertical {
            double get(double x, double y) {
                return y;
            }

            @Override
            double y(double v) {
                return v;
            }
            @Override
            double x(double v) {
                return 0;
            }
        },
        Horizontal {
            double get(double x, double y) {
                return x;
            }

            @Override
            double x(double v) {
                return v;
            }
            @Override
            double y(double v) {
                return 0;
            }
        };

        abstract double get(double x, double y);
        abstract double x(double v);
        abstract double y(double v);
    }
}
