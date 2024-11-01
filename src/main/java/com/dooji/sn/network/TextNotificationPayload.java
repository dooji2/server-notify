package com.dooji.sn.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TextNotificationPayload(
        String name,
        String type,
        String soundNamespace,
        String soundPath,
        String message,
        boolean dismissButton,
        boolean dismissMessage,
        boolean alwaysShow
) implements CustomPayload {

    public static final CustomPayload.Id<TextNotificationPayload> ID =
            new CustomPayload.Id<>(Identifier.of("server-notify", "text_notification"));

    public static final PacketCodec<RegistryByteBuf, TextNotificationPayload> CODEC = new PacketCodec<>() {
        @Override
        public TextNotificationPayload decode(RegistryByteBuf buf) {
            String name = buf.readString(Short.MAX_VALUE);
            String type = buf.readString(Short.MAX_VALUE);
            String soundNamespace = buf.readString(Short.MAX_VALUE);
            String soundPath = buf.readString(Short.MAX_VALUE);
            String message = buf.readString(Short.MAX_VALUE);
            boolean dismissButton = buf.readBoolean();
            boolean dismissMessage = buf.readBoolean();
            boolean alwaysShow = buf.readBoolean();

            return new TextNotificationPayload(name, type, soundNamespace, soundPath, message, dismissButton, dismissMessage, alwaysShow);
        }

        @Override
        public void encode(RegistryByteBuf buf, TextNotificationPayload payload) {
            buf.writeString(payload.name(), Short.MAX_VALUE);
            buf.writeString(payload.type(), Short.MAX_VALUE);
            buf.writeString(payload.soundNamespace(), Short.MAX_VALUE);
            buf.writeString(payload.soundPath(), Short.MAX_VALUE);
            buf.writeString(payload.message(), Short.MAX_VALUE);
            buf.writeBoolean(payload.dismissButton());
            buf.writeBoolean(payload.dismissMessage());
            buf.writeBoolean(payload.alwaysShow());
        }
    };

    @Override
    public CustomPayload.Id<TextNotificationPayload> getId() {
        return ID;
    }
}