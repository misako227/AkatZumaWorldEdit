package com.z227.ImGuiRender.mode;

import com.mojang.blaze3d.vertex.*;
import com.z227.AkatZumaWorldEdit.Render.renderLine.RenderLineBox;
import com.z227.ImGuiRender.EditModeData;
import com.z227.ImGuiRender.render.renderLine.RenderImGuiLineBox;
import com.z227.ImGuiRender.util.PosUtilImGui;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiMouseButton;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class SelectionMode extends ModeBase{

    public String mode = "selection";
    public EditModeData editModeData;

    private float scale;
    private boolean isSelected;
    private AABB boundingBox;

    public boolean isDragging = false; // 拖拽状态



    public SelectionMode(){
        vertexBuffer = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);
        vertexBuffer.close();
        this.hoverText = "选择模式";
        editModeData = EditModeData.getInstance();
        scale = 2f;
    }
    @Override
    public void render() {
        ImGui.text(mode);
        ImGui.text("pos1:" + editModeData.getPos1());
        ImGui.text("pos2:" + editModeData.getPos2());
        ImGui.text("pos3:" + editModeData.getCenterPos());
        ImGui.text("is sel:" + editModeData.isSelection());
        ImGui.text("pos index:" + editModeData.getPosIndex());
        ImGui.text("isDragging:" + isDragging);

        //下拉框
//        if (ImGui.beginCombo("功能", mode)) {
//            if (ImGui.selectable("复制")) {
//                mode = "复制";
//            }
//        }

    }

    @Override
    public void mouseUpdate() {
        ImGuiIO io = ImGui.getIO();
        if(io.getWantCaptureMouse()) return;

        if(editModeData.isSelection()){     //已经有选区
            if (io.getMouseDown(ImGuiMouseButton.Left) ) {
                HitResult hitResult = PosUtilImGui.getScreenToWorldPos();
                if(hitResult != null){
                    BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
                    if(pos.equals(editModeData.getPos1())){
                        editModeData.setPosIndex(1);
                        return;
                    }else if(pos.equals(editModeData.getPos2())){
                        editModeData.setPosIndex(2);
                        return;
                    }else if(pos.equals(editModeData.centerPos)){
                        editModeData.setPosIndex(3);
                        return;
                    }
                }
            }
        }
        // 左键是否按下
        if (!isDragging && ImGui.isMouseDown(ImGuiMouseButton.Left)) {
            isDragging = true;

            HitResult hitResult = PosUtilImGui.getScreenToWorldPos();
            if(hitResult != null){
                editModeData.setPos1(((BlockHitResult) hitResult).getBlockPos());
                updateLineVertex();

            }
        }

        // 检测鼠标是否在拖动（需要配合位置判断）
        if (isDragging && ImGui.isMouseDragging(ImGuiMouseButton.Left) ) {
            HitResult hitResult = PosUtilImGui.getScreenToWorldPos();
            if(hitResult != null){
                editModeData.setPos2(((BlockHitResult) hitResult).getBlockPos());
                updateLineVertex();

            }
            return;
        }

        // 检测鼠标是否被释放
        if (isDragging && ImGui.isMouseReleased(ImGuiMouseButton.Left)) {

            isDragging = false;
            HitResult hitResult = PosUtilImGui.getScreenToWorldPos();
            if(hitResult != null){
                editModeData.setPos2(((BlockHitResult) hitResult).getBlockPos());
                updateLineVertex();
                editModeData.setSelection(true);

            }

        }











    }

    @Override
    public void levelRender(PoseStack stack, Matrix4f mat4f, Vec3 view) {
        RenderImGuiLineBox.renderBlockLine(vertexBuffer, stack, mat4f, view);
    }


    public void updateScale() {
        if(editModeData.gizmoPos == null) return;
        Player player = Minecraft.getInstance().player;
        BlockPos blockPos =  player.getOnPos();
        double d = PosUtilImGui.calcDistance(blockPos, editModeData.gizmoPos);
        this.scale = (float) (d / 10.0);
        if(scale < 2f) scale = 2f;
    }

    public void updateLineVertex() {
        BlockPos pStart = editModeData.getPos1();
        BlockPos pEnd = editModeData.getPos2();

        if (pStart == null || pEnd == null) return;
        updateVertexBuffer();


        editModeData.setCenterPos(editModeData.calculateCenterPos());
        BlockPos center = editModeData.getCenterPos();

        if (vertexBuffer == null || refreshVertexBuffer) {
//            refreshVertexBuffer = false;
            //STATIC 表示缓冲区不会经常修改
            vertexBuffer = new VertexBuffer(VertexBuffer.Usage.DYNAMIC);

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

            RenderLineBox.bufferAddBlockVertex(buffer, aabb, opacity, 1, 1, 1);      //选区框
            RenderLineBox.bufferAddBlockVertex(buffer, pStart.getX(), pStart.getY(), pStart.getZ(), size, opacity, 170, 1, 1); //第一个选区点
            RenderLineBox.bufferAddBlockVertex(buffer, pEnd.getX(), pEnd.getY(), pEnd.getZ(), size, opacity,1, 170, 170); //第二个选区点
            RenderLineBox.bufferAddBlockVertex(buffer, center.getX(), center.getY(), center.getZ(), size, opacity,170, 1, 170); //选区中心点

            updateScale();
            SelectionModeRender.drawAxes(buffer, scale, editModeData.gizmoPos);

            //将顶点缓冲绑定在0penGL顶点数组上
            vertexBuffer.bind();
            //将缓冲区数据上传到顶点缓冲对象，buffer包含了绘制的顶点数据。
            vertexBuffer.upload(buffer.end());
//            结束顶点缓冲的绑定，后续绘制不会在使用这个顶点缓冲对象了
            VertexBuffer.unbind();
        }

    }




