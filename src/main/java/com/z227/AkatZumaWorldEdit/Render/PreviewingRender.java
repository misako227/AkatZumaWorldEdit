package com.z227.AkatZumaWorldEdit.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.PosDirection;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.Event.ImguiMethod.ImguiMouseEvent;
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
import net.minecraft.util.RandomSource;
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
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.BitSet;
import java.util.List;
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


                    RenderLineBox.renderBlockLine(stack, pStart, pEnd, projectionMatrix, view);

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

                    if(ImguiMouseEvent.pos1 != null && ImguiMouseEvent.pos2 != null){
                        stack.pushPose();
                        stack.translate(-view.x, -view.y, -view.z);
                        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
                        renderLine(vertexConsumer, stack, ImguiMouseEvent.pos1,ImguiMouseEvent.pos2, 1.0F, 0.0F,0.0F, 1.0F);
                        AABB aabb = new AABB(ImguiMouseEvent.pos1, ImguiMouseEvent.pos2);
                        LevelRenderer.renderLineBox(stack, vertexConsumer, aabb, 1.0F, 1.0F, 0.0F, 1.0F);
                        stack.popPose();
                    }


                    return;
                }
            }


    }

    public static void renderLine(VertexConsumer pConsumer,PoseStack pPoseStack, BlockPos pos1, BlockPos pos2, float pRed, float pGreen, float pBlue, float pAlpha) {

        Matrix4f matrix4f = pPoseStack.last().pose();
        Matrix3f matrix3f = pPoseStack.last().normal();
        float pMinX = (float)pos1.getX();
        float pMinY = (float)pos1.getY();
        float pMinZ = (float)pos1.getZ();
        float pMaxX = (float)pos2.getX();
        float pMaxY = (float)pos2.getY();
        float pMaxZ = (float)pos2.getZ();

        pConsumer.vertex(matrix4f, pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        pConsumer.vertex(matrix4f, pMaxX, pMaxY, pMaxZ).color(pBlue, pGreen, pRed, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
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

        RandomSource randomSource = RandomSource.create();
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
            if(!state.getFluidState().isEmpty()){
                RenderLiquidBlock.drawLiquid(stack,pos, state);

            }else{
                drawBlock(stack,vertexConsumerB, pos, state,level, randomSource,camvec);
            }

        }

    }

    public static void drawBlock(PoseStack stack,VertexConsumer vertexConsumer,BlockPos pos,BlockState blockState,Level level,RandomSource randomSource,Vec3 camvec){

        stack.pushPose();
        stack.translate(pos.getX()-camvec.x, pos.getY()-camvec.y, pos.getZ()-camvec.z);

        BakedModel bakedModel = RenderBlockTest.getBlockModel(blockState);
        // 使用可变的BlockPos来减少对象创建，提高性能
//        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();
        BitSet bitSet = new BitSet(3);

        for (Direction direction : Direction.values()) {
            // 获取当前方向的四边形列表
            List<BakedQuad> quads = bakedModel.getQuads(blockState, direction, randomSource, ModelData.EMPTY, null);
            RenderBlockTest.renderModelFaceFlat(level, blockState, pos, 15728880,  655363, false, stack, vertexConsumer, quads, bitSet);

        }


        // 获取无方向的四边形列表
        List<BakedQuad> quadsNoDirection = bakedModel.getQuads(blockState, null, randomSource, ModelData.EMPTY, null);
        if (!quadsNoDirection.isEmpty()) {
            // 渲染模型的无方向平面
            RenderBlockTest.renderModelFaceFlat(level, blockState, pos, 15728880, 655363, true, stack, vertexConsumer, quadsNoDirection, bitSet);
        }
        stack.popPose();

    }



}

