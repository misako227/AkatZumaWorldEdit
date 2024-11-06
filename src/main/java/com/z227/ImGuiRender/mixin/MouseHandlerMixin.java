package com.z227.ImGuiRender.mixin;

import com.z227.ImGuiRender.EditModeData;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Inject(method = "onPress", at = @At(value = "HEAD"), cancellable = true)
    public void onPress(long pWindowPointer, int pButton, int pAction, int pModifiers, CallbackInfo ci) {
        if(EditModeData.getEditMode()){
            ci.cancel();
        }
    }

    @Inject(method = "grabMouse", at = @At(value = "HEAD"), cancellable = true)
    public void grabMouse(CallbackInfo ci) {
        if(EditModeData.getEditMode()){
            ci.cancel();
        }
    }
}
