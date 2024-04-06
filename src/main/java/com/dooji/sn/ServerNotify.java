package com.dooji.sn;

import com.dooji.sn.commands.NotificationCommands;
import com.dooji.sn.network.NotificationConfig;
import com.dooji.sn.network.NotificationPacket;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerNotify implements ModInitializer {
	@Override
	public void onInitialize() {

		NotificationConfig.initConfig();

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {

			ServerPlayConnectionEvents.JOIN.register((handler, sender, server1) -> {

				JsonObject config = NotificationConfig.loadConfig();
				if (config != null) {
					com.google.gson.JsonArray notifications = config.getAsJsonArray("notifications");
					for (int i = 0; i < notifications.size(); i++) {
						JsonObject notification = notifications.get(i).getAsJsonObject();
						String name = notification.get("name").getAsString();
						String type = notification.get("type").getAsString();
						String sound_namespace = notification.get("sound_namespace").getAsString();
						String sound_path = notification.get("sound_path").getAsString();
						if (type.equals("texture")) {
							String namespace = notification.get("namespace").getAsString();
							String texture = notification.get("texture").getAsString();
							int width = notification.get("width").getAsInt();
							int height = notification.get("height").getAsInt();
							boolean dismiss_message = notification.get("dismiss_message").getAsBoolean();
							boolean alwaysShow = notification.get("alwaysShow").getAsBoolean();

							NotificationPacket.send((ServerPlayerEntity) handler.player, name, type, sound_namespace,
									sound_path, namespace, texture, width, height, dismiss_message, alwaysShow);
						} else if (type.equals("text")) {
							String message = notification.get("message").getAsString();
							boolean dismiss_button = notification.get("dismiss_button").getAsBoolean();
							boolean dismiss_message = notification.get("dismiss_message").getAsBoolean();
							boolean alwaysShow = notification.get("alwaysShow").getAsBoolean();

							NotificationPacket.send((ServerPlayerEntity) handler.player, name, type, sound_namespace,
									sound_path, message, dismiss_button, dismiss_message, alwaysShow);
						} else if (type.equals("url")) {
							String url = notification.get("url").getAsString();
							int width = notification.get("width").getAsInt();
							int height = notification.get("height").getAsInt();
							boolean dismiss_message = notification.get("dismiss_message").getAsBoolean();
							boolean alwaysShow = notification.get("alwaysShow").getAsBoolean();

							NotificationPacket.send((ServerPlayerEntity) handler.player, name, type, sound_namespace,
									sound_path, url, width, height, dismiss_message, alwaysShow);
						}
					}
				}
			});
		});
		ServerLifecycleEvents.SERVER_STARTED.register(this::registerCommands);
	}

	private void registerCommands(MinecraftServer server) {
		CommandDispatcher<ServerCommandSource> dispatcher = server.getCommandManager().getDispatcher();
		NotificationCommands.register(dispatcher);
	}
}