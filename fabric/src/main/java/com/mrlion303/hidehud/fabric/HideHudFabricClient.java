package com.mrlion303.hidehud.fabric;

import com.mrlion303.hidehud.HideHudState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class HideHudFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(HideHudFabric.CHANNEL,
            (client, handler, buf, responseSender) -> {
                boolean hide = buf.readBoolean();
                int seconds = buf.readVarInt();
                boolean hand = buf.readBoolean();
                client.execute(() -> HideHudState.set(hide, seconds, hand));
            });

        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            if (HideHudState.hidden && !HideHudState.fullyHidden()) {
                int alpha = Math.round(HideHudState.progress() * 255f);
                if (alpha > 0) {
                    graphics.fill(0, 0, clientWidth(), clientHeight(), alpha << 24);
                }
            }
        });
    }

    private static int clientWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledWidth();
    }

    private static int clientHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
    }
}
