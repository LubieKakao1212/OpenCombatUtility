package com.LubieKakao1212.opencu.common.screen;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.OpenCUModCommon;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityDeviceContainer;
import com.LubieKakao1212.opencu.common.network.packet.device.PacketServerToggleRequiresLock;
import com.LubieKakao1212.opencu.common.network.packet.generic.PacketServerCycleRedstoneControl;
import com.LubieKakao1212.opencu.common.screen.handler.DeviceContainerScreenHandler;
import com.LubieKakao1212.opencu.common.screen.widget.FillableBarWidget;
import com.LubieKakao1212.opencu.common.screen.widget.ResponsiveToggleWidget;
import com.LubieKakao1212.opencu.common.util.RedstoneControlType;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModularFrameScreen extends HandledScreen<DeviceContainerScreenHandler> {

    private static final String energyBarTooltipKey = "info.opencu.frame.gui.energy";
    private static final Identifier mainTexture = new Identifier(OpenCUModCommon.MODID, "textures/gui/omnidispenser_gui.png");

    private FillableBarWidget energyWidget;

    public ModularFrameScreen(DeviceContainerScreenHandler container, PlayerInventory inv, Text titleIn) {
        super(container, inv, titleIn);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        //var aimLockToggle = new ToggleButtonWidget(161, 7, 10, 10, false);
        //aimLockToggle.setTextureUV(178, 69, 12,12, mainTexture);
        //aimLockToggle.onClick();
        var lockButton = ResponsiveToggleWidget.dualState(mainTexture,
                x + 149, y + 6,
                10, 10,
                188, 68,
                -11,11,
                () -> handler.getProperty(be -> false, false),
                (state) -> NetworkUtil.sendToServer(new PacketServerToggleRequiresLock(handler.targetPosition())),
                "info.opencu.frame.gui.lock");
        addDrawableChild(lockButton);

        var redstoneControlButton = ResponsiveToggleWidget.multiState(mainTexture,
                x + 160, y + 6,
                10, 10,
                177, 90,
                11, 11,
                () -> handler.getProperty(be -> be.getRedstoneControlType().ordinal(), 0),
                (state) -> NetworkUtil.sendToServer(new PacketServerCycleRedstoneControl(handler.targetPosition())),
                Tooltip.of(Text.empty()),
                Tooltip.of(RedstoneControlType.LOW.tooltip),
                Tooltip.of(RedstoneControlType.HIGH.tooltip),
                Tooltip.of(RedstoneControlType.PULSE.tooltip),
                Tooltip.of(RedstoneControlType.DISABLED.tooltip)
        );

        addDrawableChild(redstoneControlButton);

        energyWidget = new FillableBarWidget(mainTexture,
                x + 15, y + 33,
                13,16,
                226, 58,
                FillableBarWidget.FillDirection.DOWN,
                () -> handler.getProperty(BlockEntityDeviceContainer::getEnergyRatio, 0.0f));
        addDrawableChild(energyWidget);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(context);

        energyWidget.setTooltip(Tooltip.of(Text.translatable(energyBarTooltipKey,
                handler.getProperty(BlockEntityDeviceContainer::getEnergy, 0),
                handler.getProperty(BlockEntityDeviceContainer::getMaxEnergy, 0)
                )
        ));

        //this.drawBackground(poseStack, partialTick, mouseX, mouseY);
        super.render(context, mouseX, mouseY, partialTick);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
//        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
//        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, mainTexture);

        context.drawTexture(mainTexture, x, y,0,0, backgroundWidth, backgroundHeight);
    }
}
