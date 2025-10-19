package com.z227.ImGuiRender.util;

import com.z227.ImGuiRender.EditModeData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PosUtilImGui {
    public static Vec3 getPlayerPOVHitEndVec3(Player pPlayer, int distance) {
        float f = pPlayer.getXRot();
        float f1 = pPlayer.getYRot();
        Vec3 vec3 = pPlayer.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;

        return vec3.add((double) f6 * distance, (double) f5 * distance, (double) f7 * distance);
    }
    public static BlockHitResult getPlayerPOVHitResult(Player player, int distance){

        Level level = player.level();
        Vec3 toEnd = getPlayerPOVHitEndVec3(player, distance);
        return level.clip(new ClipContext(player.getEyePosition(), toEnd, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

    }
    public static BlockHitResult getPlayerPOVHitResult(Player player){
        return getPlayerPOVHitResult(player, 120);
    }


    public static HitResult getScreenToWorldPos() {
        Minecraft mc = Minecraft.getInstance();

        double screenX = mc.mouseHandler.xpos();
        double screenY = mc.mouseHandler.ypos();


        Player player = mc.player;
        // 获取视口尺寸
        int viewportWidth = mc.getWindow().getWidth();
        int viewportHeight = mc.getWindow().getHeight();

        // 转换到NDC坐标（-1到1的坐标系）
        double ndcX = (screenX / viewportWidth) * 2.0 - 1.0;
//        double ndcY = 1.0 - (screenY / viewportHeight) * 2.0;
        double ndcY = (screenY / viewportHeight) * 2.0 - 1.0;
//        System.out.println("ndcX:" + ndcX + " ndcY:" + ndcY);

        // 获取当前矩阵（需要确保在渲染阶段调用）
        Matrix4f projectionMatrix = new Matrix4f(EditModeData.projectionMatrix);
        projectionMatrix.invert();

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

//        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
//        EditModeUI.start = (int)startPos.x + " " + (int)startPos.y + " " + (int)startPos.z;
//        EditModeUI.end = (int)endPos.x + " " + (int)endPos.y + " " + (int)endPos.z;
//
//        pos1 = BlockPos.containing(startPos);
//        pos2 = BlockPos.containing(endPos);

//        // 执行射线追踪
        HitResult hitResult = player.level().clip(
                new ClipContext(startPos, endPos,
                        ClipContext.Block.OUTLINE,
                        ClipContext.Fluid.NONE,
                        player
                )
        );

        if(hitResult.getType() == HitResult.Type.BLOCK){
//            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
//            BlockPos blockPos = blockHitResult.getBlockPos();
//            BlockState blockState = player.level().getBlockState(blockPos);
//            System.out.println(blockPos.toString());
//            System.out.println(blockState.toString());
            return hitResult;
        }

        return null;

    }


    public static double calcDistance(Vec3i pos1, Vec3i pos2){
        int x = Math.abs(pos1.getX() - pos2.getX()) + 1;
        int y = Math.abs(pos1.getY() - pos2.getY()) + 1;
        int z = Math.abs(pos1.getZ() - pos2.getZ()) + 1;
        return  Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

}
