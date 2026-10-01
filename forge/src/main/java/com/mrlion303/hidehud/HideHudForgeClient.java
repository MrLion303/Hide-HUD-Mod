package com.mrlion303.hidehud;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HideHudForgeClient {
    @SubscribeEvent
    public void onHudPre(RenderGuiEvent.Pre event) {
        if (HideHudState.fullyHidden()) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onOverlayPre(RenderGuiOverlayEvent.Pre event) {
        if (HideHudState.fullyHidden()) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onHudPost(RenderGuiEvent.Post event) {
        if (HideHudState.hidden && !HideHudState.fullyHidden()) {
            int a = Math.round(HideHudState.progress() * 255f);
            if (a > 0) {
                var g = event.getGuiGraphics();
                var w = Minecraft.getInstance().getWindow();
                g.fill(0, 0, w.getGuiScaledWidth(), w.getGuiScaledHeight(), a << 24);
            }
        }
    }

    @SubscribeEvent
    public void onHand(RenderHandEvent event) {
        if (HideHudState.hideHand) event.setCanceled(true);
    }
}
