package com.z227.AkatZumaWorldEdit.Event.ImguiMethod;

import com.mojang.blaze3d.platform.Window;
import com.z227.ImGuiRender.EditModeUI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

import java.text.NumberFormat;
import java.util.Locale;

public class screenToWorldPosRTS {


    /**
     * 获取玩家当前视线方向的单位向量
     *
     * @param MC Minecraft客户端实例，用于获取玩家信息
     * @return Vector3d 表示玩家视线方向的3D向量，当玩家不存在时返回零向量
     *         向量分量说明：
     *         x: 水平面横向分量（左右方向）
     *         y: 垂直分量（上下方向）
     *         z: 水平面纵向分量（前后方向）
     */
    public static Vector3d getPlayerLookVector(Minecraft MC) {
        // 检查玩家实体是否存在
        if (MC.player == null)
            return new Vector3d(0,0,0);

        // 将玩家旋转角度转换为弧度（偏航角Yaw和俯仰角Pitch）
        float a = (float) Math.toRadians(MC.player.getYRot());
        float b = (float) Math.toRadians(MC.player.getXRot());

        // 计算三维空间中的方向向量：
        // x = -cos(pitch) * sin(yaw)
        // y = -sin(pitch)
        // z = cos(pitch) * cos(yaw)
        return new Vector3d(-Mth.cos(b) * Mth.sin(a), -Mth.sin(b), Mth.cos(b) * Mth.cos(a));
    }


    /**
     * 二维坐标旋转方法
     *
     * 该方法实现了将给定的二维坐标(x,y)绕坐标原点(0,0)逆时针旋转指定角度
     * 主要用于将屏幕坐标转换为世界坐标时的坐标变换
     *
     * @param x   原始x坐标值
     * @param y   原始y坐标值
     * @param deg 旋转角度(单位：度)，正值表示逆时针旋转
     * @return    返回包含旋转后新坐标的Vec2对象
     */
    public static Vec2 rotateCoords(float x, float y, double deg) {
        // 将角度从度转换为弧度(因为三角函数需要弧度制参数)
        float xRotRads = (float) Math.toRadians(deg);

        // 应用二维旋转矩阵公式：
        // 新x坐标 = x*cosθ - y*sinθ
        // 新y坐标 = y*cosθ + x*sinθ
        float moveXRotated = (x * Mth.cos(xRotRads)) - (y * Mth.sin(xRotRads));
        float moveyRotated = (y * Mth.cos(xRotRads)) + (x * Mth.sin(xRotRads));

        // 返回旋转后的新坐标
        return new Vec2(moveXRotated, moveyRotated);
    }

    //RTS
    public static Vector3d screenPosToWorldPos(Minecraft MC, int mouseX, int mouseY) {
        // 如果玩家为空，则返回原点
        if (MC.player == null) {
            return new Vector3d(0,0,0);
        }
        // 获取窗口宽度和高度
        int winWidth = MC.getWindow().getGuiScaledWidth();
        int winHeight = MC.getWindow().getGuiScaledHeight();

        // at winHeight=240, zoom=10, screen is 20 blocks high, so PTB=240/20=24
        //在winHeight=240, zoom=10时，屏幕是20块高，所以PTB=240/20=24
//        float pixelsToBlocks = winHeight / OrthoviewClientEvents.getZoom();
        float pixelsToBlocks = winHeight / 30;

        // make mouse coordinate origin centre of screen使鼠标坐标原点为屏幕中心
        float x = (mouseX - (float) winWidth / 2) / pixelsToBlocks;
        float y = 0;
        float z = (mouseY - (float) winHeight / 2) / pixelsToBlocks;// / pixelsToBlocks;

//        double camRotYRads = Math.toRadians(OrthoviewClientEvents.getCamRotY());
        //将角度值从度（degrees）转换为弧度（radians）
        double camRotYRads = Math.toRadians(MC.player.getYRot());
        z = z / (float) (Math.sin(camRotYRads));

//        Vec2 XZRotated = MyMath.rotateCoords(x, z, OrthoviewClientEvents.getCamRotX());
        Vec2 XZRotated = rotateCoords(x, z, MC.player.getYRot());

        // for some reason position is off by some y coord so just move it down manually由于某些原因，位置偏离了y轴，所以只需手动向下移动它
        return new Vector3d(
                MC.player.xo - XZRotated.x,
                MC.player.yo + y + 1.5f,
                MC.player.zo - XZRotated.y
        );
    }

    public static Vector3d addVector3d(Vector3d vec, Vector3d unitVec, float scale) {
        Vector3d unitVecLocal = new Vector3d(0,0,0);
        unitVecLocal.set(unitVec);
        unitVecLocal.mul(scale);
        Vector3d vecLocal = new Vector3d(0,0,0);
        vecLocal.set(vec);
        vecLocal.add(unitVecLocal);
        return vecLocal;
    }
    public static void screenToWorld() {
        Minecraft mc = Minecraft.getInstance();
        // 获取窗口尺寸
        Window window = mc.getWindow();
        int windowWidth = window.getWidth();
        int windowHeight = window.getHeight();
        System.out.println("windowWidth:" + windowWidth + " windowHeight:" + windowHeight);
        double screenX = mc.mouseHandler.xpos();
        double screenY = mc.mouseHandler.ypos();


        Vec3 startPos = mc.player.getEyePosition();
        Vector3d centrePosd = screenPosToWorldPos(mc, (int) screenX, (int) screenY);

        Vector3d lookVector = getPlayerLookVector(mc);
        Vector3d cursorWorldPosNear = addVector3d(centrePosd, lookVector, -200);
        Vector3d cursorWorldPosFar = addVector3d(centrePosd, lookVector, 200);

        Vec3 vectorNear = new Vec3(cursorWorldPosNear.x, cursorWorldPosNear.y, cursorWorldPosNear.z);
        Vec3 vectorFar = new Vec3(cursorWorldPosFar.x, cursorWorldPosFar.y, cursorWorldPosFar.z);

//        HitResult hitResult = null;
//        hitResult = mc.level.clip(MC.level, new ClipContext(vectorNear, vectorFar, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, null));




        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        EditModeUI.start = vectorNear.toString();
        EditModeUI.end = vectorFar.toString();

//        pos1 = BlockPos.containing(vectorNear.x, vectorNear.y, vectorNear.z);
//        pos2 = BlockPos.containing(vectorFar.x, vectorFar.y, vectorFar.z);
    }
}
