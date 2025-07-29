package com.z227.AkatZumaWorldEdit.Render.renderBlock;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.z227.AkatZumaWorldEdit.Core.PosDirection;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11C;

import java.util.List;
import java.util.Map;


@OnlyIn(Dist.CLIENT)
public class RenderBlock {
    private static VertexBuffer vertexBuffer;
    public static boolean requestedRefresh = false;

    public static void updateBLockVertexBuffer() {
        vertexBuffer = null;
        requestedRefresh = true;

    }

    public static void renderBlock(CopyBlock copyBlock, PoseStack stack, Player player, Matrix4f projectionMatrix, Vec3 camera) {
        if(copyBlock == null) return;


        Map<BlockPos, BlockState> clientCopyMap = copyBlock.getClientCopyMap();
        int size = clientCopyMap.size();
        if(size == 0 || size > 100000) return;

//        requestedRefresh = false;
        //STATIC 表示缓冲区不会经常修改
//        vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        stack.pushPose();
        RenderSystem.enableCull();
        RenderSystem.depthFunc(GL11C.GL_LESS);
        //tess用于在OpenGL中创建和处理几何图形，用于在GPU生成线，三角形等基本形状
        Tesselator tessellator = Tesselator.getInstance();
        //BufferBuilder 用于构建顶点缓冲区，用于将几何数据转化为0penGL可以理解的定点格式
        BufferBuilder buffer = tessellator.getBuilder();

        //设置BufferBuilder的绘制模式和顶点格式
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
        RenderSystem.setShader(GameRenderer::getRendertypeCutoutShader);
        copyBlock.setPlayerPastePos(player.getOnPos());//粘帖时位置
        copyBlock.setPasteVec3(player.getDirection().getNormal());//粘帖时朝向
        //计算玩家朝向旋转的角度
        Rotation rotation = PosDirection.calcDirection(copyBlock.getCopyVec3(),copyBlock.getPasteVec3());
        RandomSource randomSource = RandomSource.create();
        randomSource.setSeed(42L);
        Map<BlockPos, List<Direction>> clientCopyDirectionMap = copyBlock.getClientCopyDirectionMap();
        for (Map.Entry<BlockPos, BlockState> entry : clientCopyMap.entrySet()){
            BlockPos pos = entry.getKey();
            BlockPos pastePos = pos.rotate(rotation);
            pastePos = pastePos.offset(player.getOnPos());
//            pastePos = pastePos.offset(copyBlock.getPlayerCopyPos().relative(Direction.UP, 10));
            BlockState state = entry.getValue().rotate(rotation);

//            if(!state.getFluidState().isEmpty()){
//                RenderLiquidBlock.drawLiquid(stack,pastePos, state);
//
//            }else{
//                drawBlock(stack,vertexConsumerB, pastePos, state, clientCopyDirectionMap.get(pos), level, randomSource,camvec);
//            }
            drawBlock(stack, buffer, pastePos, state, clientCopyDirectionMap.get(pos), rotation,  randomSource, camera);
        }



//            RenderLinePos.bufferAddBlockVertex(buffer,matrix3f, aabb, opacity, 48, 1, 167);
//            LevelRenderer.renderLineBox(stack,buffer, aabb, 48, 1, 167, opacity);

        //将顶点缓冲绑定在0penGL顶点数组上
//        vertexBuffer.bind();
        //将缓冲区数据上传到顶点缓冲对象，buffer包含了绘制的顶点数据。

        BufferUploader.drawWithShader(buffer.end());
        stack.popPose();
//        vertexBuffer.upload(buffer.end());
//            结束顶点缓冲的绑定，后续绘制不会在使用这个顶点缓冲对象了
//        VertexBuffer.unbind();

        //设置渲染系统的着色器为位置颜色着色器，返回的是着色器对象

//            RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);

//                PoseStack matrix = event.getPoseStack();
//        stack.pushPose();
        // 禁用背面剔除
//        RenderSystem.disableCull();
//        RenderSystem.depthFunc(GL11C.GL_ALWAYS);

//        stack.translate(-camera.x, -camera.y, -camera.z);

//        OptifinePipelineProvider.beginLeash().run();
//        vertexBuffer.bind();

        //绘制场景模型
//        vertexBuffer.drawWithShader(stack.last().pose(), projectionMatrix, RenderSystem.getShader());
//        VertexBuffer.unbind();
//        OptifinePipelineProvider.endLeash().run();

        // 启用背面剔除
//        RenderSystem.enableCull();



//            GL11.glEnable(GL11.GL_DEPTH_TEST);
            //关闭混合
//            GL11.glDisable(GL11.GL_BLEND);


    }

