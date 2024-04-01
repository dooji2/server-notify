package com.dooji.sn.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class PacketHandler {
    public static final Identifier NOTIFICATION_PACKET_ID = new Identifier("server-notify", "notification_packet");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(NOTIFICATION_PACKET_ID, PacketHandler::handleNotificationPacket);
    }

    public static void sendNotificationPacket(ServerPlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        ServerPlayNetworking.send(player, NOTIFICATION_PACKET_ID, buf);
    }

    private static void handleNotificationPacket(MinecraftServer server, ServerPlayerEntity player,
            ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {

    }
}
