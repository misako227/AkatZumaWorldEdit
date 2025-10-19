package com.z227.ImGuiRender.mixin;

import com.z227.AkatZumaWorldEdit.utilities.Util;
import com.z227.ImGuiRender.EditModeData;
import net.minecraft.client.MouseHandler;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Inject(method = "onPress", at = @At(value = "HEAD"), cancellable = true)
    public void onPress(long pWindowPointer, int pButton, int pAction, int pModifiers, CallbackInfo ci) {
        if(EditModeData.isOpenEditMode()){
            if(pButton != GLFW.GLFW_MOUSE_BUTTON_RIGHT){
                ci.cancel();
            }
        }
    }

    @Inject(method = "grabMouse", at = @At(value = "HEAD"), cancellable = true)
    public void grabMouse(CallbackInfo ci) {
        if(EditModeData.isOpenEditMode() && !Util.isDownMouse(GLFW.GLFW_MOUSE_BUTTON_RIGHT)){
            ci.cancel();
        }
    }

    @Inject(method = "onScroll", at = @At(value = "HEAD"), cancellable = true)
    public void onScroll(long pWindowPointer, double pXOffset, double pYOffset, CallbackInfo ci) {
        if(EditModeData.isOpenEditMode()){
            ci.cancel();
        }
    }
}
