package com.LubieKakao1212.opencu.common.block.entity;

import com.LubieKakao1212.opencu.common.util.RedstoneControlType;

public interface IRedstoneControlled {

    void cycleRedstoneControl();

    void setRedstoneControlType(RedstoneControlType type);

    RedstoneControlType getRedstoneControlType();

}
