package com.z227.ImGuiRender;


import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class EditModeData {
    static boolean editMode = false;
    public static Matrix4f projectionMatrix;
    public static Quaternionf cameraRotation;

    public static boolean getEditMode() {
        return editMode;
    }

    public static void setEditMode() {
        editMode = !editMode;
        if(editMode){
            Minecraft.getInstance().mouseHandler.releaseMouse();
            Minecraft.getInstance().setWindowActive(true);
        }else{
            Minecraft.getInstance().mouseHandler.grabMouse();
        }
    }

    public static void setEditMode(boolean bool) {
        editMode = bool;

    }
}
