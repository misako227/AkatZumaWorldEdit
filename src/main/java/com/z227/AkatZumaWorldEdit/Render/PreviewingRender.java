package com.z227.AkatZumaWorldEdit.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.PosDirection;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.Items.LineItem;
import com.z227.AkatZumaWorldEdit.Items.ProjectorItem;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Random;


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


        //获取方块坐标
        PlayerMapData PMD = Util.getPMD(player);
        Item item = heldItem.getItem();
        if(AkatZumaWorldEdit.USEITEM.get(item) == null && PMD.getBrushMap().get(item) == null)return;

        BlockPos pStart= PMD.getPos1(), pEnd = PMD.getPos2();




            for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
                if (entity instanceof LivingEntity) {

//                    VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
                    PoseStack stack = event.getPoseStack();
                    Matrix4f  projectionMatrix = event.getProjectionMatrix();

                    Vec3 view = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();


//                    RenderLineBox.renderBlockLine(stack, pStart, pEnd, projectionMatrix, view);
                    renderLine(stack, pStart, pEnd);

                    if(item instanceof ProjectorItem){
                        CopyBlock copyBlock = PMD.getCopyBlock();
                        drawCopyBlock( copyBlock, stack, player);
                        return;
                    }
//                    //渲染笔刷
//                    if(PMD.getBrushMap().get(item)!= null){
//                        RenderSphere.render(stack, item, PMD);
//                    }



                    //渲染连线工具
                    if (item instanceof LineItem){
//                        RenderCurveLine.render(vertexConsumer, stack, player);
                        RenderCurveLineBox.renderBlockLine(stack, player, projectionMatrix, view);
                    }


                    return;
                }
            }


    }

    //选择框
    public static void renderLine(PoseStack stack, BlockPos pStart, BlockPos pEnd){
        if (pStart == null || pEnd == null) return;

        VertexConsumer VertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
        BlockPos p1 = new BlockPos(Math.min(pStart.getX(), pEnd.getX()), Math.min(pStart.getY(), pEnd.getY()), Math.min(pStart.getZ(), pEnd.getZ()));
        BlockPos p2 = new BlockPos(Math.max(pStart.getX(), pEnd.getX()) + 1, Math.max(pStart.getY(), pEnd.getY()) + 1, Math.max(pStart.getZ(), pEnd.getZ()) + 1);
        AABB aabb =  new AABB(p1, p2);
        Vec3 camvec = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        stack.pushPose();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        stack.translate(-camvec.x, -camvec.y,-camvec.z);
        LevelRenderer.renderLineBox(stack,VertexConsumer, aabb,48, 1, 167,1);
        renderLineBox(stack,VertexConsumer, pStart,1, 170, 1, 1,1);
        renderLineBox(stack,VertexConsumer, pEnd,1, 1, 170, 170,1);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        stack.popPose();

    }

    public static void renderLineBox(PoseStack stack,VertexConsumer vertexConsumer,BlockPos pos,float size,float r,float g,float b, float opacity){
        LevelRenderer.renderLineBox(stack,vertexConsumer, pos.getX(), pos.getY(), pos.getZ(),pos.getX()+size, pos.getY()+size, pos.getZ()+size, r, g, b,opacity);
    }





    public static void drawCopyBlock(CopyBlock copyBlock,PoseStack stack, Player player){
        if(copyBlock == null) return;
        int size = copyBlock.getClientCopyMap().size();
        if(size == 0 || size > 100000) return;

        VertexConsumer vertexConsumerB = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.cutout());


        copyBlock.setPlayerPastePos(player.getOnPos());//粘帖时位置
        copyBlock.setPasteVec3(player.getDirection().getNormal());//粘帖时朝向

        //计算玩家朝向旋转的角度
        Rotation rotation = PosDirection.calcDirection(copyBlock.getCopyVec3(),copyBlock.getPasteVec3());

        Random randomSource = new Random();
        randomSource.setSeed(42L);
        Vec3 camvec = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        Level level = Minecraft.getInstance().level;
        for (Map.Entry<BlockPos, BlockState> entry : copyBlock.getClientCopyMap().entrySet()){

//            if(entry.getValue().is(Blocks.AIR)) continue;

            BlockPos pos = entry.getKey();
            //根据玩家朝向旋转复制内容
            pos = pos.rotate(rotation);
            pos = pos.offset(player.getOnPos());
            BlockState state = entry.getValue().rotate(rotation);
//                drawBlock(stack, pos, state);
//            if(!state.getFluidState().isEmpty()){
//                RenderLiquidBlock.drawLiquid(stack,pos, state);
//
//            }else{
//                drawBlock(stack,vertexConsumerB, pos, state,level, randomSource, camvec);
//            }
            drawBlock(stack,vertexConsumerB, pos, state,level, randomSource, camvec);

        }

    }

    public static void drawBlock(PoseStack stack,VertexConsumer vertexConsumer,BlockPos pos,BlockState blockState,Level level,Random randomSource,Vec3 camvec){

        stack.pushPose();
        stack.translate(pos.getX()-camvec.x, pos.getY()-camvec.y, pos.getZ()-camvec.z);

        BakedModel bakedModel = RenderBlockTest.getBlockModel(blockState);
        // 使用可变的BlockPos来减少对象创建，提高性能
//        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();
        BitSet bitSet = new BitSet(3);

        for (Direction direction : Direction.values()) {
            // 获取当前方向的四边形列表
            List<BakedQuad> quads = bakedModel.getQuads(blockState, direction, randomSource);
            RenderBlockTest.renderModelFaceFlat(level, blockState, pos, 15728880,  655363, false, stack, vertexConsumer, quads, bitSet);

        }


        // 获取无方向的四边形列表
        List<BakedQuad> quadsNoDirection = bakedModel.getQuads(blockState, null, randomSource);
        if (!quadsNoDirection.isEmpty()) {
            // 渲染模型的无方向平面
            RenderBlockTest.renderModelFaceFlat(level, blockState, pos, 15728880, 655363, true, stack, vertexConsumer, quadsNoDirection, bitSet);
        }
        stack.popPose();

    }



}