//    public void updateLineVertex() {
//        BlockPos pStart = editModeData.getPos1();
//        BlockPos pEnd = editModeData.getPos2();
//
//        if (pStart == null || pEnd == null) return;
//        updateVertexBuffer();
//
//        editModeData.setCenterPos(editModeData.calculateCenterPos());
//        BlockPos center = editModeData.getCenterPos();
//
//        if (vertexBuffer == null || refreshVertexBuffer) {
//            refreshVertexBuffer = false;
//            //STATIC 表示缓冲区不会经常修改
//            vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
//
//            //tess用于在OpenGL中创建和处理几何图形，用于在GPU生成线，三角形等基本形状
//            Tesselator tessellator = Tesselator.getInstance();
//            //BufferBuilder 用于构建顶点缓冲区，用于将几何数据转化为0penGL可以理解的定点格式
//            BufferBuilder buffer = tessellator.getBuilder();
//
//            //设置BufferBuilder的绘制模式和顶点格式
//            //VertexFormat.Mode.DEBUG 表示绘制模式为调试线,
//            //DefaultVertexFormat.POSITION_COLOR 表示顶点格式为位置+颜色模式，即每个顶点具有位置信息和颜色信息
//            buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
//
//            float opacity = 1F;
//            final float size = 1.0f;
//
//            //获取标线AABB
//            BlockPos p1 = new BlockPos(Math.min(pStart.getX(), pEnd.getX()), Math.min(pStart.getY(), pEnd.getY()), Math.min(pStart.getZ(), pEnd.getZ()));
//            BlockPos p2 = new BlockPos(Math.max(pStart.getX(), pEnd.getX()) + 1, Math.max(pStart.getY(), pEnd.getY()) + 1, Math.max(pStart.getZ(), pEnd.getZ()) + 1);
//            AABB aabb =  new AABB(p1, p2);
//
//            RenderLineBox.bufferAddBlockVertex( buffer, aabb, opacity, 1, 1, 1);      //选区框
//            RenderLineBox.bufferAddBlockVertex(buffer, pStart.getX(), pStart.getY(), pStart.getZ(), size, opacity, 170, 1, 1); //第一个选区点
//            RenderLineBox.bufferAddBlockVertex(buffer, pEnd.getX(), pEnd.getY(), pEnd.getZ(), size, opacity,1, 170, 170); //第二个选区点
//            RenderLineBox.bufferAddBlockVertex(buffer, center.getX(), center.getY(), center.getZ(), size, opacity,170, 1, 170); //选区中心点
//
//            //将顶点缓冲绑定在0penGL顶点数组上
//            vertexBuffer.bind();
//            //将缓冲区数据上传到顶点缓冲对象，buffer包含了绘制的顶点数据。
//            vertexBuffer.upload(buffer.end());
////            结束顶点缓冲的绑定，后续绘制不会在使用这个顶点缓冲对象了
//            VertexBuffer.unbind();
//        }
//
//    }
}
