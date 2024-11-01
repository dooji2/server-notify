package com.dooji.sn.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class NotificationPacket {

    public static void sendTextureNotification(ServerPlayerEntity player, String name, String type, String soundNamespace,
                                               String soundPath, String namespace, String texture, int width, int height,
                                               boolean dismissMessage, boolean alwaysShow) {
        TextureNotificationPayload payload = new TextureNotificationPayload(name, type, soundNamespace, soundPath,
                namespace, texture, width, height, dismissMessage, alwaysShow);
        ServerPlayNetworking.send(player, payload);
    }

    public static void sendTextNotification(ServerPlayerEntity player, String name, String type, String soundNamespace,
                                            String soundPath, String message, boolean dismissButton, boolean dismissMessage,
                                            boolean alwaysShow) {
        TextNotificationPayload payload = new TextNotificationPayload(name, type, soundNamespace, soundPath,
                message, dismissButton, dismissMessage, alwaysShow);
        ServerPlayNetworking.send(player, payload);
    }

    public static void sendURLNotification(ServerPlayerEntity player, String name, String type, String soundNamespace,
                                           String soundPath, String url, int width, int height, boolean dismissMessage,
                                           boolean alwaysShow) {
        URLNotificationPayload payload = new URLNotificationPayload(name, type, soundNamespace, soundPath,
                url, width, height, dismissMessage, alwaysShow);
        ServerPlayNetworking.send(player, payload);
    }
}