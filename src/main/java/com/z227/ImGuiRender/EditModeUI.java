package com.z227.ImGuiRender;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EditModeUI {
    public static String start = "";
    public static String direction = "";
    public static String end = "";

    public static void init(){
        ImGuiInit.onGlfwInit(Minecraft.getInstance().getWindow().getWindow());
    }

    public static void render(){

        if(!EditModeData.getEditMode()) return;
        ImGui.begin("ImGuiRender", ImGuiWindowFlags.AlwaysUseWindowPadding);
//        ImGui.showDemoWindow();
        ImGui.text("奥术大师aasdasdasdasdasd");
        ImGui.text(start);
        ImGui.text(end);
        ImGui.text(direction);
        ImGui.button("按钮");
        ImGui.end();

    }


}
