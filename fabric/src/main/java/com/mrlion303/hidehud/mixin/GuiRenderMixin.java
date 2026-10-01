package com.mrlion303.hidehud.mixin;

import com.mrlion303.hidehud.HideHudState;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiRenderMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void hideHud(GuiGraphics graphics, float tickDelta, CallbackInfo ci) {
        if (HideHudState.fullyHidden()) ci.cancel();
    }
}
