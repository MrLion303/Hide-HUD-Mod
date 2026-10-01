package com.mrlion303.hidehud.mixin;

import com.mrlion303.hidehud.HideHudState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
    private void hideHand(PoseStack poseStack, Camera camera, float tickDelta, CallbackInfo ci) {
        if (HideHudState.hideHand) ci.cancel();
    }
}
