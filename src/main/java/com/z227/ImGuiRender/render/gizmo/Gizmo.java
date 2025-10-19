package com.z227.ImGuiRender.render.gizmo;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.z227.ImGuiRender.util.PosUtilImGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class Gizmo {
    private Vec3i position;
    private float scale;
    private boolean isSelected;
    private final AABB boundingBox;

    public Gizmo(Vec3i position, float scale) {
        this.position = position;
        this.scale = scale;
        this.isSelected = false;
        this.boundingBox = new AABB(
                position.getX() - scale, position.getY() - scale, position.getZ() - scale,
                position.getX() + scale, position.getY() + scale, position.getZ() + scale
        );
    }

    public Vec3i getPosition() {
        return position;
    }

    public void setPosition(Vec3i position) {
        this.position = position;
    }

    public float getScale() {
        return scale;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public AABB getBoundingBox() {
        return boundingBox;
    }

    public void updateScale() {
        Player player = Minecraft.getInstance().player;
        BlockPos blockPos =  player.getOnPos();
        double d = PosUtilImGui.calcDistance(blockPos, position);
        this.scale = (float) (d / 10.0);
        if(scale < 2f) scale = 2f;
    }

    // 渲染Gizmo
    public void render(PoseStack poseStack, MultiBufferSource bufferSource) {
        updateScale();
        poseStack.pushPose();

        // 移动到Gizmo位置
        poseStack.translate(position.getX(), position.getY(), position.getZ());

        // 绘制坐标轴
        drawAxes(poseStack, bufferSource);

        // 绘制中心立方体
//        drawCenterCube(poseStack, bufferSource);

        poseStack.popPose();
    }

    // 绘制坐标轴
    private void drawAxes(PoseStack poseStack, MultiBufferSource bufferSource) {
        float axisLength = scale * 2.0f;
        float axisThickness = scale * 0.05f;

        // X轴 (红色)
        drawLine(poseStack, bufferSource,
                0, 0, 0,
                axisLength, 0, 0,
                axisThickness,
                1.0f, 0.0f, 0.0f, 1.0f);

        // Y轴 (绿色)
        drawLine(poseStack, bufferSource,
                0, 0, 0,
                0, axisLength, 0,
                axisThickness,
                0.0f, 1.0f, 0.0f, 1.0f);

        // Z轴 (蓝色)
        drawLine(poseStack, bufferSource,
                0, 0, 0,
                0, 0, axisLength,
                axisThickness,
                0.0f, 0.0f, 1.0f, 1.0f);

        // 绘制轴尖锥
        drawAxisCone(poseStack, bufferSource, Axis.XP, axisLength, 1.0f, 0.0f, 0.0f);
        drawAxisCone(poseStack, bufferSource, Axis.YP, axisLength, 0.0f, 1.0f, 0.0f);
        drawAxisCone(poseStack, bufferSource, Axis.ZP, axisLength, 0.0f, 0.0f, 1.0f);


    }

    // 绘制中心立方体
    private void drawCenterCube(PoseStack poseStack, MultiBufferSource bufferSource) {
        float cubeSize = scale * 1.0f;

        // 根据选中状态设置颜色
        float r = isSelected ? 1.0f : 1.0f;
        float g = isSelected ? 1.0f : 1.0f;
        float b = isSelected ? 0.0f : 1.0f;
        float a = 1.0f;

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.lines());
        Matrix4f matrix = poseStack.last().pose();

        // 绘制立方体线框
        // 底部
        consumer.vertex(matrix, -cubeSize, -cubeSize, -cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, cubeSize, -cubeSize, -cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();

        consumer.vertex(matrix, cubeSize, -cubeSize, -cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, cubeSize, -cubeSize, cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();

        consumer.vertex(matrix, cubeSize, -cubeSize, cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, -cubeSize, -cubeSize, cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();

        consumer.vertex(matrix, -cubeSize, -cubeSize, cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, -cubeSize, -cubeSize, -cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();

        // 顶部
        consumer.vertex(matrix, -cubeSize, cubeSize, -cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, cubeSize, cubeSize, -cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();

        consumer.vertex(matrix, cubeSize, cubeSize, -cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, cubeSize, cubeSize, cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();

        consumer.vertex(matrix, cubeSize, cubeSize, cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, -cubeSize, cubeSize, cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();

        consumer.vertex(matrix, -cubeSize, cubeSize, cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, -cubeSize, cubeSize, -cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();

        // 垂直线
        consumer.vertex(matrix, -cubeSize, -cubeSize, -cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, -cubeSize, cubeSize, -cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();

        consumer.vertex(matrix, cubeSize, -cubeSize, -cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, cubeSize, cubeSize, -cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();

        consumer.vertex(matrix, cubeSize, -cubeSize, cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, cubeSize, cubeSize, cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();

        consumer.vertex(matrix, -cubeSize, -cubeSize, cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, -cubeSize, cubeSize, cubeSize).color(r, g, b, a).normal(0, 1, 0).endVertex();
    }

    // 绘制线
    private void drawLine(PoseStack poseStack, MultiBufferSource bufferSource,
                          float x1, float y1, float z1,
                          float x2, float y2, float z2,
                          float thickness, float r, float g, float b, float a) {
        poseStack.pushPose();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.LINES);
        Matrix4f matrix = poseStack.last().pose();

        // 设置线宽
        RenderSystem.lineWidth(thickness);

        // 绘制线
        consumer.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(0, 1, 0).endVertex();
        consumer.vertex(matrix, x2, y2, z2).color(r, g, b, a).normal(0, 1, 0).endVertex();

        poseStack.popPose();
    }

    // 绘制轴尖锥
    private void drawAxisCone(PoseStack poseStack, MultiBufferSource bufferSource,
                              Axis axis, float length, float r, float g, float b) {
        poseStack.pushPose();

        // 移动到轴末端
        if (axis.XP.equals(axis)) {
            poseStack.translate(length, 0, 0);
        } else if (axis.YP.equals(axis)) {
            poseStack.translate(0, length, 0);
        } else if (axis.ZP.equals(axis)) {
            poseStack.translate(0, 0, length);
        }

        // 旋转到正确方向
        if (axis.XP.equals(axis)) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(90));
        } else if (axis.YP.equals(axis)) { // 不需要旋转
        } else if (axis.ZP.equals(axis)) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
        }

        float coneHeight = scale * 0.3f;
        float coneRadius = scale * 0.1f;
        int segments = 8;

        // 绘制圆锥
//        drawAxes(poseStack, bufferSource, coneHeight, coneRadius, segments, r, g, b, 1.0f);

        poseStack.popPose();
    }



}