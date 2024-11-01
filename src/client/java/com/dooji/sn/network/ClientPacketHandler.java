package com.dooji.sn.network;

import com.dooji.sn.gui.TextNotificationScreen;
import com.dooji.sn.gui.TextureNotificationScreen;
import com.dooji.sn.gui.URLTextureNotificationScreen;
import com.dooji.sn.config.ConfigManager;
import com.dooji.sn.config.NotificationConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientPacketHandler {

    private static final Queue<NotificationData> notificationQueue = new ArrayDeque<>();
    public static boolean isDisplayingNotification = false;
    private static boolean isRegistered = false;
    private static final long NOTIFICATION_DELAY = 1000;
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static void register() {
        if (!isRegistered) {
            PayloadTypeRegistry.playS2C().register(TextureNotificationPayload.ID, TextureNotificationPayload.CODEC);
            PayloadTypeRegistry.playS2C().register(TextNotificationPayload.ID, TextNotificationPayload.CODEC);
            PayloadTypeRegistry.playS2C().register(URLNotificationPayload.ID, URLNotificationPayload.CODEC);

            ClientPlayNetworking.registerGlobalReceiver(TextureNotificationPayload.ID, (payload, context) ->
                    handleTextureNotification(context.client(), payload)
            );

            ClientPlayNetworking.registerGlobalReceiver(TextNotificationPayload.ID, (payload, context) ->
                    handleTextNotification(context.client(), payload)
            );

            ClientPlayNetworking.registerGlobalReceiver(URLNotificationPayload.ID, (payload, context) ->
                    handleURLNotification(context.client(), payload)
            );

            isRegistered = true;
        }
    }

    private static void handleTextureNotification(MinecraftClient client, TextureNotificationPayload payload) {
        NotificationConfig config = loadConfig();
        String name = payload.name();

        if (!config.getSeenNotifications().contains(name)) {
            config.getSeenNotifications().add(name);
            saveConfig(config);
        }

        NotificationData notificationData = new NotificationData(
                name, payload.type(), payload.soundNamespace(), payload.soundPath(),
                payload.namespace(), payload.texture(), payload.width(), payload.height(),
                payload.dismissMessage(), payload.alwaysShow()
        );

        client.execute(() -> {
            notificationQueue.add(notificationData);
            displayNextNotification(client);
        });
    }

    private static void handleTextNotification(MinecraftClient client, TextNotificationPayload payload) {
        NotificationConfig config = loadConfig();
        String name = payload.name();

        if (!config.getSeenNotifications().contains(name)) {
            config.getSeenNotifications().add(name);
            saveConfig(config);
        }

        NotificationData notificationData = new NotificationData(
                name, payload.type(), payload.soundNamespace(), payload.soundPath(),
                payload.message(), payload.dismissButton(), payload.dismissMessage(),
                payload.alwaysShow()
        );

        client.execute(() -> {
            notificationQueue.add(notificationData);
            displayNextNotification(client);
        });
    }

    private static void handleURLNotification(MinecraftClient client, URLNotificationPayload payload) {
        NotificationConfig config = loadConfig();
        String name = payload.name();

        if (!config.getSeenNotifications().contains(name)) {
            config.getSeenNotifications().add(name);
            saveConfig(config);
        }

        NotificationData notificationData = new NotificationData(
                name, payload.type(), payload.soundNamespace(), payload.soundPath(),
                payload.url(), payload.width(), payload.height(),
                payload.dismissMessage(), payload.alwaysShow()
        );

        client.execute(() -> {
            notificationQueue.add(notificationData);
            displayNextNotification(client);
        });
    }

    private static NotificationConfig loadConfig() {
        File configDir = new File(MinecraftClient.getInstance().runDirectory, "config" + File.separator + "Server Notify");
        return ConfigManager.loadConfig(configDir);
    }

    private static void saveConfig(NotificationConfig config) {
        File configDir = new File(MinecraftClient.getInstance().runDirectory, "config" + File.separator + "Server Notify");
        ConfigManager.saveConfig(config, configDir);
    }

    public static void displayNextNotification(MinecraftClient client) {
        if (!isDisplayingNotification && !notificationQueue.isEmpty()) {
            NotificationData notificationData = notificationQueue.poll();
            String type = notificationData.getType();

            client.execute(() -> {
                if (type.equals("texture")) {
                    client.setScreen(new TextureNotificationScreen(notificationData, type));
                } else if (type.equals("text")) {
                    client.setScreen(new TextNotificationScreen(notificationData, type));
                } else if (type.equals("url")) {
                    client.setScreen(new URLTextureNotificationScreen(notificationData, type));
                }
            });

            isDisplayingNotification = true;

            executorService.schedule(() -> {
                isDisplayingNotification = false;
            }, NOTIFICATION_DELAY, TimeUnit.MILLISECONDS);
        } else if (notificationQueue.isEmpty()) {
            isDisplayingNotification = false;
        }
    }
}