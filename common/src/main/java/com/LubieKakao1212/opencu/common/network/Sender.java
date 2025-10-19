package com.LubieKakao1212.opencu.common.network;

public interface Sender {
    <T extends Record> void send(T packet);
}
