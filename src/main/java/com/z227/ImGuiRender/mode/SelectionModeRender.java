package com.z227.ImGuiRender.mode;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;

public class SelectionModeRender {

    public static void drawAxes(BufferBuilder bufferSource, float scale, BlockPos gizmoPos) {
        if(gizmoPos == null) return;
        float axisLength = scale * 2.0f;
        float axisThickness = scale * 0.05f;

        // X轴 (红色)
        drawLine(bufferSource,
                gizmoPos.getX(),gizmoPos.getY(),gizmoPos.getZ(),
                gizmoPos.getX() + axisLength, gizmoPos.getY(),gizmoPos.getZ(),
                axisThickness,
                1.0f, 0.0f, 0.0f, 1.0f);

        // Y轴 (绿色)
        drawLine(bufferSource,
                gizmoPos.getX(),gizmoPos.getY(),gizmoPos.getZ(),
                gizmoPos.getX(), gizmoPos.getY() + axisLength, gizmoPos.getZ(),
                axisThickness,
                0.0f, 1.0f, 0.0f, 1.0f);

        // Z轴 (蓝色)
        drawLine(bufferSource,
                gizmoPos.getX(),gizmoPos.getY(),gizmoPos.getZ(),
                gizmoPos.getX(), gizmoPos.getY() , gizmoPos.getZ() + axisLength,
                axisThickness,
                0.0f, 0.0f, 1.0f, 1.0f);



    }

    // 绘制线
    public static void drawLine(BufferBuilder bufferSource,
                          float x1, float y1, float z1,
                          float x2, float y2, float z2,
                          float thickness, float r, float g, float b, float a) {

        // 设置线宽
        RenderSystem.lineWidth(thickness);

        // 绘制线
        bufferSource.vertex(x1, y1, z1).color(r, g, b, a).normal(0, 1, 0).endVertex();
        bufferSource.vertex(x2, y2, z2).color(r, g, b, a).normal(0, 1, 0).endVertex();


    }


    public static void renderCone(PoseStack poseStack, float radius, float height,
                                  int segments, float red, float green, float blue, float alpha) {

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        Matrix4f matrix = poseStack.last().pose();

        // 设置渲染状态
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableCull(); // 禁用面剔除以显示内部

        // 渲染侧面（三角形扇形）
        buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i < segments; i++) {
            float angle1 = (float) (2 * Math.PI * i / segments);
            float angle2 = (float) (2 * Math.PI * (i + 1) / segments);

            float x1 = radius * (float) Math.cos(angle1);
            float z1 = radius * (float) Math.sin(angle1);
            float x2 = radius * (float) Math.cos(angle2);
            float z2 = radius * (float) Math.sin(angle2);

            // 顶点（圆锥尖端）
            buffer.vertex(matrix, 0, height, 0).color(red, green, blue, alpha).endVertex();
            // 底面边缘点1
            buffer.vertex(matrix, x1, 0, z1).color(red, green, blue, alpha).endVertex();
            // 底面边缘点2
            buffer.vertex(matrix, x2, 0, z2).color(red, green, blue, alpha).endVertex();
        }

        tesselator.end();

        // 渲染底面（圆形）
        buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);

        for (int i = 0; i < segments; i++) {
            float angle1 = (float) (2 * Math.PI * i / segments);
            float angle2 = (float) (2 * Math.PI * (i + 1) / segments);

            float x1 = radius * (float) Math.cos(angle1);
            float z1 = radius * (float) Math.sin(angle1);
            float x2 = radius * (float) Math.cos(angle2);
            float z2 = radius * (float) Math.sin(angle2);

            // 底面中心点
            buffer.vertex(matrix, 0, 0, 0).color(red, green, blue, alpha).endVertex();
            // 边缘点1
            buffer.vertex(matrix, x1, 0, z1).color(red, green, blue, alpha).endVertex();
            // 边缘点2
            buffer.vertex(matrix, x2, 0, z2).color(red, green, blue, alpha).endVertex();
        }

        tesselator.end();

        // 恢复渲染状态
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }


    public static void renderCyl(PoseStack poseStack){
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();

        Matrix4f matrix = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        float red = 0.0f;
        float green = 1.0f;
        float blue = 0.0f;
        float alpha = 0.8f;

        // 渲染侧面（三角形扇形）
        buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        // 顶点（圆锥尖端）
        buffer.vertex(matrix, 0, 2, 0).color(red, green, blue, alpha).endVertex();
        // 底面边缘点1
        buffer.vertex(matrix, 1, 0, 1).color(red, green, blue, alpha).endVertex();
        // 底面边缘点2
        buffer.vertex(matrix, 1, 0, 0).color(red, green, blue, alpha).endVertex();

        tesselator.end();
    }
}
