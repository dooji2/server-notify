package com.dooji.sn.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File BASE_CONFIG_DIR = new File(FabricLoader.getInstance().getConfigDir().toFile(), "Server Notify");
    private static final File SEEN_NOTIFICATIONS_FILE = new File(BASE_CONFIG_DIR, "seen_notifications.json");

    public static NotificationConfig loadConfig() {
        if (!BASE_CONFIG_DIR.exists()) {
            BASE_CONFIG_DIR.mkdirs();
        }

        if (SEEN_NOTIFICATIONS_FILE.exists()) {
            try (FileReader reader = new FileReader(SEEN_NOTIFICATIONS_FILE)) {
                return GSON.fromJson(reader, NotificationConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new NotificationConfig();
    }

    public static void saveConfig(NotificationConfig config) {
        if (!BASE_CONFIG_DIR.exists()) {
            BASE_CONFIG_DIR.mkdirs();
        }

        try (FileWriter writer = new FileWriter(SEEN_NOTIFICATIONS_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}