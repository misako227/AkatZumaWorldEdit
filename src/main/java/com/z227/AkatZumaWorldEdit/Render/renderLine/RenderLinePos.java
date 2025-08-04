//package com.z227.AkatZumaWorldEdit.Render.renderLine;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.*;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.joml.Matrix3f;
//import org.joml.Matrix4f;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL11C;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderLinePos {
//
//    public static void renderBlockLineTess(PoseStack stack, BlockPos pStart, BlockPos pEnd,  Vec3 view) {
//        if (pStart == null || pEnd == null) return;
////        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
//        Tesselator tessellator = Tesselator.getInstance();
//        BufferBuilder buffer = tessellator.getBuilder();
//        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
//        stack.pushPose();
//        GL11.glEnable(GL11.GL_LINE_SMOOTH);
//        // 禁用背面剔除
//        RenderSystem.disableCull();
//        RenderSystem.depthFunc(GL11C.GL_ALWAYS);
//        RenderSystem.lineWidth(3.0F);
//        RenderSystem.setShader(GameRenderer::getPositionColorShader);
//        stack.translate(-view.x, -view.y, -view.z);
//        //获取标线AABB
//        BlockPos p1 = new BlockPos(Math.min(pStart.getX(), pEnd.getX()), Math.min(pStart.getY(), pEnd.getY()), Math.min(pStart.getZ(), pEnd.getZ()));
//        BlockPos p2 = new BlockPos(Math.max(pStart.getX(), pEnd.getX()) + 1, Math.max(pStart.getY(), pEnd.getY()) + 1, Math.max(pStart.getZ(), pEnd.getZ()) + 1);
//        AABB aabb =  new AABB(p1, p2);
//        float opacity = 1F;
//        final float size = 1.0f;
////        RenderLineBox.bufferAddBlockVertex(buffer, aabb, opacity, 48, 1, 167);;
////        bufferAddBlockVertex(buffer, pStart.getX(), pStart.getY(), pStart.getZ(), size, opacity, 170, 1, 1); //第一个选区点
////        bufferAddBlockVertex(buffer, pEnd.getX(), pEnd.getY(), pEnd.getZ(), size, opacity,1, 170, 170); //第二个选区点
//        bufferAddBlockVertex(stack, buffer, aabb, 48, 1, 167, 1);
//
//        OptifinePipelineProvider.beginLeash().run();
//        tessellator.end();
//        OptifinePipelineProvider.endLeash().run();
//
//
//        // 启用背面剔除
//        RenderSystem.enableCull();
//        GL11.glDisable(GL11.GL_LINE_SMOOTH);
//        stack.popPose();
//    }
//
//
//    public static void bufferAddBlockVertex(PoseStack pPoseStack, VertexConsumer pConsumer, AABB pBox, float pAlpha, float pRed, float pGreen, float pBlue) {
//        Matrix4f matrix4f = pPoseStack.last().pose();
//        Matrix3f matrix3f = pPoseStack.last().normal();
//        float f = (float) pBox.minX;
//        float f1 = (float) pBox.minY;
//        float f2 = (float) pBox.minZ;
//        float f3 = (float) pBox.maxX;
//        float f4 = (float) pBox.maxY;
//        float f5 = (float) pBox.maxZ;
//        float pGreen2 = pGreen;
//        float pBlue2 = pBlue;
//        float pRed2 = pRed;
////        float pAlpha = opacity;
//        pConsumer.vertex(matrix4f, f, f1, f2).color(pRed, pGreen2, pBlue2, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f3, f1, f2).color(pRed, pGreen2, pBlue2, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f, f1, f2).color(pRed2, pGreen, pBlue2, pAlpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f, f4, f2).color(pRed2, pGreen, pBlue2, pAlpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f, f1, f2).color(pRed2, pGreen2, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
//        pConsumer.vertex(matrix4f, f, f1, f5).color(pRed2, pGreen2, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
//        pConsumer.vertex(matrix4f, f3, f1, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f3, f4, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f3, f4, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f, f4, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f, f4, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
//        pConsumer.vertex(matrix4f, f, f4, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
//        pConsumer.vertex(matrix4f, f, f4, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f, f1, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f, f1, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f3, f1, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f3, f1, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
//        pConsumer.vertex(matrix4f, f3, f1, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
//        pConsumer.vertex(matrix4f, f, f4, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f3, f4, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f3, f1, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f3, f4, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        pConsumer.vertex(matrix4f, f3, f4, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
//        pConsumer.vertex(matrix4f, f3, f4, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
//    }
//
//
//
//
//    public static void bufferAddBlockVertex(VertexConsumer pConsumer,Matrix3f matrix3f,  AABB pBox, float opacity, float pRed, float pGreen, float pBlue) {
//        double f = pBox.minX;
//        double f1 = pBox.minY;
//        double f2 = pBox.minZ;
//        double f3 = pBox.maxX;
//        double f4 = pBox.maxY;
//        double f5 = pBox.maxZ;
//        float pGreen2 = pGreen;
//        float pBlue2 = pBlue;
//        float pRed2 = pRed;
//        float pAlpha = opacity;
//
//
//
//        pConsumer.vertex(f, f1, f2).color(pRed, pGreen2, pBlue2, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(f3, f1, f2).color(pRed, pGreen2, pBlue2, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(f, f1, f2).color(pRed2, pGreen, pBlue2, pAlpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        pConsumer.vertex(f, f4, f2).color(pRed2, pGreen, pBlue2, pAlpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        pConsumer.vertex(f, f1, f2).color(pRed2, pGreen2, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
//        pConsumer.vertex(f, f1, f5).color(pRed2, pGreen2, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
//        pConsumer.vertex(f3, f1, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        pConsumer.vertex(f3, f4, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        pConsumer.vertex(f3, f4, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(f, f4, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(f, f4, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
//        pConsumer.vertex(f, f4, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
//        pConsumer.vertex(f, f4, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
//        pConsumer.vertex(f, f1, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
//        pConsumer.vertex(f, f1, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(f3, f1, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(f3, f1, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
//        pConsumer.vertex(f3, f1, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
//        pConsumer.vertex(f, f4, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(f3, f4, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
//        pConsumer.vertex(f3, f1, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        pConsumer.vertex(f3, f4, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
//        pConsumer.vertex(f3, f4, f2).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
//        pConsumer.vertex(f3, f4, f5).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
//    }
//
//
//
//
//    public static void bufferAddBlockVertex(VertexConsumer buffer, double x, double y, double z,double maxX, double maxY, double maxZ,float opacity, float red, float green, float blue) {
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
//
//
////    public class LineRenderType extends RenderType {
////        private static final RenderType LINE_TYPE = create("akatzuma_line_type",
////                DefaultVertexFormat.POSITION_COLOR_NORMAL,
////                VertexFormat.Mode.LINES,
////                256,
////                false,
////                false,
////                RenderType.CompositeState.builder()
////                        .setShaderState(RENDERTYPE_LINES_SHADER)
////                        .setLineState(new LineStateShard(OptionalDouble.of(3.0))) // 线宽
////                        .setLayeringState(VIEW_OFFSET_Z_LAYERING)
////                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
////                        .setOutputState(ITEM_ENTITY_TARGET)
////                        .setWriteMaskState(COLOR_WRITE)
////                        .setCullState(NO_CULL)
////                        .setDepthTestState(NO_DEPTH_TEST) // 关键：无深度测试
////                        .createCompositeState(false));
////
////        public LineRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufSize, boolean useDelegate, boolean needsSorting, Runnable setup, Runnable clear) {
////            super(name, format, mode, bufSize, useDelegate, needsSorting, setup, clear);
////        }
////
////        public static RenderType get() {
////            return LINE_TYPE;
////        }
////    }
//}
//
//
//
//
