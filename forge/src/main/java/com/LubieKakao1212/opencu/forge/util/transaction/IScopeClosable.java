package com.LubieKakao1212.opencu.forge.util.transaction;

public interface IScopeClosable {

    void onPush();
    void onPop(boolean commited);

}
