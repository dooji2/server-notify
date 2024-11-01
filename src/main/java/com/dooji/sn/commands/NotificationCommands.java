package com.dooji.sn.commands;

import com.dooji.sn.network.NotificationConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class NotificationCommands {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> command = LiteralArgumentBuilder
                .<ServerCommandSource>literal("server-notify")
                .requires(source -> source.hasPermissionLevel(2)
                        || source.hasPermissionLevel(source.getServer().getOpPermissionLevel()))
                .then(CommandManager.literal("new")
                        .requires(source -> source.hasPermissionLevel(2)
                                || source.hasPermissionLevel(source.getServer().getOpPermissionLevel()))
                        .then(CommandManager.argument("friendly_name", StringArgumentType.string())
                                .then(CommandManager.argument("type", StringArgumentType.word())
                                        .then(CommandManager.literal("texture")
                                                .then(CommandManager
                                                        .argument("sound_namespace", StringArgumentType.word())
                                                        .then(CommandManager
                                                                .argument("sound_path", StringArgumentType.string())
                                                                .then(CommandManager
                                                                        .argument("namespace",
                                                                                StringArgumentType.word())
                                                                        .then(CommandManager
                                                                                .argument("texture",
                                                                                        StringArgumentType.string())
                                                                                .then(CommandManager
                                                                                        .argument("width",
                                                                                                IntegerArgumentType
                                                                                                        .integer())
                                                                                        .then(CommandManager.argument(
                                                                                                "height",
                                                                                                IntegerArgumentType
                                                                                                        .integer())
                                                                                                .then(CommandManager
                                                                                                        .argument(
                                                                                                                "dismiss_message",
                                                                                                                BoolArgumentType
                                                                                                                        .bool())
                                                                                                        .then(CommandManager
                                                                                                                .argument(
                                                                                                                        "alwaysShow",
                                                                                                                        BoolArgumentType
                                                                                                                                .bool())
                                                                                                                .executes(
                                                                                                                        context -> executeAddNotification(
                                                                                                                                context)))))))))))
                                        .then(CommandManager.literal("text")
                                                .then(CommandManager
                                                        .argument("sound_namespace", StringArgumentType.word())
                                                        .then(CommandManager
                                                                .argument("sound_path", StringArgumentType.string())
                                                                .then(CommandManager
                                                                        .argument("message",
                                                                                StringArgumentType.string())
                                                                        .then(CommandManager
                                                                                .argument("dismiss_button",
                                                                                        BoolArgumentType.bool())
                                                                                .then(CommandManager
                                                                                        .argument("dismiss_message",
                                                                                                BoolArgumentType.bool())
                                                                                        .then(CommandManager
                                                                                                .argument("alwaysShow",
                                                                                                        BoolArgumentType
                                                                                                                .bool())
                                                                                                .executes(
                                                                                                        context -> executeAddNotification(
                                                                                                                context)))))))))
                                        .then(CommandManager.literal("url")
                                                .then(CommandManager.argument("url", StringArgumentType.string())
                                                        .then(CommandManager
                                                                .argument("sound_namespace", StringArgumentType.word())
                                                                .then(CommandManager
                                                                        .argument("sound_path",
                                                                                StringArgumentType.string())
                                                                        .then(CommandManager
                                                                                .argument("width",
                                                                                        IntegerArgumentType.integer())
                                                                                .then(CommandManager
                                                                                        .argument("height",
                                                                                                IntegerArgumentType
                                                                                                        .integer())
                                                                                        .then(CommandManager.argument(
                                                                                                "dismiss_message",
                                                                                                BoolArgumentType.bool())
                                                                                                .then(CommandManager
                                                                                                        .argument(
                                                                                                                "alwaysShow",
                                                                                                                BoolArgumentType
                                                                                                                        .bool())
                                                                                                        .executes(
                                                                                                                context -> executeAddNotification(
                                                                                                                        context)))))))))))))
                .then(CommandManager.literal("list")
                        .requires(source -> source.hasPermissionLevel(2)
                                || source.hasPermissionLevel(source.getServer().getOpPermissionLevel()))
                        .executes(context -> executeListNotifications(context)))
                .then(CommandManager.literal("uuid-list")
                        .requires(source -> source.hasPermissionLevel(2)
                                || source.hasPermissionLevel(source.getServer().getOpPermissionLevel()))
                        .executes(context -> executeListNotificationUUIDs(context)))
                .then(CommandManager.literal("remove")
                        .requires(source -> source.hasPermissionLevel(2)
                                || source.hasPermissionLevel(source.getServer().getOpPermissionLevel()))
                        .then(CommandManager.argument("uuid", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    JsonObject configObject = NotificationConfig.loadConfig();
                                    JsonArray notificationsArray = configObject.getAsJsonArray("notifications");
                                    for (int i = 0; i < notificationsArray.size(); i++) {
                                        JsonObject notification = notificationsArray.get(i).getAsJsonObject();
                                        String uuid = notification.get("name").getAsString();
                                        builder.suggest(uuid);
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> executeRemoveNotification(context))))
                .then(CommandManager.literal("edit")
                        .requires(source -> source.hasPermissionLevel(2)
                                || source.hasPermissionLevel(source.getServer().getOpPermissionLevel()))
                        .then(CommandManager.argument("uuid", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    JsonObject configObject = NotificationConfig.loadConfig();
                                    JsonArray notificationsArray = configObject.getAsJsonArray("notifications");
                                    for (int i = 0; i < notificationsArray.size(); i++) {
                                        JsonObject notification = notificationsArray.get(i).getAsJsonObject();
                                        String uuid = notification.get("name").getAsString();
                                        builder.suggest(uuid);
                                    }
                                    return builder.buildFuture();
                                })
                                .then(CommandManager.argument("element", StringArgumentType.word())
                                        .suggests((context, builder) -> {
                                            String type = context.getArgument("type", String.class);

                                            switch (type.toLowerCase()) {
                                                case "texture":
                                                    builder.suggest("friendly_name");
                                                    builder.suggest("type");
                                                    builder.suggest("sound_namespace");
                                                    builder.suggest("sound_path");
                                                    builder.suggest("namespace");
                                                    builder.suggest("texture");
                                                    builder.suggest("width");
                                                    builder.suggest("height");
                                                    builder.suggest("dismiss_message");
                                                    builder.suggest("alwaysShow");
                                                    break;
                                                case "text":
                                                    builder.suggest("friendly_name");
                                                    builder.suggest("type");
                                                    builder.suggest("sound_namespace");
                                                    builder.suggest("sound_path");
                                                    builder.suggest("message");
                                                    builder.suggest("dismiss_button");
                                                    builder.suggest("dismiss_message");
                                                    builder.suggest("alwaysShow");
                                                    break;
                                                case "url":
                                                    builder.suggest("friendly_name");
                                                    builder.suggest("type");
                                                    builder.suggest("sound_namespace");
                                                    builder.suggest("sound_path");
                                                    builder.suggest("url");
                                                    builder.suggest("width");
                                                    builder.suggest("height");
                                                    builder.suggest("dismiss_message");
                                                    builder.suggest("alwaysShow");
                                                    break;
                                                default:
                                                    break;
                                            }

                                            return builder.buildFuture();
                                        })
                                        .then(CommandManager.argument("newvalue", StringArgumentType.word())
                                                .executes(context -> executeEditNotification(context))))))
                .then(CommandManager.literal("info")
                        .requires(source -> source.hasPermissionLevel(2)
                                || source.hasPermissionLevel(source.getServer().getOpPermissionLevel()))
                        .then(CommandManager.argument("uuid", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    JsonObject configObject = NotificationConfig.loadConfig();
                                    JsonArray notificationsArray = configObject.getAsJsonArray("notifications");
                                    for (int i = 0; i < notificationsArray.size(); i++) {
                                        JsonObject notification = notificationsArray.get(i).getAsJsonObject();
                                        String uuid = notification.get("name").getAsString();
                                        builder.suggest(uuid);
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> executeNotificationInfo(context))));

        dispatcher.register(command);
    }

    private static int executeAddNotification(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String friendlyName = context.getArgument("friendly_name", String.class);
        String type = context.getArgument("type", String.class);
        String soundNamespace = context.getArgument("sound_namespace", String.class);
        String soundPath = context.getArgument("sound_path", String.class);
        boolean dismissButton = false;

        if (type.equalsIgnoreCase("text")) {
            dismissButton = context.getArgument("dismiss_button", Boolean.class);
        }

        boolean dismissMessage = context.getArgument("dismiss_message", Boolean.class);
        boolean alwaysShow = context.getArgument("alwaysShow", Boolean.class);

        switch (type.toLowerCase()) {
            case "texture":
                addNotification(source, friendlyName, type, soundNamespace, soundPath,
                        context.getArgument("namespace", String.class),
                        context.getArgument("texture", String.class),
                        context.getArgument("width", Integer.class),
                        context.getArgument("height", Integer.class),
                        dismissButton,
                        dismissMessage,
                        alwaysShow);
                break;
            case "text":
                addNotification(source, friendlyName, type, soundNamespace, soundPath,
                        context.getArgument("message", String.class),
                        "",
                        0,
                        0,
                        dismissButton,
                        dismissMessage,
                        alwaysShow);
                break;
            case "url":
                addNotification(source, friendlyName, type, soundNamespace, soundPath,
                        context.getArgument("url", String.class),
                        "",
                        context.getArgument("width", Integer.class),
                        context.getArgument("height", Integer.class),
                        dismissButton,
                        dismissMessage,
                        alwaysShow);
                break;
            default:
                source.sendError(Text.of("Invalid notification type."));
                return 0;
        }

        return 1;
    }

    private static int executeListNotifications(CommandContext<ServerCommandSource> context) {
        listNotifications(context.getSource());
        return 1;
    }

    private static int executeListNotificationUUIDs(CommandContext<ServerCommandSource> context) {
        listNotificationUUIDs(context.getSource());
        return 1;
    }

    private static int executeRemoveNotification(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String uuid = context.getArgument("uuid", String.class);
        removeNotification(source, uuid);
        return 1;
    }

    private static int executeEditNotification(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String uuid = context.getArgument("uuid", String.class);
        String whatToEdit = context.getArgument("element", String.class);

        if (whatToEdit == null) {
            source.sendError(Text.of("You must specify what to edit."));
            return 0;
        }

        switch (whatToEdit.toLowerCase()) {
            case "friendly_name":
            case "type":
            case "sound_namespace":
            case "sound_path":
            case "namespace":
            case "texture":
            case "width":
            case "height":
            case "dismiss_message":
            case "alwaysshow":
                break;
            default:
                source.sendError(Text.of("Invalid property to edit."));
                return 0;
        }

        String newValue = context.getArgument("newvalue", String.class);

        editNotification(source, uuid, whatToEdit, newValue);
        return 1;
    }

    private static int executeNotificationInfo(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        String uuid = context.getArgument("uuid", String.class);

        JsonObject configObject = NotificationConfig.loadConfig();
        JsonArray notificationsArray = configObject.getAsJsonArray("notifications");

        for (int i = 0; i < notificationsArray.size(); i++) {
            JsonObject notification = notificationsArray.get(i).getAsJsonObject();
            if (notification.get("name").getAsString().equals(uuid)) {
                String type = notification.get("type").getAsString();
                switch (type.toLowerCase()) {
                    case "texture":
                        displayTextureNotificationInfo(source, notification);
                        break;
                    case "text":
                        displayTextNotificationInfo(source, notification);
                        break;
                    case "url":
                        displayURLNotificationInfo(source, notification);
                        break;
                    default:
                        source.sendError(Text.of("Invalid notification type."));
                        return 0;
                }
                return 1;
            }
        }
        source.sendError(Text.literal("Notification with UUID " + uuid + " not found.").formatted(Formatting.RED));
        return 0;
    }

    private static void displayTextureNotificationInfo(ServerCommandSource source, JsonObject notification) {
        source.sendFeedback(Text.literal("Notification Info for UUID: " + notification.get("name").getAsString())
                .formatted(Formatting.YELLOW), true);
        source.sendFeedback(Text.literal("Friendly Name: " + notification.get("friendly_name").getAsString()), false);
        source.sendFeedback(Text.literal("Type: Texture"), false);
        source.sendFeedback(Text.literal("Namespace: " + notification.get("namespace").getAsString()), false);
        source.sendFeedback(Text.literal("Texture: " + notification.get("texture").getAsString()), false);
        source.sendFeedback(Text.literal("Width: " + notification.get("width").getAsInt()), false);
        source.sendFeedback(Text.literal("Height: " + notification.get("height").getAsInt()), false);
        source.sendFeedback(Text.literal("Dismiss Message: " + notification.get("dismiss_message").getAsBoolean()),
                false);
        source.sendFeedback(Text.literal("Always Show: " + notification.get("alwaysShow").getAsBoolean()), false);
    }

    private static void displayTextNotificationInfo(ServerCommandSource source, JsonObject notification) {
        source.sendFeedback(Text.literal("Notification Info for UUID: " + notification.get("name").getAsString())
                .formatted(Formatting.YELLOW), true);
        source.sendFeedback(Text.literal("Friendly Name: " + notification.get("friendly_name").getAsString()), false);
        source.sendFeedback(Text.literal("Type: Text"), false);
        source.sendFeedback(Text.literal("Message: " + notification.get("message").getAsString()), false);
        source.sendFeedback(Text.literal("Dismiss Message: " + notification.get("dismiss_message").getAsBoolean()),
                false);
        source.sendFeedback(Text.literal("Always Show: " + notification.get("alwaysShow").getAsBoolean()), false);
    }

    private static void displayURLNotificationInfo(ServerCommandSource source, JsonObject notification) {
        source.sendFeedback(Text.literal("Notification Info for UUID: " + notification.get("name").getAsString())
                .formatted(Formatting.YELLOW), true);
        source.sendFeedback(Text.literal("Friendly Name: " + notification.get("friendly_name").getAsString()), false);
        source.sendFeedback(Text.literal("Type: URL"), false);
        source.sendFeedback(Text.literal("URL: " + notification.get("url").getAsString()), false);
        source.sendFeedback(Text.literal("Width: " + notification.get("width").getAsInt()), false);
        source.sendFeedback(Text.literal("Height: " + notification.get("height").getAsInt()), false);
        source.sendFeedback(Text.literal("Dismiss Message: " + notification.get("dismiss_message").getAsBoolean()),
                false);
        source.sendFeedback(Text.literal("Always Show: " + notification.get("alwaysShow").getAsBoolean()), false);
    }

    private static void addNotification(ServerCommandSource source, String friendlyName, String type,
            String soundNamespace, String soundPath, String url, String texture, int width, int height,
            boolean dismissButton, boolean dismissMessage, boolean alwaysShow) {
        JsonObject notification = new JsonObject();
        notification.addProperty("friendly_name", friendlyName);
        notification.addProperty("name", java.util.UUID.randomUUID().toString());
        notification.addProperty("type", type);
        notification.addProperty("sound_namespace", soundNamespace);
        notification.addProperty("sound_path", soundPath);

        switch (type.toLowerCase()) {
            case "texture":
                notification.addProperty("namespace", url);
                notification.addProperty("texture", texture);
                notification.addProperty("width", width);
                notification.addProperty("height", height);
                notification.addProperty("dismiss_message", dismissMessage);
                notification.addProperty("alwaysShow", alwaysShow);
                break;
            case "text":
                notification.addProperty("message", url);
                notification.addProperty("dismiss_button", dismissButton);
                notification.addProperty("dismiss_message", dismissMessage);
                notification.addProperty("alwaysShow", alwaysShow);
                break;
            case "url":
                notification.addProperty("url", url);
                notification.addProperty("width", width);
                notification.addProperty("height", height);
                notification.addProperty("dismiss_message", dismissMessage);
                notification.addProperty("alwaysShow", alwaysShow);
                break;
            default:
                source.sendError(Text.of("Invalid notification type."));
                return;
        }

        JsonObject configObject = NotificationConfig.loadConfig();
        JsonArray notificationsArray = configObject.getAsJsonArray("notifications");
        notificationsArray.add(notification);
        NotificationConfig.saveConfig(configObject);

        source.sendFeedback(Text.of("Notification added successfully."), true);
    }

    private static void listNotifications(ServerCommandSource source) {
        JsonObject configObject = NotificationConfig.loadConfig();
        JsonArray notificationsArray = configObject.getAsJsonArray("notifications");

        source.sendFeedback(Text.literal("Notifications List:").formatted(Formatting.YELLOW), false);
        for (int i = 0; i < notificationsArray.size(); i++) {
            JsonObject notification = notificationsArray.get(i).getAsJsonObject();
            String friendlyName = notification.get("friendly_name").getAsString();
            String type = notification.get("type").getAsString();
            source.sendFeedback(Text.literal("- " + friendlyName + " - " + type), false);
        }
    }

    private static void listNotificationUUIDs(ServerCommandSource source) {
        JsonObject configObject = NotificationConfig.loadConfig();
        JsonArray notificationsArray = configObject.getAsJsonArray("notifications");

        source.sendFeedback(Text.literal("Notification UUIDs:").formatted(Formatting.YELLOW), false);
        for (int i = 0; i < notificationsArray.size(); i++) {
            JsonObject notification = notificationsArray.get(i).getAsJsonObject();
            String uuid = notification.get("name").getAsString();
            String friendlyName = notification.get("friendly_name").getAsString();
            source.sendFeedback(Text.literal("- " + uuid + " - " + friendlyName), false);
        }
    }

    private static void removeNotification(ServerCommandSource source, String uuid) {
        JsonObject configObject = NotificationConfig.loadConfig();
        JsonArray notificationsArray = configObject.getAsJsonArray("notifications");

        for (int i = 0; i < notificationsArray.size(); i++) {
            JsonObject notification = notificationsArray.get(i).getAsJsonObject();
            if (notification.get("name").getAsString().equals(uuid)) {
                notificationsArray.remove(i);
                NotificationConfig.saveConfig(configObject);
                source.sendFeedback(Text.literal("Notification with UUID " + uuid + " removed successfully.")
                        .formatted(Formatting.GREEN), true);
                return;
            }
        }
        source.sendFeedback(Text.literal("Notification with UUID " + uuid + " not found.").formatted(Formatting.RED),
                true);
    }

    private static void editNotification(ServerCommandSource source, String uuid, String whatToEdit, String newValue) {
        JsonObject configObject = NotificationConfig.loadConfig();
        JsonArray notificationsArray = configObject.getAsJsonArray("notifications");

        for (int i = 0; i < notificationsArray.size(); i++) {
            JsonObject notification = notificationsArray.get(i).getAsJsonObject();
            if (notification.get("name").getAsString().equals(uuid)) {
                switch (whatToEdit.toLowerCase()) {
                    case "friendly_name":
                        notification.addProperty("friendly_name", newValue);
                        break;
                    case "type":
                        notification.addProperty("type", newValue);
                        break;
                    case "sound_namespace":
                        notification.addProperty("sound_namespace", newValue);
                        break;
                    case "sound_path":
                        notification.addProperty("sound_path", newValue);
                        break;
                    case "namespace":
                        notification.addProperty("namespace", newValue);
                        break;
                    case "texture":
                        notification.addProperty("texture", newValue);
                        break;
                    case "width":
                        notification.addProperty("width", Integer.parseInt(newValue));
                        break;
                    case "height":
                        notification.addProperty("height", Integer.parseInt(newValue));
                        break;
                    case "dismiss_message":
                        notification.addProperty("dismiss_message", Boolean.parseBoolean(newValue));
                        break;
                    case "alwaysshow":
                        notification.addProperty("alwaysShow", Boolean.parseBoolean(newValue));
                        break;
                    default:
                        source.sendError(Text.of("Invalid property to edit."));
                        return;
                }

                NotificationConfig.saveConfig(configObject);
                source.sendFeedback(Text.literal("Notification with UUID " + uuid + " edited successfully.")
                        .formatted(Formatting.GREEN), true);
                return;
            }
        }
        source.sendFeedback(Text.literal("Notification with UUID " + uuid + " not found.").formatted(Formatting.RED),
                true);
    }
}