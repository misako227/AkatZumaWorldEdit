package com.z227.ImGuiRender;


import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EditModeData {
    static boolean editMode = false;

    public static boolean getEditMode() {
        return editMode;
    }

    public static void setEditMode() {
        editMode = !editMode;
        if(editMode){
            Minecraft.getInstance().mouseHandler.releaseMouse();
        }else{
            Minecraft.getInstance().mouseHandler.grabMouse();
        }
    }

    public static void setEditMode(boolean bool) {
        editMode = bool;

    }
}
