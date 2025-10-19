package com.z227.ImGuiRender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.z227.ImGuiRender.render.LeftMenuRender;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;

public class EditModeLevelRender {
    static EditModeData editModeData = EditModeData.getInstance();

    public static void render(RenderLevelStageEvent event){
        Player player = Minecraft.getInstance().player;
        if (player == null)return;


        for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
            if (entity instanceof LivingEntity) {
                PoseStack poseStack = event.getPoseStack();
                MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
                Camera camera = event.getCamera();

                // 应用相机变换
                Vec3 cameraPos = camera.getPosition();
                poseStack.pushPose();

                poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);


//                SelectionModeRender.renderCone(poseStack, 1.0f, 2.0f, 32, 1.0f, 0.0f, 0.0f, 1.0f);
//                SelectionModeRender.renderCyl(poseStack);

                poseStack.popPose();


                LeftMenuRender.currentMenu.levelRender(poseStack, event.getProjectionMatrix(), cameraPos);


                // 结束批处理
                bufferSource.endBatch();
            }

        }


    }
}
