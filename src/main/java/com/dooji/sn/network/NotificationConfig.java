package com.dooji.sn.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class NotificationConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File BASE_CONFIG_DIR = new File(FabricLoader.getInstance().getConfigDir().toFile(), "Server Notify");
    private static final File NOTIFICATIONS_FILE = new File(BASE_CONFIG_DIR, "notifications.json");

    public static void initConfig() {
        if (!BASE_CONFIG_DIR.exists()) {
            BASE_CONFIG_DIR.mkdirs();
        }

        if (!NOTIFICATIONS_FILE.exists()) {
            try {
                JsonObject root = new JsonObject();
                JsonArray notificationsArray = new JsonArray();
                root.add("notifications", notificationsArray);

                writeConfig(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeConfig(JsonObject jsonObject) throws IOException {
        try (Writer writer = new FileWriter(NOTIFICATIONS_FILE)) {
            GSON.toJson(jsonObject, writer);
        }
    }

    public static JsonObject loadConfig() {
        try (Reader reader = new FileReader(NOTIFICATIONS_FILE)) {
            return GSON.fromJson(reader, JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveConfig(JsonObject configObject) {
        try {
            writeConfig(configObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}