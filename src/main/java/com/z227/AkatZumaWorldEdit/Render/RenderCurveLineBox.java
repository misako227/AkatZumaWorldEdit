package com.z227.AkatZumaWorldEdit.Render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.LineBase;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderCurveLineBox {

    private static VertexBuffer vertexBuffer;
    public static boolean requestedRefresh = false;

    public static void updateVertexBuffer() {
        vertexBuffer = null;
        requestedRefresh = true;
    }

    public static void renderBlockLine(PoseStack stack, Player player, Matrix4f mat4f, Vec3 view) {
//        if (controlPosList.size() == 0 || curvePosList.size() == 0) return;
        if (vertexBuffer == null || requestedRefresh) {
            PlayerMapData PMD = Util.getPMD(player);
            LineBase lineBase = PMD.getLineBase();


            List<BlockPos> posList = lineBase.getPosList();
            if(posList.size() == 0 ) return;

            requestedRefresh = false;
            //STATIC 表示缓冲区不会经常修改
            vertexBuffer = new VertexBuffer();

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

            //曲线
            List<BlockPos> curvePosList = lineBase.getCurvePosList();
            for(BlockPos pos : curvePosList){
                RenderLineBox.bufferAddBlockVertex(buffer, pos.getX(), pos.getY(), pos.getZ(), size, opacity, 67, 17, 151); //第一个选区点
            }

            for(int i = 0; i <= posList.size()-1; i++){
                BlockPos p1 = posList.get(i);
                BlockPos p2 = p1.offset(1,1,1);
                AABB aabb =  new AABB(p1, p2);
                //渲染控制点（白色）
                RenderLineBox.bufferAddBlockVertex(buffer, aabb, 1, 1, 1, 1);

                //渲染直线
                if(i < posList.size()-1){
                    BlockPos linePos2 = posList.get(i+1);
                    int r,g,b;
                    if(i % 2 == 0){
                        r = 1;
                        g = 200;
                        b = 200;
                    }else{
                        r = 200;
                        g = 55;
                        b = 55;
                    }
                    renderLine(p1, linePos2,buffer, r, g, b, 1);
                }

            }


            //渲染右键选中的点位（红色）
            BlockPos rightPos = lineBase.getRightPos();
            if(rightPos != null){
                RenderLineBox.bufferAddBlockVertex(buffer, rightPos.getX(),rightPos.getY(),rightPos.getZ(), size, opacity, 1, 255, 255);
            }



            //将顶点缓冲绑定在0penGL顶点数组上
            vertexBuffer.bind();
            //将缓冲区数据上传到顶点缓冲对象，buffer包含了绘制的顶点数据。
//            vertexBuffer.upload(buffer.end());
            buffer.end();
            //结束顶点缓冲的绑定，后续绘制不会在使用这个顶点缓冲对象了
            VertexBuffer.unbind();
        }

        if (vertexBuffer != null) {
//            Vec3 view = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();

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


    public static void renderLine(BlockPos pos1, BlockPos pos2,VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, float pAlpha) {

        renderLine(pConsumer, pos1.getX()+0.5f,(float)pos1.getY()+0.5f,pos1.getZ()+0.5f,pos2.getX()+0.5f,pos2.getY()+0.5f,pos2.getZ()+0.5f, pRed, pGreen, pBlue, pAlpha);
    }


    public static void renderLine(VertexConsumer pConsumer, float pMinX, float pMinY, float pMinZ, float pMaxX, float pMaxY, float pMaxZ, float pRed, float pGreen, float pBlue, float pAlpha) {
        pConsumer.vertex(pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).endVertex();
        pConsumer.vertex(pMaxX, pMaxY, pMaxZ).color(pBlue, pGreen, pRed, pAlpha).endVertex();
    }
}
