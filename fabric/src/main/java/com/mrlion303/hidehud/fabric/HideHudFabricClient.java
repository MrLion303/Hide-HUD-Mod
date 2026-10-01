package com.mrlion303.hidehud.fabric;

import com.mrlion303.hidehud.HideHudState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class HideHudFabricClient implements ClientModInitializer {
    @Override public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(HideHudFabric.CHANNEL, (client, handler, buf, responseSender) -> {
            boolean hide = buf.readBoolean();
            int seconds = buf.readVarInt();
            boolean hand = buf.readBoolean();
            client.execute(() -> HideHudState.set(hide, seconds, hand));
        });
    }
}
