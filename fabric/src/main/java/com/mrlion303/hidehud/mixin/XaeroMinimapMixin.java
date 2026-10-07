package com.mrlion303.hidehud.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrlion303.hidehud.HideHudState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "xaero.common.minimap.render.MinimapRenderer", remap = false)
public class XaeroMinimapMixin {
    @Inject(method = "renderMinimap", at = @At("HEAD"), cancellable = true, require = 0)
    private void hideHud(CallbackInfo ci) {
        if (HideHudState.fullyHidden()) {
            ci.cancel();
            return;
        }
        RenderSystem.setShaderColor(1f, 1f, 1f, HideHudState.visibilityAlpha());
    }

    @Inject(method = "renderMinimap", at = @At("RETURN"), require = 0)
    private void restoreHudColor(CallbackInfo ci) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}
