package com.z227.AkatZumaWorldEdit.Render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.PosDirection;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.Items.ProjectorItem;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;


@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
@OnlyIn(Dist.CLIENT)
public class PreviewingRender {

    @SubscribeEvent
    public static void onRenderLastEvent(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            return;
        }
        Player player = Minecraft.getInstance().player;
        if (player == null)return;

        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.isEmpty())return;

//        if (!(heldItem.getItem() instanceof WoodAxeItem)) {
//            return;
//        }

        //获取方块坐标
        PlayerMapData PMD = Util.getPMD(player);
        Item item = heldItem.getItem();
        if(AkatZumaWorldEdit.USEITEM.get(item) == null && PMD.getBrushMap().get(item) == null)return;


//        if(PMD==null)return;
        BlockPos pStart= PMD.getPos1(), pEnd = PMD.getPos2();

        CopyBlock copyBlock = PMD.getCopyBlockClient();



        if (pStart != null && pEnd != null) {
            for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
                if (entity instanceof LivingEntity) {
                    //以下两项不知道做什么的，但是最好不要动 //builder
                    VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
                    PoseStack stack = event.getPoseStack();
                    drawLineBox(vertexConsumer, stack, pStart, pEnd);
                    if(item instanceof ProjectorItem){
                        drawCopyBlock( copyBlock, stack, player);
                        return;
                    }
//                    //渲染笔刷
//                    if(PMD.getBrushMap().get(item)!= null){
//                        RenderSphere.render(stack, item, PMD);
//                    }

                    return;
                }
            }

        }
    }

    public static void drawLineBox(VertexConsumer vertexConsumer, PoseStack stack,BlockPos pStart, BlockPos pEnd){


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


    }

    public static void drawBlock(PoseStack stack,BlockPos pos, BlockState blockState){
        //关闭深度检测
        RenderSystem.disableDepthTest();
        Vec3 camvec = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        stack.pushPose();
        stack.translate(pos.getX()-camvec.x, pos.getY()-camvec.y, pos.getZ()-camvec.z);

//        Minecraft mc = Minecraft.getInstance();

//        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
//        ModelData modelData = Minecraft.getInstance().level.getModelDataManager().getAt(pos);

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
                blockState,
                stack,
                Minecraft.getInstance().renderBuffers().bufferSource(),
//                15728640,
                15728880,

//                OverlayTexture.NO_OVERLAY,
                OverlayTexture.pack(3,10),
//                OverlayTexture.NO_OVERLAY,

                ModelData.EMPTY,
//                modelData,

                null
//                RenderType.cutout()
        );

        RenderSystem.enableCull();
        stack.popPose();

        RenderSystem.enableDepthTest();
    }




    public static void drawCopyBlock(CopyBlock copyBlock,PoseStack stack, Player player){
        if(copyBlock == null) return;
        int size = copyBlock.getCopyMap().size();
        if(size == 0 || size > 100000) return;


        copyBlock.setPlayerPastePos(player.getOnPos());//粘帖时位置
        copyBlock.setPasteVec3(player.getDirection().getNormal());//粘帖时朝向
        //计算玩家朝向旋转的角度
        Rotation rotation = PosDirection.calcDirection(copyBlock.getCopyVec3(),copyBlock.getPasteVec3());
        for (Map.Entry<BlockPos, BlockState> entry : copyBlock.getCopyMap().entrySet()){

            if(entry.getValue().is(Blocks.AIR)) continue;

            BlockPos pos = entry.getKey();
            //根据玩家朝向旋转复制内容
            pos = pos.rotate(rotation);
            pos = pos.offset(player.getOnPos());
            BlockState state = entry.getValue().rotate(rotation);
//                drawBlock(stack, pos, state);
            if(!state.getFluidState().isEmpty()){
                RenderLiquidBlock.drawLiquid(stack,pos, state);

            }else{
                drawBlock(stack, pos, state);
            }

        }
    }

}

