package com.dooji.sn.network;

public class NotificationData {
    private final String name;
    private final String type;
    private final String sound_namespace;
    private final String sound_path;
    private String message = null;
    private String namespace = null;
    private String texture = null;
    private String url = null;
    private int width = 0;
    private int height = 0;
    private boolean dismiss_button = false;
    private final boolean dismiss_message;
    private final boolean alwaysShow;

    public NotificationData(String name, String type, String sound_namespace, String sound_path, String namespace,
            String texture, int width, int height, boolean dismiss_message, boolean alwaysShow) {
        this.name = name;
        this.type = type;
        this.sound_namespace = sound_namespace;
        this.sound_path = sound_path;
        this.namespace = namespace;
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.dismiss_message = dismiss_message;
        this.alwaysShow = alwaysShow;
    }

    public NotificationData(String name, String type, String sound_namespace, String sound_path, String message,
            boolean dismiss_button, boolean dismiss_message, boolean alwaysShow) {
        this.name = name;
        this.type = type;
        this.sound_namespace = sound_namespace;
        this.sound_path = sound_path;
        this.message = message;
        this.dismiss_button = dismiss_button;
        this.dismiss_message = dismiss_message;
        this.alwaysShow = alwaysShow;
    }

    public NotificationData(String name, String type, String sound_namespace, String sound_path, String url, int width,
            int height, boolean dismiss_message, boolean alwaysShow) {
        this.name = name;
        this.type = type;
        this.sound_namespace = sound_namespace;
        this.sound_path = sound_path;
        this.url = url;
        this.width = width;
        this.height = height;
        this.dismiss_message = dismiss_message;
        this.alwaysShow = alwaysShow;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSoundNamespace() {
        return sound_namespace;
    }

    public String getSoundPath() {
        return sound_path;
    }

    public String getURL() {
        return url;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getTexture() {
        return texture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isDismissShow() {
        return dismiss_message;
    }

    public boolean isAlwaysShow() {
        return alwaysShow;
    }

    public String getMessage() {
        return message;
    }

    public boolean isDismissButtonShow() {
        return dismiss_button;
    }
}
