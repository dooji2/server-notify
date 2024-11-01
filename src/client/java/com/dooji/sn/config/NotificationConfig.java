package com.dooji.sn.config;

import java.util.HashSet;
import java.util.Set;

public class NotificationConfig {
    private final Set<String> seenNotifications = new HashSet<>();

    public Set<String> getSeenNotifications() {
        return seenNotifications;
    }
}