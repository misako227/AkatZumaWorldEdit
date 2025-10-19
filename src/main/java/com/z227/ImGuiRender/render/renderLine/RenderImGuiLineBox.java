package com.z227.ImGuiRender.render.renderLine;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.z227.AkatZumaWorldEdit.Render.renderLine.OptifinePipelineProvider;
import com.z227.ImGuiRender.EditModeData;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;


@OnlyIn(Dist.CLIENT)
public class RenderImGuiLineBox {

    public static void renderBlockLine(VertexBuffer vertexBuffer, PoseStack stack,Matrix4f mat4f, Vec3 view) {
        if (vertexBuffer != null && !vertexBuffer.isInvalid()) {
//            = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();

            //启动混合模式，控制颜色和深度值合并在一起，即RGB通道和透明度通道
//            GL11.glEnable(GL11.GL_BLEND);
            //设置混合函数，设置混合函数是源颜色的透明通道和目标颜色的1-透明通道进行混合
//            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            //禁用深度测试
//            GL11.glDisable(GL11.GL_DEPTH_TEST);


            //设置渲染系统的着色器为位置颜色着色器，返回的是着色器对象
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
//            RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);

            stack.pushPose();
            // 禁用背面剔除
            RenderSystem.disableCull();
            RenderSystem.depthFunc(GL11C.GL_ALWAYS);
            RenderSystem.lineWidth(3.0F);
            //抗锯齿
            GL11.glEnable(GL11.GL_LINE_SMOOTH);

            stack.translate(-view.x, -view.y, -view.z);

            EditModeData.submitTask(OptifinePipelineProvider.beginLeash());
            vertexBuffer.bind();

            //绘制场景模型
            vertexBuffer.drawWithShader(stack.last().pose(), mat4f, RenderSystem.getShader());
            VertexBuffer.unbind();
            EditModeData.submitTask(OptifinePipelineProvider.endLeash());

            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            // 启用背面剔除
            RenderSystem.enableCull();

            stack.popPose();

//            GL11.glEnable(GL11.GL_DEPTH_TEST);
            //关闭混合
//            GL11.glDisable(GL11.GL_BLEND);

        }
    }

//
//    public static void bufferAddBlockVertex(BufferBuilder buffer, AABB pBox,float opacity, float red, float green, float blue) {
//        double x = pBox.minX;
//        double y = pBox.minY;
//        double z = pBox.minZ;
//        double maxX = pBox.maxX;
//        double maxY = pBox.maxY;
//        double maxZ = pBox.maxZ;
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

}