package com.z227.AkatZumaWorldEdit.Render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class RenderLineBox {
    private static VertexBuffer vertexBuffer;
    public static boolean requestedRefresh = false;

    public static void updateVertexBuffer() {
        vertexBuffer = null;
        requestedRefresh = true;
    }

    public static void renderBlocks(PoseStack stack, BlockPos pStart, BlockPos pEnd, Matrix4f mat4f) {
        if (pStart == null || pEnd == null) return;
        if (vertexBuffer == null || requestedRefresh) {
            requestedRefresh = false;
            //STATIC 表示缓冲区不会经常修改
            vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);

            //tess用于在OpenGL中创建和处理几何图形，用于在GPU生成线，三角形等基本形状
            Tesselator tessellator = Tesselator.getInstance();
            //BufferBuilder 用于构建顶点缓冲区，用于将几何数据转化为0penGL可以理解的定点格式
            BufferBuilder buffer = tessellator.getBuilder();



            //设置BufferBuilder的绘制模式和顶点格式
            //VertexFormat.Mode.DEBUG 表示绘制模式为调试线,
            //DefaultVertexFormat.POSITION_COLOR 表示顶点格式为位置+颜色模式，即每个顶点具有位置信息和颜色信息
            buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

            float opacity = 1F;
            final float size = 1.0f;

            //获取标线AABB
            BlockPos p1 = new BlockPos(Math.min(pStart.getX(), pEnd.getX()), Math.min(pStart.getY(), pEnd.getY()), Math.min(pStart.getZ(), pEnd.getZ()));
            BlockPos p2 = new BlockPos(Math.max(pStart.getX(), pEnd.getX()) + 1, Math.max(pStart.getY(), pEnd.getY()) + 1, Math.max(pStart.getZ(), pEnd.getZ()) + 1);
            AABB aabb =  new AABB(p1, p2);
            bufferAddBlockVertex(buffer, aabb, opacity, 48, 1, 167);      //选区框
            bufferAddBlockVertex(buffer, pStart.getX(), pStart.getY(), pStart.getZ(), size, opacity, 170, 1, 1); //第一个选区点
            bufferAddBlockVertex(buffer, pEnd.getX(), pEnd.getY(), pEnd.getZ(), size, opacity,1, 170, 170); //第二个选区点

            //将顶点缓冲绑定在0penGL顶点数组上
            vertexBuffer.bind();
            //将缓冲区数据上传到顶点缓冲对象，buffer包含了绘制的顶点数据。
            vertexBuffer.upload(buffer.end());
            //结束顶点缓冲的绑定，后续绘制不会在使用这个顶点缓冲对象了
            VertexBuffer.unbind();
        }

            if (vertexBuffer != null) {
                Vec3 view = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();

                //启动混合模式，控制颜色和深度值合并在一起，即RGB通道和透明度通道
                GL11.glEnable(GL11.GL_BLEND);
                //设置混合函数，设置混合函数是源颜色的透明通道和目标颜色的1-透明通道进行混合
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                //抗锯齿
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                //禁用深度测试
                GL11.glDisable(GL11.GL_DEPTH_TEST);

                //设置渲染系统的着色器为位置颜色着色器，返回的是着色器对象
                RenderSystem.setShader(GameRenderer::getPositionColorShader);

//                PoseStack matrix = event.getPoseStack();
                stack.pushPose();
                stack.translate(-view.x, -view.y, -view.z);

                vertexBuffer.bind();
                //绘制场景模型
                vertexBuffer.drawWithShader(stack.last().pose(), mat4f, RenderSystem.getShader());
                VertexBuffer.unbind();
                stack.popPose();

                GL11.glEnable(GL11.GL_DEPTH_TEST);
                //关闭混合
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
            }
        }


    public static void bufferAddBlockVertex(BufferBuilder buffer, AABB pBox,float opacity, float red, float green, float blue) {
        double x = pBox.minX;
        double y = pBox.minY;
        double z = pBox.minZ;
        double maxX = pBox.maxX;
        double maxY = pBox.maxY;
        double maxZ = pBox.maxZ;
        buffer.vertex(x, maxY, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(maxX, maxY, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(maxX, maxY, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(maxX, maxY, maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(maxX, maxY, maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, maxY, maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, maxY, maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, maxY, z).color(red, green, blue, opacity).endVertex();

        // BOTTOM
        buffer.vertex(maxX, y, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(maxX, y, maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(maxX, y, maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y, maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y, maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(maxX, y, z).color(red, green, blue, opacity).endVertex();

        // Edge 1
        buffer.vertex(maxX, y, maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(maxX, maxY, maxZ).color(red, green, blue, opacity).endVertex();

        // Edge 2
        buffer.vertex(maxX, y, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(maxX, maxY, z).color(red, green, blue, opacity).endVertex();

        // Edge 3
        buffer.vertex(x, y, maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, maxY, maxZ).color(red, green, blue, opacity).endVertex();

        // Edge 4
        buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, maxY, z).color(red, green, blue, opacity).endVertex();
    }
    public static void bufferAddBlockVertex(BufferBuilder buffer, double x, double y, double z,float size,float opacity, float red, float green, float blue) {
        bufferAddBlockVertex(buffer, x, y, z, size, size, size, opacity, red, green, blue);
    }

    public static void bufferAddBlockVertex(BufferBuilder buffer, double x, double y, double z,double maxX, double maxY, double maxZ,float opacity, float red, float green, float blue) {
        
        buffer.vertex(x, y + maxY, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x+ maxX, y + maxY, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x+ maxX, y + maxY, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x+ maxX, y + maxY, z+ maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x+ maxX, y + maxY, z+ maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y + maxY, z+ maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y + maxY, z+ maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y + maxY, z).color(red, green, blue, opacity).endVertex();

        // BOTTOM
        buffer.vertex(x+ maxX, y, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x+ maxX, y, z+ maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x+ maxX, y, z+ maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y, z+ maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y, z+ maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x+ maxX, y, z).color(red, green, blue, opacity).endVertex();

        // Edge 1
        buffer.vertex(x+ maxX, y, z+ maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x+ maxX, y + maxY, z+ maxZ).color(red, green, blue, opacity).endVertex();

        // Edge 2
        buffer.vertex(x+ maxX, y, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x+ maxX, y + maxY, z).color(red, green, blue, opacity).endVertex();

        // Edge 3
        buffer.vertex(x, y, z+ maxZ).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y + maxY, z+ maxZ).color(red, green, blue, opacity).endVertex();

        // Edge 4
        buffer.vertex(x, y, z).color(red, green, blue, opacity).endVertex();
        buffer.vertex(x, y + maxY, z).color(red, green, blue, opacity).endVertex();
    }

}
