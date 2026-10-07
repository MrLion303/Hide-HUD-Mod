package com.mrlion303.hidehud.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrlion303.hidehud.HideHudState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.negative.negativeutils.WaypointRenderer", remap = false)
public class NegativeUtilsWaypointMixin {
    @Inject(method = "onRenderHud", at = @At("HEAD"), cancellable = true, require = 0)
    private static void hideHud(CallbackInfo ci) {
        if (HideHudState.fullyHidden()) {
            ci.cancel();
            return;
        }
        RenderSystem.setShaderColor(1f, 1f, 1f, HideHudState.visibilityAlpha());
    }

    @Inject(method = "onRenderHud", at = @At("RETURN"), require = 0)
    private static void restoreHudColor(CallbackInfo ci) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}
