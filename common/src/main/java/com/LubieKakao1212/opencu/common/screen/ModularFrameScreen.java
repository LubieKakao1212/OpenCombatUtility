package com.LubieKakao1212.opencu.common.screen;

import com.LubieKakao1212.opencu.NetworkUtil;
import com.LubieKakao1212.opencu.common.block.entity.BlockEntityModularFrame;
import com.LubieKakao1212.opencu.common.network.packet.devicecontainer.frame.PacketC2SToggleRequiresLock;
import com.LubieKakao1212.opencu.common.screen.handler.DeviceContainerScreenHandler;
import com.LubieKakao1212.opencu.common.screen.widget.ResponsiveToggleWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ModularFrameScreen extends DeviceContainerScreen {


    public ModularFrameScreen(DeviceContainerScreenHandler container, PlayerInventory inv, Text titleIn) {
        super(container, inv, titleIn);
    }

    @Override
    protected void initBasic() {
        super.initBasic();
        var lockButton = ResponsiveToggleWidget.dualState(mainTexture,
                x + 149, y + 6,
                10, 10,
                188, 68,
                -11, 11,
                () -> handler.getProperty(be -> ((BlockEntityModularFrame) be).isRequiresLock(), false),
                (state) -> NetworkUtil.sendToServer(new PacketC2SToggleRequiresLock(handler.targetPosition())),
                "info.opencu.gui.frame.lock");
        addDrawableChild(lockButton);
    }
}
