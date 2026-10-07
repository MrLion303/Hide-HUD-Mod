package com.mrlion303.hidehud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HideHudForgeClient {
    @SubscribeEvent
    public void onHudPre(RenderGuiEvent.Pre event) {
        if (HideHudState.fullyHidden()) {
            event.setCanceled(true);
            return;
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, HideHudState.visibilityAlpha());
    }

    @SubscribeEvent
    public void onOverlayPre(RenderGuiOverlayEvent.Pre event) {
        if (HideHudState.fullyHidden()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onHudPost(RenderGuiEvent.Post event) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    @SubscribeEvent
    public void onHand(RenderHandEvent event) {
        if (HideHudState.hideHand) event.setCanceled(true);
    }
}
