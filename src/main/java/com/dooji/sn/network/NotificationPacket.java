package com.dooji.sn.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;

public class NotificationPacket {
    public static void send(ServerPlayerEntity player, String name, String type, String sound_namespace,
            String sound_path, String namespace, String texture, int width, int height, boolean dismiss_message,
            boolean alwaysShow) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(name);
        buf.writeString(type);
        buf.writeString(sound_namespace);
        buf.writeString(sound_path);
        buf.writeString(namespace);
        buf.writeString(texture);
        buf.writeInt(width);
        buf.writeInt(height);
        buf.writeBoolean(dismiss_message);
        buf.writeBoolean(alwaysShow);
        ServerPlayNetworking.send(player, new Identifier("server-notify", "notification_packet"), buf);
    }

    public static void send(ServerPlayerEntity player, String name, String type, String sound_namespace,
            String sound_path, String message2, boolean dismiss_button, boolean dismiss_message, boolean alwaysShow) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(name);
        buf.writeString(type);
        buf.writeString(sound_namespace);
        buf.writeString(sound_path);
        buf.writeString(message2);
        buf.writeBoolean(dismiss_button);
        buf.writeBoolean(dismiss_message);
        buf.writeBoolean(alwaysShow);
        ServerPlayNetworking.send(player, new Identifier("server-notify", "notification_packet"), buf);
    }

    public static void send(ServerPlayerEntity player, String name, String type, String sound_namespace,
            String sound_path, String url, int width, int height, boolean dismiss_message,
            boolean alwaysShow) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(name);
        buf.writeString(type);
        buf.writeString(sound_namespace);
        buf.writeString(sound_path);
        buf.writeString(url);
        buf.writeInt(width);
        buf.writeInt(height);
        buf.writeBoolean(dismiss_message);
        buf.writeBoolean(alwaysShow);
        ServerPlayNetworking.send(player, new Identifier("server-notify", "notification_packet"), buf);
    }
}