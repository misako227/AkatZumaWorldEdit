package com.z227.AkatZumaWorldEdit.Event.ImguiMethod;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.z227.ImGuiRender.EditModeData;
import com.z227.ImGuiRender.EditModeUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.text.NumberFormat;
import java.util.Locale;

public class ImguiMouseEvent {

    public static BlockPos pos1 = null;
    public static BlockPos pos2 = null;



    public static void getBlockAtMouse() {
        Minecraft mc = Minecraft.getInstance();
        GameRenderer gameRenderer = mc.gameRenderer;
        Player player = mc.player;
//        if (player == null) return null;


        // 获取窗口尺寸
        Window window = mc.getWindow();
        int windowWidth = window.getWidth();
        int windowHeight = window.getHeight();
        double mouseX = mc.mouseHandler.xpos();
        double mouseY = mc.mouseHandler.ypos();

        // 转换为NDC坐标 [-1, 1]
        float ndcX = (float) ((2.0f * mouseX) / windowWidth - 1.0f);
        float ndcY = (float) (1.0f - (2.0f * mouseY) / windowHeight);

        // 获取投影和模型视图矩阵
        Matrix4f projectionMatrix = EditModeData.projectionMatrix;
        Matrix4f modelViewMatrix = RenderSystem.getModelViewMatrix();


        // 计算逆矩阵
        Matrix4f inverseProjModelView = new Matrix4f();
        inverseProjModelView.set(projectionMatrix);
        inverseProjModelView.mul(modelViewMatrix);
        inverseProjModelView.invert();

        // 近平面和远平面点（Z值为-1和1）
        Vector4f nearVec = new Vector4f(ndcX, ndcY, -1.0f, 1.0f);
        Vector4f farVec = new Vector4f(ndcX, ndcY, 1.0f, 1.0f);
        nearVec.mul(inverseProjModelView);
        farVec.mul(inverseProjModelView);

        // 转换为三维向量
        Vector3d start = new Vector3d(nearVec.x() / nearVec.w(), nearVec.y() / nearVec.w(), nearVec.z() / nearVec.w());
        Vector3d end = new Vector3d(farVec.x() / farVec.w(), farVec.y() / farVec.w(), farVec.z() / farVec.w());

        System.out.println("start:" + start.toString());
        System.out.println("end:" + end.toString());


        // 计算视线方向
        Vector3d direction = end.sub(start).normalize();
        System.out.println("direction:" + direction.toString());
        System.out.println("---------");

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        EditModeUI.start = start.toString(formatter);
        EditModeUI.end = String.valueOf(end);
        EditModeUI.direction = String.valueOf(direction);

        // 玩家眼睛位置
//        Vec3 eyesPos = player.getEyePosition(1.0f);

        // 最大距离（例如玩家的到达距离）
        double reachDistance = 100;
//        Vector3d endPos = eyesPos.add(direction.scale(reachDistance));

        // 创建射线追踪上下文

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


        Player player = mc.player;
//        if (player == null) return Vec3.ZERO;

        // 获取视口尺寸
        int viewportWidth = mc.getWindow().getWidth();
        int viewportHeight = mc.getWindow().getHeight();

        // 转换到NDC坐标（-1到1的坐标系）
        double ndcX = (screenX / viewportWidth) * 2.0 - 1.0;
//        double ndcY = 1.0 - (screenY / viewportHeight) * 2.0;
        double ndcY = (screenY / viewportHeight) * 2.0 - 1.0;
        System.out.println("ndcX:" + ndcX + " ndcY:" + ndcY);

                // 获取当前矩阵（需要确保在渲染阶段调用）
//        Matrix4f projectionMatrix = EditModeData.projectionMatrix;
        Matrix4f projectionMatrix = new Matrix4f(EditModeData.projectionMatrix);
        projectionMatrix.invert();
//        Matrix4f modelViewMatrix = RenderSystem.getModelViewMatrix();

        // 计算逆变换矩阵
//        Matrix4f inverseMatrix = new Matrix4f();
//        projectionMatrix.mul(modelViewMatrix, inverseMatrix).invert();

        // 执行坐标变换
        Vector4f clipCoords = new Vector4f((float) ndcX, (float) ndcY, 0.0f, 1.0f);
        clipCoords.mul(projectionMatrix);

        // 标准化并获取方向向量
//        if (eyeCoords.w == 0) return ;
        Vector3f viewVector = new Vector3f(-clipCoords.x(), -clipCoords.y(), -clipCoords.z()).normalize();
//        viewVector.normalize();
        Vector3f loockatVec3 = viewVector.rotate(EditModeData.cameraRotation).normalize();

        // 计算射线起点（玩家眼睛位置）
        Vec3 startPos = player.getEyePosition();

        // 计算射线终点（最大距离1000）
        Vec3 endPos = startPos.add(
                loockatVec3.x() * 200,
                loockatVec3.y() * 200,
                loockatVec3.z() * 200
        );

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        EditModeUI.start = (int)startPos.x + " " + (int)startPos.y + " " + (int)startPos.z;
        EditModeUI.end = (int)endPos.x + " " + (int)endPos.y + " " + (int)endPos.z;

        pos1 = BlockPos.containing(startPos);
        pos2 = BlockPos.containing(endPos);

//        // 执行射线追踪
        HitResult hitResult = player.level().clip(
                new ClipContext(startPos, endPos,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
                        player
                )
        );

        if(hitResult.getType() == HitResult.Type.BLOCK){
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = player.level().getBlockState(blockPos);
            System.out.println(blockState.toString());
        }
//
//        return hitResult.getType() == HitResult.Type.MISS ?
//                endPos :
//                hitResult.getLocation();
    }

}
