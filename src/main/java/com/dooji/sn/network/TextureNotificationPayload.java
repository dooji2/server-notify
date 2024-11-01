package com.dooji.sn.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record TextureNotificationPayload(
        String name,
        String type,
        String soundNamespace,
        String soundPath,
        String namespace,
        String texture,
        int width,
        int height,
        boolean dismissMessage,
        boolean alwaysShow
) implements CustomPayload {

    public static final CustomPayload.Id<TextureNotificationPayload> ID =
            new CustomPayload.Id<>(Identifier.of("server-notify", "texture_notification"));

    public static final PacketCodec<RegistryByteBuf, TextureNotificationPayload> CODEC = new PacketCodec<>() {
        @Override
        public TextureNotificationPayload decode(RegistryByteBuf buf) {
            String name = buf.readString(Short.MAX_VALUE);
            String type = buf.readString(Short.MAX_VALUE);
            String soundNamespace = buf.readString(Short.MAX_VALUE);
            String soundPath = buf.readString(Short.MAX_VALUE);
            String namespace = buf.readString(Short.MAX_VALUE);
            String texture = buf.readString(Short.MAX_VALUE);
            int width = buf.readInt();
            int height = buf.readInt();
            boolean dismissMessage = buf.readBoolean();
            boolean alwaysShow = buf.readBoolean();

            return new TextureNotificationPayload(name, type, soundNamespace, soundPath, namespace, texture, width, height, dismissMessage, alwaysShow);
        }

        @Override
        public void encode(RegistryByteBuf buf, TextureNotificationPayload payload) {
            buf.writeString(payload.name(), Short.MAX_VALUE);
            buf.writeString(payload.type(), Short.MAX_VALUE);
            buf.writeString(payload.soundNamespace(), Short.MAX_VALUE);
            buf.writeString(payload.soundPath(), Short.MAX_VALUE);
            buf.writeString(payload.namespace(), Short.MAX_VALUE);
            buf.writeString(payload.texture(), Short.MAX_VALUE);
            buf.writeInt(payload.width());
            buf.writeInt(payload.height());
            buf.writeBoolean(payload.dismissMessage());
            buf.writeBoolean(payload.alwaysShow());
        }
    };

    @Override
    public CustomPayload.Id<TextureNotificationPayload> getId() {
        return ID;
    }
}