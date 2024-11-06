package com.z227.ImGuiRender.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.z227.ImGuiRender.EditModeUI;
import com.z227.ImGuiRender.ImGuiInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = {"<init>"}, at = {@At("RETURN")})
    public void init(GameConfig gameConfig, CallbackInfo ci) {
        EditModeUI.init();
    }
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;blitToScreen(II)V", shift = At.Shift.AFTER))
    public void afterMainBlit(CallbackInfo ci) {
        if (!RenderSystem.isOnRenderThread()) return;

        ImGuiInit.onFrameRender();

    }


}
