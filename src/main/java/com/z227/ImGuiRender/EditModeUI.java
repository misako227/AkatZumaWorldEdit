package com.z227.ImGuiRender;


import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.ImGuiRender.render.LeftMenuRender;
import imgui.ImGui;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.z227.akatzumaimgui.ImGuiInit;


@OnlyIn(Dist.CLIENT)
public class EditModeUI {

    private static final ResourceLocation IMAGE_LOC = new ResourceLocation(AkatZumaWorldEdit.MODID, "textures/block/mmc_v0.5.png");


    public static void onFrameRender(){
        if(!EditModeData.isOpenEditMode()) return;
        ImGuiInit.imGuiGlfw.newFrame();
        ImGuiInit.imGuiGl3.newFrame();
        ImGui.newFrame();
        render();
        ImGui.render();
        ImGuiInit.endFrame();
    }

    public static void render(){
        LeftMenuRender.render();
    }




}
