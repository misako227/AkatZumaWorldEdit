//package com.z227.AkatZumaWorldEdit.Render;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.block.model.BakedQuad;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.inventory.InventoryMenu;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.level.material.FluidState;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
//import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;
//import net.minecraftforge.fluids.FluidStack;
//
//import java.util.List;
//
//// by 建筑小帮手
//public class RenderLiquidBlock {
//
//    private static BakedQuad createQuad(List<Vec3> vectors, float[] cols, TextureAtlasSprite sprite, Direction face, float u1, float u2, float v1, float v2) {
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
//    private static void putVertex(QuadBakingVertexConsumer quadBaker, Vec3 normal,
//                                  double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float[] cols, Direction face) {
//        quadBaker.vertex(x, y, z);
//        quadBaker.normal((float) normal.x, (float) normal.y, (float) normal.z);
//        quadBaker.color(cols[0], cols[1], cols[2], cols[3]);
//        quadBaker.uv(sprite.getU(u), sprite.getV(v));
//        quadBaker.setSprite(sprite);
//        quadBaker.setDirection(face);
//        quadBaker.endVertex();
//    }
//
//
//    public static void drawLiquid(PoseStack stack, BlockPos pos, BlockState blockState){
//        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.translucent());
//
//        //关闭深度检测
//        RenderSystem.disableDepthTest();
//
//        Vec3 camvec = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
//        FluidState fluidState= blockState.getFluidState();
//
//        stack.pushPose();
//        stack.translate(pos.getX()-camvec.x, pos.getY()-camvec.y, pos.getZ()-camvec.z);
//
//        Fluid fluid = fluidState.getType();
//        FluidStack fluidStack = new FluidStack(fluid, 1000);
//        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
//
//
//        int color = extensions.getTintColor(fluidStack);
//        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(extensions.getStillTexture(fluidStack));
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
//        float minU = sprite.getU0();
//        float minV = sprite.getV0();
////        int brightness = LevelRenderer.getLightColor(Minecraft.getInstance().level, pos);
//        int brightness = 1000;
//
//        BakedQuad quad;
////        quad = createQuad(ImmutableList.of(new Vec3(x, y, z2), new Vec3(x, y, z), new Vec3(x2, y, z), new Vec3(x2, y, z2)), cols, sprite, Direction.DOWN, minU, size, minV, size);
//        quad = createQuad(ImmutableList.of(new Vec3(x, height, z), new Vec3(x, height, z2), new Vec3(x2, height, z2), new Vec3(x2, height, z)), cols, sprite, Direction.UP, minU, size, minV, size);
//        vertexConsumer.putBulkData(stack.last(), quad, r, g, b, a, brightness, 0, false);
////        quad = createQuad(ImmutableList.of(new Vec3(x2, height, z), new Vec3(x2, y, z), new Vec3(x, y, z), new Vec3(x, height, z)), cols, sprite, Direction.NORTH, minU, size, minV, size);
////        vertexConsumer.putBulkData(stack.last(), quad, r, g, b, a, brightness, 0, false);
//
//
//        stack.popPose();
//
//        RenderSystem.enableDepthTest();
//    }
//
////    public static void drawBlock(PoseStack stack, BlockPos pos, BlockState blockState){
////        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.translucent());
////
////        //关闭深度检测
////        RenderSystem.disableDepthTest();
////
////        Vec3 camvec = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
////        FluidState fluidState= blockState.getFluidState();
////
////        stack.pushPose();
////        stack.translate(pos.getX()-camvec.x, pos.getY()-camvec.y, pos.getZ()-camvec.z);
////
//////        Fluid fluid = fluidState.getType();
//////        FluidStack fluidStack = new FluidStack(fluid, 1000);
//////        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
////        IClientBlockExtensions extensions = IClientBlockExtensions.of(blockState.getBlock());
////        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
////        int color = blockColors.getColor(blockState, Minecraft.getInstance().level, pos);
////        BakedModel bakedModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState);
////
////        float a = (color >> 24 & 0xFF) / 255.0f;
////        float r = (color >> 16 & 0xFF) / 255.0f;
////        float g = (color >> 8 & 0xFF) / 255.0f;
////        float b = (color & 0xFF) / 255.0f;
////        float x = 0.0f;
////        float y = 0.0f;
////        float z = 0.0f;
////        float x2 = 1.0f;
////        float z2 = 1.0f;
////        float height = 0.875f; //14/16
////        float size = 16.0f;
////        float[] cols = new float[]{r, g, b, a};
//////        float minU = sprite.getU0();
//////        float minV = sprite.getV0();
//////        int brightness = LevelRenderer.getLightColor(Minecraft.getInstance().level, pos);
////        int brightness = 1000;
////
////        BakedQuad quad;
////        List<BakedQuad> quadList = bakedModel.getQuads(blockState,Direction.UP, new SingleThreadedRandomSource(100));
////        if(quadList.size() > 0){
////            quad = quadList.get(0);
////
////            vertexConsumer.putBulkData(stack.last(), quad, r, g, b, a, brightness, 0, false);
////        }
//////        quad = createQuad(ImmutableList.of(new Vec3(x, y, z2), new Vec3(x, y, z), new Vec3(x2, y, z), new Vec3(x2, y, z2)), cols, sprite, Direction.DOWN, minU, size, minV, size);
//////        quad = createQuad(ImmutableList.of(new Vec3(x, height, z), new Vec3(x, height, z2), new Vec3(x2, height, z2), new Vec3(x2, height, z)), cols, sprite, Direction.UP, minU, size, minV, size);
//////        vertexConsumer.putBulkData(stack.last(), quad, r, g, b, a, brightness, 0, false);
//////        quad = createQuad(ImmutableList.of(new Vec3(x2, height, z), new Vec3(x2, y, z), new Vec3(x, y, z), new Vec3(x, height, z)), cols, sprite, Direction.NORTH, minU, size, minV, size);
//////        vertexConsumer.putBulkData(stack.last(), quad, r, g, b, a, brightness, 0, false);
////        stack.popPose();
////
////        RenderSystem.enableDepthTest();
////    }
//}
