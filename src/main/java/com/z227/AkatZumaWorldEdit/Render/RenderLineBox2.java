//package com.z227.AkatZumaWorldEdit.Render;
//
//import com.mojang.blaze3d.vertex.BufferBuilder;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexBuffer;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.LevelRenderer;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import org.joml.Matrix4f;
//import org.lwjgl.opengl.GL11;
//
//public class RenderLineBox2 {
//    private static VertexBuffer vertexBuffer;
//    public static boolean requestedRefresh = false;
//
//
//
//    public static void updateVertexBuffer() {
//        vertexBuffer = null;
//        requestedRefresh = true;
////        vertexConsumer.end();
//
//    }
//
//    public static void renderBlockLine(PoseStack stack, BlockPos pStart, BlockPos pEnd, Matrix4f mat4f) {
//        if (pStart == null || pEnd == null) return;
//
//        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
//        BufferBuilder vertexConsumer = (BufferBuilder) bufferSource.getBuffer(RenderType.lines());
//
//        Vec3 camvec = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
//        //禁用深度测试
//        GL11.glDisable(GL11.GL_DEPTH_TEST);
//        //启动混合模式，控制颜色和深度值合并在一起，即RGB通道和透明度通道
//        GL11.glEnable(GL11.GL_BLEND);
//        //设置混合函数，设置混合函数是源颜色的透明通道和目标颜色的1-透明通道进行混合
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
////        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
////        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//
//        stack.pushPose();
////        stack.translate(-view.x, -view.y, -view.z);
//        stack.translate(-camvec.x, -camvec.y, -camvec.z);
//
//
//
////        RenderSystem.setShaderTexture(0, RenderLineBox.CLOUDS_LOCATION);
//
//            //获取标线AABB
//            BlockPos p1 = new BlockPos(Math.min(pStart.getX(), pEnd.getX()), Math.min(pStart.getY(), pEnd.getY()), Math.min(pStart.getZ(), pEnd.getZ()));
//            BlockPos p2 = new BlockPos(Math.max(pStart.getX(), pEnd.getX()) + 1, Math.max(pStart.getY(), pEnd.getY()) + 1, Math.max(pStart.getZ(), pEnd.getZ()) + 1);
//            AABB aabb =  new AABB(p1, p2);
////        bufferAddBlockVertex2(vertexConsumer, aabb, 1, 148, 148, 167);      //选区框
//        LevelRenderer.renderLineBox(stack,vertexConsumer, aabb,148, 255, 255, 1);
////            bufferAddBlockVertex(buffer, pStart.getX(), pStart.getY(), pStart.getZ(), size, opacity, 170, 1, 1); //第一个选区点
////            bufferAddBlockVertex(buffer, pEnd.getX(), pEnd.getY(), pEnd.getZ(), size, opacity,1, 170, 170); //第二个选区点
////        BufferUploader.drawWithShader(vertexConsumer.end());
//
//        stack.popPose();
//        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        //关闭混合
//        GL11.glDisable(GL11.GL_BLEND);
//
////        bufferSource.endBatch();
//
//
//
//        }
//
//
//
//    public static void bufferAddBlockVertex2(VertexConsumer buffer, AABB pBox,float opacity, float red, float green, float blue) {
//        double x = pBox.minX;
//        double y = pBox.minY;
//        double z = pBox.minZ;
//        double maxX = pBox.maxX;
//        double maxY = pBox.maxY;
//        double maxZ = pBox.maxZ;
//        double f = x;
//        double f1 = y;
//        double f2 = z;
//        double f3 = maxX;
//        double f4 = maxY;
//        double f5 = maxZ;
//
//
//
//
//        buffer.vertex( f, f1, f2).color(red, green, blue, opacity).normal( 1.0F, 0.0F, 0.0F).endVertex();
//        buffer.vertex( f3, f1, f2).color(red, green, blue, opacity).normal( 1.0F, 0.0F, 0.0F).endVertex();
//        buffer.vertex( f, f1, f2).color(red, green, blue, opacity).normal( 0.0F, 1.0F, 0.0F).endVertex();
//        buffer.vertex( f, f4, f2).color(red, green, blue, opacity).normal( 0.0F, 1.0F, 0.0F).endVertex();
//        buffer.vertex( f, f1, f2).color(red, green, blue, opacity).normal( 0.0F, 0.0F, 1.0F).endVertex();
//        buffer.vertex( f, f1, f5).color(red, green, blue, opacity).normal( 0.0F, 0.0F, 1.0F).endVertex();
//        buffer.vertex( f3, f1, f2).color(red, green, blue, opacity).normal( 0.0F, 1.0F, 0.0F).endVertex();
//        buffer.vertex( f3, f4, f2).color(red, green, blue, opacity).normal( 0.0F, 1.0F, 0.0F).endVertex();
//        buffer.vertex( f3, f4, f2).color(red, green, blue, opacity).normal( -1.0F, 0.0F, 0.0F).endVertex();
//        buffer.vertex( f, f4, f2).color(red, green, blue, opacity).normal( -1.0F, 0.0F, 0.0F).endVertex();
//        buffer.vertex( f, f4, f2).color(red, green, blue, opacity).normal( 0.0F, 0.0F, 1.0F).endVertex();
//        buffer.vertex( f, f4, f5).color(red, green, blue, opacity).normal( 0.0F, 0.0F, 1.0F).endVertex();
//        buffer.vertex( f, f4, f5).color(red, green, blue, opacity).normal( 0.0F, -1.0F, 0.0F).endVertex();
//        buffer.vertex( f, f1, f5).color(red, green, blue, opacity).normal( 0.0F, -1.0F, 0.0F).endVertex();
//        buffer.vertex( f, f1, f5).color(red, green, blue, opacity).normal( 1.0F, 0.0F, 0.0F).endVertex();
//        buffer.vertex( f3, f1, f5).color(red, green, blue, opacity).normal( 1.0F, 0.0F, 0.0F).endVertex();
//        buffer.vertex( f3, f1, f5).color(red, green, blue, opacity).normal( 0.0F, 0.0F, -1.0F).endVertex();
//        buffer.vertex( f3, f1, f2).color(red, green, blue, opacity).normal( 0.0F, 0.0F, -1.0F).endVertex();
//        buffer.vertex( f, f4, f5).color(red, green, blue, opacity).normal( 1.0F, 0.0F, 0.0F).endVertex();
//        buffer.vertex( f3, f4, f5).color(red, green, blue, opacity).normal( 1.0F, 0.0F, 0.0F).endVertex();
//        buffer.vertex( f3, f1, f5).color(red, green, blue, opacity).normal( 0.0F, 1.0F, 0.0F).endVertex();
//        buffer.vertex( f3, f4, f5).color(red, green, blue, opacity).normal( 0.0F, 1.0F, 0.0F).endVertex();
//        buffer.vertex( f3, f4, f2).color(red, green, blue, opacity).normal( 0.0F, 0.0F, 1.0F).endVertex();
//        buffer.vertex( f3, f4, f5).color(red, green, blue, opacity).normal( 0.0F, 0.0F, 1.0F).endVertex();
//
//
//    }
//
//
//    public static void bufferAddBlockVertex(VertexConsumer buffer, AABB pBox,float opacity, float red, float green, float blue) {
//        double x = pBox.minX;
//        double y = pBox.minY;
//        double z = pBox.minZ;
//        double maxX = pBox.maxX;
//        double maxY = pBox.maxY;
//        double maxZ = pBox.maxZ;
//
////        buffer.vertex(x, y, z).color(red, green, blue, opacity).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0.0F, 0.0F, 1.0F).endVertex();
////        buffer.vertex(x, maxY, z).color(red, green, blue, opacity).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0.0F, 0.0F, 1.0F).endVertex();
////        buffer.vertex(x, maxY, maxZ).color(red, green, blue, opacity).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0.0F, 0.0F, 1.0F).endVertex();
////        buffer.vertex(x, y, maxZ).color(red, green, blue, opacity).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0.0F, 0.0F, 1.0F).endVertex();
//
//        buffer.vertex(x, maxY, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(maxX, maxY, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(maxX, maxY, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(maxX, maxY, maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(maxX, maxY, maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, maxY, maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, maxY, maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, maxY, z).color(red, green, blue, opacity).endVertex();
//
//        // BOTTOM
//        buffer.vertex(maxX, y, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(maxX, y, maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(maxX, y, maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y, maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y, maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(maxX, y, z).color(red, green, blue, opacity).endVertex();
//
//        // Edge 1
//        buffer.vertex(maxX, y, maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(maxX, maxY, maxZ).color(red, green, blue, opacity).endVertex();
//
//        // Edge 2
//        buffer.vertex(maxX, y, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(maxX, maxY, z).color(red, green, blue, opacity).endVertex();
//
//        // Edge 3
//        buffer.vertex(x, y, maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, maxY, maxZ).color(red, green, blue, opacity).endVertex();
//
//        // Edge 4
//        buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, maxY, z).color(red, green, blue, opacity).endVertex();
//
//
//    }
//    public static void bufferAddBlockVertex(BufferBuilder buffer, double x, double y, double z,float size,float opacity, float red, float green, float blue) {
//        bufferAddBlockVertex(buffer, x, y, z, size, size, size, opacity, red, green, blue);
//    }
//
//    public static void bufferAddBlockVertex(BufferBuilder buffer, double x, double y, double z,double maxX, double maxY, double maxZ,float opacity, float red, float green, float blue) {
//
//        buffer.vertex(x, y + maxY, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x+ maxX, y + maxY, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x+ maxX, y + maxY, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x+ maxX, y + maxY, z+ maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x+ maxX, y + maxY, z+ maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y + maxY, z+ maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y + maxY, z+ maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y + maxY, z).color(red, green, blue, opacity).endVertex();
//
//        // BOTTOM
//        buffer.vertex(x+ maxX, y, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x+ maxX, y, z+ maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x+ maxX, y, z+ maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y, z+ maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y, z+ maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x+ maxX, y, z).color(red, green, blue, opacity).endVertex();
//
//        // Edge 1
//        buffer.vertex(x+ maxX, y, z+ maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x+ maxX, y + maxY, z+ maxZ).color(red, green, blue, opacity).endVertex();
//
//        // Edge 2
//        buffer.vertex(x+ maxX, y, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x+ maxX, y + maxY, z).color(red, green, blue, opacity).endVertex();
//
//        // Edge 3
//        buffer.vertex(x, y, z+ maxZ).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y + maxY, z+ maxZ).color(red, green, blue, opacity).endVertex();
//
//        // Edge 4
//        buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
//        buffer.vertex(x, y + maxY, z).color(red, green, blue, opacity).endVertex();
//    }
//
//
//
//
//}
