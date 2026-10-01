package com.mrlion303.hidehud.mixin;

import com.mrlion303.hidehud.HideHudState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class GuiRenderMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void hideHud(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (HideHudState.fullyHidden()) {
            ci.cancel();
        }
    }
}
