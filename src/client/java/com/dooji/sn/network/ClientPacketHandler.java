package com.dooji.sn.network;

import com.dooji.sn.gui.TextNotificationScreen;
import com.dooji.sn.gui.TextureNotificationScreen;
import com.dooji.sn.config.ConfigManager;
import com.dooji.sn.config.NotificationConfig;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientPacketHandler {
    public static final Identifier NOTIFICATION_PACKET_ID = new Identifier("server-notify", "notification_packet");
    private static Queue<NotificationData> notificationQueue = new ArrayDeque<>();
    public static boolean isDisplayingNotification = false;
    private static boolean isRegistered = false;
    private static final long NOTIFICATION_DELAY = 1000;
    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static void register() {
        if (!isRegistered) {
            ClientPlayNetworking.registerGlobalReceiver(NOTIFICATION_PACKET_ID,
                    ClientPacketHandler::handleNotificationPacket);

            isRegistered = true;
        } else {

        }
    }

    private static void handleNotificationPacket(MinecraftClient client, ClientPlayNetworkHandler handler,
            PacketByteBuf buf, PacketSender responseSender) {
        String name = buf.readString();
        String type = buf.readString();
        String sound_namespace = buf.readString();
        String sound_path = buf.readString();

        String namespace = null;
        String texture = null;
        int width = 0;
        int height = 0;
        boolean dismiss_message = false;
        boolean alwaysShow = false;
        String message = null;
        boolean dismiss_button = false;

        if (type.equals("texture")) {
            namespace = buf.readString();
            texture = buf.readString();
            width = buf.readInt();
            height = buf.readInt();
            dismiss_message = buf.readBoolean();
            alwaysShow = buf.readBoolean();
        } else if (type.equals("text")) {
            message = buf.readString();
            dismiss_button = buf.readBoolean();
            dismiss_message = buf.readBoolean();
            alwaysShow = buf.readBoolean();
        }

        NotificationConfig config = loadConfig();

        if (!config.getSeenNotifications().contains(name)) {
            config.getSeenNotifications().add(name);
            saveConfig(config);

            String finalNamespace = namespace;
            String finalTexture = texture;
            String finalMessage = message;
            boolean finalDismissMessage = dismiss_message;
            boolean finalAlwaysShow = alwaysShow;
            boolean finalDismissButton = dismiss_button;
            int finalWidth = width;
            int finalHeight = height;

            client.execute(() -> {
                if (type.equals("texture")) {
                    NotificationData notificationData = new NotificationData(name, type, sound_namespace, sound_path,
                            finalNamespace, finalTexture, finalWidth, finalHeight, finalDismissMessage,
                            finalAlwaysShow);
                    notificationQueue.add(notificationData);

                    displayNextNotification(MinecraftClient.getInstance());
                } else if (type.equals("text")) {
                    NotificationData notificationData = new NotificationData(name, type, sound_namespace, sound_path,
                            finalMessage, finalDismissButton, finalDismissMessage, finalAlwaysShow);
                    notificationQueue.add(notificationData);

                    displayNextNotification(MinecraftClient.getInstance());
                }
            });
        } else {
            String finalNamespace = namespace;
            String finalTexture = texture;
            String finalMessage = message;
            boolean finalDismissMessage = dismiss_message;
            boolean finalAlwaysShow = alwaysShow;
            boolean finalDismissButton = dismiss_button;
            int finalWidth = width;
            int finalHeight = height;

            client.execute(() -> {
                if (type.equals("texture") && finalAlwaysShow == true) {
                    NotificationData notificationData = new NotificationData(name, type, sound_namespace, sound_path,
                            finalNamespace, finalTexture, finalWidth, finalHeight, finalDismissMessage,
                            finalAlwaysShow);
                    notificationQueue.add(notificationData);

                    displayNextNotification(MinecraftClient.getInstance());
                } else if (type.equals("text") && finalAlwaysShow == true) {
                    NotificationData notificationData = new NotificationData(name, type, sound_namespace, sound_path,
                            finalMessage, finalDismissButton, finalDismissMessage, finalAlwaysShow);
                    notificationQueue.add(notificationData);

                    displayNextNotification(MinecraftClient.getInstance());
                }
            });
        }
    }

    private static NotificationConfig loadConfig() {
        File configDir = new File(MinecraftClient.getInstance().runDirectory,
                "config" + File.separator + "Server Notify");
        return ConfigManager.loadConfig(configDir);
    }

    private static void saveConfig(NotificationConfig config) {
        File configDir = new File(MinecraftClient.getInstance().runDirectory,
                "config" + File.separator + "Server Notify");
        ConfigManager.saveConfig(config, configDir);
    }

    public static void displayNextNotification(MinecraftClient client) {

        if (!isDisplayingNotification && !notificationQueue.isEmpty()) {
            NotificationData notificationData = notificationQueue.poll();
            String type = notificationData.getType();

            client.execute(() -> {
                if (type.equals("texture")) {
                    TextureNotificationScreen notificationScreen = new TextureNotificationScreen(notificationData,
                            type);
                    client.setScreen(notificationScreen);
                } else if (type.equals("text")) {
                    TextNotificationScreen notificationScreen = new TextNotificationScreen(notificationData, type);
                    client.setScreen(notificationScreen);
                }
            });

            isDisplayingNotification = true;

            executorService.schedule(() -> {
                isDisplayingNotification = false;
            }, NOTIFICATION_DELAY, TimeUnit.MILLISECONDS);
        } else if (notificationQueue.isEmpty()) {

            isDisplayingNotification = false;
        } else {

        }
    }
}
