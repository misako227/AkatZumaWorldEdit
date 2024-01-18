package com.z227.AkatZumaWorldEdit.Render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Items.WoodAxeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class PreviewingRender {

    @SubscribeEvent
    public static void onRenderLastEvent(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.isEmpty())
            return;

        if (!(heldItem.getItem() instanceof WoodAxeItem)) {
            return;
        }
        //获取方块坐标
        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
        BlockPos pStart= PMD.getPos1(), pEnd = PMD.getPos2();

        if (pStart != null && pEnd != null) {
            for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
                if (entity instanceof LivingEntity) {
                    //以下两项不知道做什么的，但是最好不要动 //builder
                    VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
                    PoseStack stack = event.getPoseStack();
                    DrawLineBox(vertexConsumer, stack, pStart, pEnd);
                    return;
                }
            }

        }
    }

    public static void DrawLineBox(VertexConsumer vertexConsumer, PoseStack stack,BlockPos pStart, BlockPos pEnd){
        //渲染
        //关闭深度检测
        RenderSystem.disableDepthTest();

        //获取标线AABB
        BlockPos p1 = new BlockPos(Math.min(pStart.getX(), pEnd.getX()), Math.min(pStart.getY(), pEnd.getY()), Math.min(pStart.getZ(), pEnd.getZ()));
        BlockPos p2 = new BlockPos(Math.max(pStart.getX(), pEnd.getX()) + 1, Math.max(pStart.getY(), pEnd.getY()) + 1, Math.max(pStart.getZ(), pEnd.getZ()) + 1);
        AABB aabb =  new AABB(p1, p2);

        //坐标变换
        Vec3 camvec = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        //渲染
        stack.pushPose();
        stack.translate( - camvec.x,  - camvec.y,  - camvec.z);
        LevelRenderer.renderLineBox(stack, vertexConsumer, aabb, 48, 1, 167, 1);
        LevelRenderer.renderLineBox(stack, vertexConsumer, pStart.getX(),pStart.getY(),pStart.getZ(),pStart.getX()+1,pStart.getY()+1,pStart.getZ()+1, 170, 1, 1, 1);
        LevelRenderer.renderLineBox(stack, vertexConsumer, pEnd.getX(),pEnd.getY(),pEnd.getZ(),pEnd.getX()+1,pEnd.getY()+1,pEnd.getZ()+1, 1, 170, 170, 1);
        stack.popPose();
        RenderSystem.enableDepthTest();
        stack.clear();

    }
}