    public static BakedModel getBlockModel(BlockState pState) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(pState);
    }


    public static void drawBlock(PoseStack stack, VertexConsumer vertexConsumer, BlockPos pos, BlockState blockState, List<Direction> directions,Rotation rotation, RandomSource randomSource, Vec3 camvec){

        if(directions == null) return;
        stack.pushPose();
        stack.translate(pos.getX()-camvec.x, pos.getY()-camvec.y, pos.getZ()-camvec.z);
//        stack.translate(-camvec.x, -camvec.y, -camvec.z);
//        stack.translate(pos.getX(), pos.getX(), pos.getX());

        BakedModel bakedModel = getBlockModel(blockState);
        // 使用可变的BlockPos来减少对象创建，提高性能
//        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();
//        BitSet bitSet = new BitSet(3);

        for (Direction direction : directions) {
            direction = getDirectionRotate(direction,rotation);
            // 获取当前方向的四边形列表
            List<BakedQuad> quads = bakedModel.getQuads(blockState, direction, randomSource, ModelData.EMPTY, null);
            RenderBlockTest.renderModelFaceFlat( blockState,15728880,655363, stack, vertexConsumer, quads);

        }


        // 获取无方向的四边形列表
        List<BakedQuad> quadsNoDirection = bakedModel.getQuads(blockState, null, randomSource, ModelData.EMPTY, null);
        if (!quadsNoDirection.isEmpty()) {
            // 渲染模型的无方向平面
            RenderBlockTest.renderModelFaceFlat(blockState, 15728880, 655363, stack, vertexConsumer, quadsNoDirection);
        }
        stack.popPose();

    }

    public static Direction getDirectionRotate(Direction direction, Rotation rotation) {
        if(direction == Direction.DOWN || direction == Direction.UP) return direction;
        switch (rotation){
            case CLOCKWISE_90:
                return direction.getClockWise();
            case COUNTERCLOCKWISE_90:
                return direction.getCounterClockWise();
            case CLOCKWISE_180:
                return direction.getOpposite();
            default:
                return direction;
        }
    }



    public static void drawLiquid(PoseStack stack, BlockPos pos, BlockState blockState){
        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.translucent());

        //关闭深度检测
//        RenderSystem.disableDepthTest();

        Vec3 camvec = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        FluidState fluidState= blockState.getFluidState();

        stack.pushPose();
        stack.translate(pos.getX()-camvec.x, pos.getY()-camvec.y, pos.getZ()-camvec.z);

        Fluid fluid = fluidState.getType();
        FluidStack fluidStack = new FluidStack(fluid, 1000);
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);


        int color = extensions.getTintColor(fluidStack);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(extensions.getStillTexture(fluidStack));
        float a = (color >> 24 & 0xFF) / 255.0f;
        float r = (color >> 16 & 0xFF) / 255.0f;
        float g = (color >> 8 & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float x = 0.0f;
        float y = 0.0f;
        float z = 0.0f;
        float x2 = 1.0f;
        float z2 = 1.0f;
        float height = 0.875f; //14/16
        float size = 16.0f;
        float[] cols = new float[]{r, g, b, a};
        float minU = sprite.getU0();
        float minV = sprite.getV0();
//        int brightness = LevelRenderer.getLightColor(Minecraft.getInstance().level, pos);
        int brightness = 1000;

        BakedQuad quad;
//        quad = createQuad(ImmutableList.of(new Vec3(x, height, z), new Vec3(x, height, z2), new Vec3(x2, height, z2), new Vec3(x2, height, z)), cols, sprite, Direction.UP, minU, size, minV, size);
//        vertexConsumer.putBulkData(stack.last(), quad, r, g, b, a, brightness, 0, false);
//        quad = createQuad(ImmutableList.of(new Vec3(x2, height, z), new Vec3(x2, y, z), new Vec3(x, y, z), new Vec3(x, height, z)), cols, sprite, Direction.NORTH, minU, size, minV, size);



        stack.popPose();

//        RenderSystem.enableDepthTest();
    }


}