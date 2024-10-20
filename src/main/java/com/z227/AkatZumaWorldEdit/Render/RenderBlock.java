//package com.z227.AkatZumaWorldEdit.Render;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.*;
//import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.block.BlockRenderDispatcher;
//import net.minecraft.client.renderer.block.ModelBlockRenderer;
//import net.minecraft.client.renderer.block.model.BakedQuad;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.client.resources.model.BakedModel;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.util.Mth;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.BlockAndTintGetter;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.client.model.data.ModelData;
//import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;
//import org.joml.Matrix4f;
//import org.lwjgl.opengl.GL11;
//
//import java.util.BitSet;
//import java.util.List;
//
//public class RenderBlock {
//    private static VertexBuffer vertexBuffer;
//    public static boolean requestedRefresh = false;
//
//    public static BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
//
//    public static void updateVertexBuffer() {
//        vertexBuffer = null;
//        requestedRefresh = true;
//    }
//
//    public static void renderBlocks(PoseStack stack, Matrix4f mat4f, Vec3 view, CopyBlock copyBlock, Player player, Level pLevel) {
//        if(copyBlock == null) return;
//
//        if (vertexBuffer == null || requestedRefresh) {
//            requestedRefresh = false;
//            //STATIC 表示缓冲区不会经常修改
//            vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
//
//            //tess用于在OpenGL中创建和处理几何图形，用于在GPU生成线，三角形等基本形状
//            Tesselator tessellator = Tesselator.getInstance();
//            //BufferBuilder 用于构建顶点缓冲区，用于将几何数据转化为0penGL可以理解的定点格式
//            BufferBuilder buffer = tessellator.getBuilder();
//            QuadBakingVertexConsumer.Buffered quadBaker = new QuadBakingVertexConsumer.Buffered();
//
//            //设置BufferBuilder的绘制模式和顶点格式
//            //VertexFormat.Mode.DEBUG 表示绘制模式为调试线,
//            //DefaultVertexFormat.POSITION_COLOR 表示顶点格式为位置+颜色模式，即每个顶点具有位置信息和颜色信息
//            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
//
//
////            int size = copyBlock.getCopyMap().size();
////            if(size == 0 || size > 100000) return;
//
//
////            copyBlock.setPlayerPastePos(player.getOnPos());//粘帖时位置
////            copyBlock.setPasteVec3(player.getDirection().getNormal());//粘帖时朝向
////            //计算玩家朝向旋转的角度
////            Rotation rotation = PosDirection.calcDirection(copyBlock.getCopyVec3(),copyBlock.getPasteVec3());
////
////            for (Map.Entry<BlockPos, BlockState> entry : copyBlock.getCopyMap().entrySet()){
////                if(entry.getValue().is(Blocks.AIR)) continue;
////
////                BlockPos pos = entry.getKey();
////                //根据玩家朝向旋转复制内容
////                pos = pos.rotate(rotation);
////                pos = pos.offset(player.getOnPos());
////                BlockState blockState = entry.getValue().rotate(rotation);
////                createQuad(stack, buffer, view, blockState,pos);
////            }
//            BlockPos pos = new BlockPos(-464,128, 75);
//            BlockState blockState = Blocks.GRASS_BLOCK.defaultBlockState();
//
//            VertexConsumer VertexC = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.solid());
////            rendermodel(stack, buffer, view, blockState, pos);
//            Vec3 vec3 = blockState.getOffset(pLevel, pos);
////            stack.translate(vec3.x, vec3.y, vec3.z);
//            // 创建一个BitSet来跟踪处理过的方向
//            BitSet bitSet = new BitSet(3);
//            // 使用可变的BlockPos来减少对象创建，提高性能
//            BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();
//            RandomSource randomsource = RandomSource.create();
//            BakedModel bakedModel = RenderBlockTest.getBlockModel(blockState);
//            createQuad(stack, buffer, view, blockState, pos);
////            for (Direction direction : Direction.values()) {
////
////                randomsource.setSeed(42L);
////                List<BakedQuad> quads = bakedModel.getQuads(blockState, direction, randomsource, ModelData.EMPTY, null);
////                if (!quads.isEmpty()) {
////                    // 设置邻接块的位置
////                    mutableBlockPos.setWithOffset(pos, direction);
////                    // 检查是否应该渲染当前方向的面
////
////                        int lightColor = 15728880;     //655363
////                        // 渲染模型的平面
////                    RenderBlockTest.renderModelFaceFlat(pLevel, blockState, pos, lightColor, 655363, false, stack, buffer, quads, bitSet);
////
////                }
////            }
//
//            //将顶点缓冲绑定在0penGL顶点数组上
//            vertexBuffer.bind();
//            //将缓冲区数据上传到顶点缓冲对象，buffer包含了绘制的顶点数据。
//            vertexBuffer.upload(buffer.end());
//            //结束顶点缓冲的绑定，后续绘制不会在使用这个顶点缓冲对象了
//            VertexBuffer.unbind();
//        }
//
//        if (vertexBuffer != null) {
////            Vec3 view = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
//
//            //启动混合模式，控制颜色和深度值合并在一起，即RGB通道和透明度通道
////            GL11.glEnable(GL11.GL_BLEND);
//            //设置混合函数，设置混合函数是源颜色的透明通道和目标颜色的1-透明通道进行混合
////            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//            //抗锯齿
////            GL11.glEnable(GL11.GL_LINE_SMOOTH);
//            //禁用深度测试
//            GL11.glDisable(GL11.GL_DEPTH_TEST);
//
//            //设置渲染系统的着色器为位置颜色着色器，返回的是着色器对象
//            RenderSystem.setShader(GameRenderer::getPositionColorShader);
//
////                PoseStack matrix = event.getPoseStack();
//            stack.pushPose();
////            stack.translate(-view.x, -view.y, -view.z);
//            BlockPos pos = new BlockPos(-464,128, 75);
//            stack.translate(pos.getX()-view.x, pos.getY()-view.y, pos.getZ()-view.z);
//
//            vertexBuffer.bind();
//            //绘制场景模型
//            vertexBuffer.drawWithShader(stack.last().pose(), mat4f, RenderSystem.getShader());
//            VertexBuffer.unbind();
//            stack.popPose();
//
//            GL11.glEnable(GL11.GL_DEPTH_TEST);
//            //关闭混合
////            GL11.glDisable(GL11.GL_BLEND);
////            GL11.glDisable(GL11.GL_LINE_SMOOTH);
//        }
//
//    }
//
//
//
//    public static void createQuad(PoseStack stack, VertexConsumer vertexConsumer,Vec3 camera, BlockState blockState, BlockPos pos){
////        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.translucent());
//
//        //深度检测
////        RenderSystem.enableDepthTest();
//        BakedModel blockModel = blockRenderDispatcher.getBlockModel(blockState);
//
//        int color = Minecraft.getInstance().getBlockColors().getColor(blockState, (BlockAndTintGetter)null, (BlockPos)null, 0);
//
////        stack.pushPose();
////        stack.translate(pos.getX()-camera.x, pos.getY()-camera.y, pos.getZ()-camera.z);
//
//        float a = (color >> 24 & 0xFF) / 255.0f;
//        float r = (color >> 16 & 0xFF) / 255.0f;
//        float g = (color >> 8 & 0xFF) / 255.0f;
//        float b = (color & 0xFF) / 255.0f;
//        float x = 0.0f;
//        float y = 0.0f;
//        float z = 0.0f;
//        float x2 = 1.0f;
//        float z2 = 1.0f;
//        float height = 0.875f; //14/16
//        float size = 16.0f;
//        float[] cols = new float[]{r, g, b, a};
////        float minU = sprite.getU0();
////        float minV = sprite.getV0();
////        int brightness = LevelRenderer.getLightColor(Minecraft.getInstance().level, pos);
//        int brightness = 1000;
////        createQuad(stack, vertexConsumer, camera, blockState, pos)
//
//        RandomSource randomsource = RandomSource.create();
//        long i = 42L;
//        for(Direction direction : Direction.values()) {
//            randomsource.setSeed(i);
//            List<BakedQuad> pQuads = blockModel.getQuads(blockState, direction, randomsource, ModelData.EMPTY, RenderType.translucent());
//
//            renderQuadList(stack.last(),vertexConsumer , r, g, b, pQuads, 15728640, OverlayTexture.pack(3,10));
//
//
////            vertexConsumer.putBulkData(stack.last(), blockModel.getQuads(blockState, direction, randomsource, ModelData.EMPTY, RenderType.translucent()), r, g, b, a, brightness, 0, false);
////            vertexConsumer.putBulkData(stack.last(), quad, r, g, b, a, brightness, 0, false);
//        }
//
////        stack.popPose();
//
//
//    }
//
//    private static void renderQuadList(PoseStack.Pose pPose, VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, List<BakedQuad> pQuads, int pPackedLight, int pPackedOverlay) {
//        for(BakedQuad bakedquad : pQuads) {
//            float f;
//            float f1;
//            float f2;
//            if (bakedquad.isTinted()) {
//                f = Mth.clamp(pRed, 0.0F, 1.0F);
//                f1 = Mth.clamp(pGreen, 0.0F, 1.0F);
//                f2 = Mth.clamp(pBlue, 0.0F, 1.0F);
//            } else {
//                f = 1.0F;
//                f1 = 1.0F;
//                f2 = 1.0F;
//            }
//
//            pConsumer.putBulkData(pPose, bakedquad, pRed, pGreen, pBlue, pPackedLight, pPackedOverlay);
//
//        }
//
//    }
//
//    public static void rendermodel(PoseStack stack, VertexConsumer vertexConsumer,Vec3 camera, BlockState blockState, BlockPos pos){
//        BakedModel blockModel = blockRenderDispatcher.getBlockModel(blockState);
//        int color = Minecraft.getInstance().getBlockColors().getColor(blockState, (BlockAndTintGetter)null, (BlockPos)null, 0);
//
////        stack.pushPose();
////        stack.translate(pos.getX()-camera.x, pos.getY()-camera.y, pos.getZ()-camera.z);
//
//        float a = (color >> 24 & 0xFF) / 255.0f;
//        float r = (color >> 16 & 0xFF) / 255.0f;
//        float g = (color >> 8 & 0xFF) / 255.0f;
//        float b = (color & 0xFF) / 255.0f;
//        for (net.minecraft.client.renderer.RenderType rt : blockModel.getRenderTypes(blockState, RandomSource.create(42), ModelData.EMPTY)){
//            Minecraft mc = Minecraft.getInstance();
//            ModelBlockRenderer mbr = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
//    //        BakedModel blockModel = blockRenderDispatcher.getBlockModel(blockState);
//            mbr.renderModel(stack.last(),vertexConsumer,blockState,blockModel,r,g,b,15728640, OverlayTexture.pack(3,10));
//
//
//            RandomSource randomsource = RandomSource.create();
//            long i = 42L;
////            for(Direction direction : Direction.values()) {
////                randomsource.setSeed(i);
////                List<BakedQuad> pQuads = blockModel.getQuads(blockState, direction, randomsource, ModelData.EMPTY, rt);
////                renderQuadList(stack.last(),vertexConsumer , r, g, b, pQuads, 10000, OverlayTexture.pack(3,10));
////            }
////
////            randomsource.setSeed(42L);
////            renderQuadList(stack.last(),vertexConsumer , r, g, b,  blockModel.getQuads(blockState, (Direction)null, randomsource, ModelData.EMPTY, rt), 10000, OverlayTexture.pack(3,10));
//
//        }
//
////        Minecraft mc = Minecraft.getInstance();
////        ModelBlockRenderer mbr = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
//////        BakedModel blockModel = blockRenderDispatcher.getBlockModel(blockState);
////        mbr.renderModel(stack.last(),vertexConsumer,blockState,blockModel,r,g,b,15728640, OverlayTexture.pack(3,10));
//
//
//
//
//    }
////    private static void renderQuadList(PoseStack.Pose pPose, VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, List<BakedQuad> pQuads, int pPackedLight, int pPackedOverlay) {
////        pConsumer.vertex(x, y, z);
////        pConsumer.normal((float) normal.x, (float) normal.y, (float) normal.z);
////        pConsumer.color(cols[0], cols[1], cols[2], cols[3]);
////        pConsumer.uv(sprite.getU(u), sprite.getV(v));
////        pConsumer.setSprite(sprite);
////        pConsumer.setDirection(face);
////        pConsumer.endVertex();
////
////    }
//
//
//    private static BakedQuad createQuad (List< Vec3 > vectors, float[] cols, TextureAtlasSprite sprite, Direction
//            face, float u1, float u2, float v1, float v2){
//        QuadBakingVertexConsumer.Buffered quadBaker = new QuadBakingVertexConsumer.Buffered();
//        Vec3 normal = Vec3.atLowerCornerOf(face.getNormal());
//
//        putVertex(quadBaker, normal, vectors.get(0).x, vectors.get(0).y, vectors.get(0).z, u1, v1, sprite, cols, face);
//        putVertex(quadBaker, normal, vectors.get(1).x, vectors.get(1).y, vectors.get(1).z, u1, v2, sprite, cols, face);
//        putVertex(quadBaker, normal, vectors.get(2).x, vectors.get(2).y, vectors.get(2).z, u2, v2, sprite, cols, face);
//        putVertex(quadBaker, normal, vectors.get(3).x, vectors.get(3).y, vectors.get(3).z, u2, v1, sprite, cols, face);
//
//        return quadBaker.getQuad();
//    }
//
//    private static void putVertex (QuadBakingVertexConsumer quadBaker, Vec3 normal,
//                                   double x, double y, double z, float u, float v, TextureAtlasSprite sprite,float[] cols, Direction face){
//        quadBaker.vertex(x, y, z);
//        quadBaker.normal((float) normal.x, (float) normal.y, (float) normal.z);
//        quadBaker.color(cols[0], cols[1], cols[2], cols[3]);
//        quadBaker.uv(sprite.getU(u), sprite.getV(v));
//        quadBaker.setSprite(sprite);
//        quadBaker.setDirection(face);
//        quadBaker.endVertex();
//    }
//}
