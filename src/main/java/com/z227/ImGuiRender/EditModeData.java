package com.z227.ImGuiRender;


import com.z227.ImGuiRender.render.gizmo.Gizmo;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@OnlyIn(Dist.CLIENT)
public class EditModeData {
    static boolean OpenEditMode = false;
    public static Matrix4f projectionMatrix;
    public static Quaternionf cameraRotation;
    public static ExecutorService cachedThreadPool;
    public Gizmo gizmo;
    public BlockPos gizmoPos;
    public String mode;

    public BlockPos pos1;
    public BlockPos pos2;
    public BlockPos centerPos;
    public int posIndex = 2;  // 1: pos1, 2: pos2, 3: 中心点
    public boolean isSelection = false; // 是否已创建选区


    public EditModeData() {
//        gizmoPos = new BlockPos(0,0,0);
        cachedThreadPool  = Executors.newCachedThreadPool();
    }

    public static EditModeData instance;
    public static void init(){
        if (instance == null) {
            instance = new EditModeData();
        }
    }

    public static EditModeData getInstance() {
        return instance;
    }

    public static boolean isOpenEditMode() {
        return OpenEditMode;
    }

    public static void setOpenEditMode() {
        OpenEditMode = !OpenEditMode;
        if(OpenEditMode){
            Minecraft.getInstance().mouseHandler.releaseMouse();
            Minecraft.getInstance().setWindowActive(true);
        }else{
            Minecraft.getInstance().mouseHandler.grabMouse();
        }
    }

    public static void setOpenEditMode(boolean open) {
        OpenEditMode = open;

    }

    public static void submitTask(Runnable task) {
        cachedThreadPool.submit(task);
    }



    public Gizmo getGizmo() {
        return gizmo;
    }

    public void setGizmo(Gizmo gizmo) {
        this.gizmo = gizmo;
    }

    public BlockPos getPos1() {
        return pos1;
    }

    public void setPos1(BlockPos pos1) {
        this.pos1 = pos1;
    }

    public BlockPos getPos2() {
        return pos2;
    }

    public void setPos2(BlockPos pos2) {
        this.pos2 = pos2;
    }

    public BlockPos getCenterPos() {
        return centerPos;
    }

    public void setCenterPos(BlockPos centerPos) {
        this.centerPos = centerPos;
    }

    public BlockPos calculateCenterPos() {
        if (pos1 == null || pos2 == null) {
            return null;
        }

        int centerX = (pos1.getX() + pos2.getX()) / 2;
        int centerY = (pos1.getY() + pos2.getY()) / 2;
        int centerZ = (pos1.getZ() + pos2.getZ()) / 2;

        return new BlockPos(centerX, centerY, centerZ);
    }

    public void clear() {
        pos1 = null;
        pos2 = null;
        centerPos = null;
        posIndex = 2;
        isSelection = false;

    }


    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getPosIndex() {
        return posIndex;
    }

    public void setPosIndex(int posIndex) {
        this.posIndex = posIndex;
        switch (posIndex){
            case 1:
                gizmoPos = pos1;
                break;
            case 2:
                gizmoPos = pos2;
                break;
            case 3:
                gizmoPos = centerPos;
                break;
        }
    }

    public boolean isSelection() {
        return isSelection;
    }

    public void setSelection(boolean selection) {
        isSelection = selection;
    }

    public BlockPos getGizmoPos() {
        return gizmoPos;
    }

    public void setGizmoPos(BlockPos gizmoPos) {
        this.gizmoPos = gizmoPos;
    }
}
