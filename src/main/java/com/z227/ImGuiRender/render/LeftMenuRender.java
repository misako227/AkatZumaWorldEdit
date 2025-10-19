package com.z227.ImGuiRender.render;

import com.z227.ImGuiRender.mode.ModeBase;
import com.z227.ImGuiRender.mode.SelectionMode;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiWindowFlags;

import java.util.HashMap;
import java.util.Map;

public class LeftMenuRender {

    public static HashMap <String, ModeBase> MenuMap = new HashMap<>();
    public static ModeBase currentMenu = null;
    private static double updateInterval = 0.05;
    private static double lastUpdateTime = 0;

    static {
        MenuMap.put("selection", new SelectionMode());
        MenuMap.put("selection2", new SelectionMode());
        currentMenu = MenuMap.get("selection");
    }
    public static void render(){
        ImGui.begin("RightMenu", ImGuiWindowFlags.AlwaysUseWindowPadding); //, ImGuiWindowFlags.NoCollapse
//        ImGui.getIO().getWantSaveIniSettings();

        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImGui.text("Size: " + windowSize.x + "x" + windowSize.y);

        int buttonIndex = 0;
        int i = 0;
        for (Map.Entry<String, ModeBase> entry : MenuMap.entrySet()){
            String key = entry.getKey();
            ModeBase modeBase = entry.getValue();
            //确保每个按钮等控件拥有独立的ID，防止ID冲突。
            ImGui.pushID(i);
            if(ImGui.button(key,50,50)){
                currentMenu = modeBase;

            }
            //鼠标悬浮提示
            if (ImGui.isItemHovered()) {
                ImGui.setTooltip(modeBase.hoverText);
            }

            // 判断是否需要在同一行显示按钮，基于按钮索引和窗口大小的计算
            // 如果当前按钮位置加上偏移量仍在窗口范围内，且按钮数量未超过10个，则继续在同一行显示下一个按钮
            if(100*(buttonIndex+1) + 10 * buttonIndex < windowSize.x && i < 10){
                ImGui.sameLine();
                buttonIndex++;
            }else{
                // 否则重置按钮索引，开始新的一行
                buttonIndex = 1;
            }

            i++;

            ImGui.popID();

        }
        ImGui.separator();
        ImGui.menuItem("功能");
        ImGui.beginChild("Child1",windowSize.x,200);

        if(currentMenu != null){
            currentMenu.render();

            double currentTime = ImGui.getTime();
            double deltaTime = currentTime - lastUpdateTime;
            if (deltaTime > updateInterval) {
                lastUpdateTime = currentTime;
                currentMenu.mouseUpdate();
            }

        }
        ImGui.endChild();



        ImGui.end();

    }

    //    public static void updateBlue(){
//        blue = new ImVec4(blueList[0]/255f, blueList[1]/255f, blueList[2]/255f, blueList[3]/255f);
//    }
    public static ImVec4 floatToImVec4(float[] list){
        return new ImVec4(list[0], list[1], list[2], list[3]);
    }

    // 帮助提示
    public static void HelpMarker(String desc){
        ImGui.sameLine();
        ImGui.textDisabled("(?)");
        if (ImGui.isItemHovered())
            ImGui.setTooltip(desc);

    }


    public static ModeBase getCurrentMenu() {
        return currentMenu;
    }

    public static void setCurrentMenu(ModeBase currentMenu) {
        LeftMenuRender.currentMenu = currentMenu;
    }
}
