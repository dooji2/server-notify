package com.dooji.sn;

import net.fabricmc.api.ClientModInitializer;
import com.dooji.sn.network.ClientPacketHandler;

public class ServerNotifyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPacketHandler.register();
    }
}