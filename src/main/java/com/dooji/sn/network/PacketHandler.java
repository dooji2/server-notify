package com.dooji.sn.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class PacketHandler {

    public static void register() {
        PayloadTypeRegistry.playS2C().register(TextureNotificationPayload.ID, TextureNotificationPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(TextNotificationPayload.ID, TextNotificationPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(URLNotificationPayload.ID, URLNotificationPayload.CODEC);
    }
}