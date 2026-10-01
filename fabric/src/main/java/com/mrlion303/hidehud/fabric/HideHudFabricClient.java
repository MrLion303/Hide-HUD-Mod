package com.mrlion303.hidehud.fabric;

import com.mrlion303.hidehud.HideHudState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class HideHudFabricClient implements ClientModInitializer {
    @Override public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(HideHudFabric.CHANNEL, (client, handler, buf, responseSender) -> {
            boolean hide = buf.readBoolean();
            int seconds = buf.readVarInt();
            boolean hand = buf.readBoolean();
            client.execute(() -> HideHudState.set(hide, seconds, hand));
        });
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            if (HideHudState.hidden && !HideHudState.fullyHidden()) {
                int a = Math.round(HideHudState.progress() * 255f);
                if (a > 0) {
                    graphics.fill(0, 0, clientWidth(graphics), clientHeight(graphics), a << 24);
                }
            }
        });
    }

    private static int clientWidth(net.minecraft.client.gui.DrawContext graphics) {
        return net.minecraft.client.Minecraft.getInstance().getWindow().getGuiScaledWidth();
    }
    private static int clientHeight(net.minecraft.client.gui.DrawContext graphics) {
        return net.minecraft.client.Minecraft.getInstance().getWindow().getGuiScaledHeight();
    }
}
