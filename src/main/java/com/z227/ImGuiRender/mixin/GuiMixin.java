package com.z227.ImGuiRender.mixin;

import com.z227.ImGuiRender.EditModeData;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "renderCrosshair", at = @At(value = "HEAD"), cancellable = true)
    public void renderCrosshair(GuiGraphics pGuiGraphics, CallbackInfo ci) {
        if(EditModeData.getEditMode()){
            ci.cancel();
        }
    }

}
