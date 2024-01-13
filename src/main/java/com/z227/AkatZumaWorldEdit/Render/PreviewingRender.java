package com.z227.AkatZumaWorldEdit.Render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;



@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber
public class PreviewingRender {
    private static LocalPlayer player;

    private static AABB getAABB(BlockPos pStart, BlockPos pEnd) {
        return new AABB(pStart, pEnd);
    }

//    @SubscribeEvent
//    //用于获取玩家的临时方法，待修改
//    public static void getPlayer(EntityJoinLevelEvent event) {
//
//        if (event.getEntity() instanceof Player){
//           player = event.getEntity();
//
//        }
//    }

    //玩家加入服务器，仅在客户端触发
    @SubscribeEvent
    public static void Logout(ClientPlayerNetworkEvent.LoggingIn event) {
        player = event.getPlayer();
        String playerName = player.getName().getString();
//        System.out.println("登录："+ playerName);
        AkatZumaWorldEdit.PlayerWEMap.put(player.getUUID(), new PlayerMapData(playerName));
    }

    @SubscribeEvent
    public static void onRenderLastEvent(RenderLevelStageEvent event) {
        if(player == null) return;
        //获取方块坐标
        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());


        BlockPos pStart= PMD.getPos1(), pEnd = PMD.getPos2();

        if (pStart != null && pEnd != null) {

            //关闭深度检测
            RenderSystem.disableDepthTest();

            //获取标线AABB
            BlockPos p1 = new BlockPos(Math.min(pStart.getX(), pEnd.getX()), Math.min(pStart.getY(), pEnd.getY()), Math.min(pStart.getZ(), pEnd.getZ()));
            BlockPos p2 = new BlockPos(Math.max(pStart.getX(), pEnd.getX()) + 1, Math.max(pStart.getY(), pEnd.getY()) + 1, Math.max(pStart.getZ(), pEnd.getZ()) + 1);
            AABB aabb = getAABB(p1, p2);

            //以下两项不知道做什么的，但是最好不要动 //builder
            VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
            PoseStack stack = event.getPoseStack();

            //坐标变换
            Vec3 camvec = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            double s0 = camvec.x;
            double s1 = camvec.y;
            double s2 = camvec.z;

            //渲染
            stack.pushPose();
//            stack.translate( - s0,  - s1,  - s2);
            stack.translate( - camvec.x,  - camvec.y,  - camvec.z);
            LevelRenderer.renderLineBox(stack, vertexConsumer, aabb, 255, 90, 90, 1);
            RenderSystem.enableDepthTest();
            stack.popPose();
            stack.clear();
        }
    }
}