package com.dooji.sn.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FOLDER_NAME = "Server Notify";
    private static final String CONFIG_FILE_NAME = "notifications.json";

    public static NotificationConfig loadConfig(File configDir) {

        File configFolder = new File(configDir, CONFIG_FOLDER_NAME);
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }

        File configFile = new File(configFolder, CONFIG_FILE_NAME);

        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                return GSON.fromJson(reader, NotificationConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new NotificationConfig();
    }

    public static void saveConfig(NotificationConfig config, File configDir) {

        File configFolder = new File(configDir, CONFIG_FOLDER_NAME);
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }

        File configFile = new File(configFolder, CONFIG_FILE_NAME);

        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
