//package com.z227.AkatZumaWorldEdit.Render;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexBuffer;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
//import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.LineBase;
//import com.z227.AkatZumaWorldEdit.utilities.Util;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.LevelRenderer;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import org.joml.Matrix3f;
//import org.joml.Matrix4f;
//import org.lwjgl.opengl.GL11;
//
//import java.util.List;
//
//public class RenderCurveLine {
//    private static VertexBuffer vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
//    public static boolean requestedRefresh = false;
//
//    public static void render(VertexConsumer vertexConsumer, PoseStack stack, Player player)
//    {
//        PlayerMapData PMD = Util.getPMD(player);
//        LineBase lineBase = PMD.getLineBase();
//
//
//        List<BlockPos> posList = lineBase.getPosList();
//        if(posList.size() == 0 ) return;
//
//        //坐标变换
//        Vec3 camvec = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
//
//
//
//
//        //渲染
//        stack.pushPose();
//        stack.translate( - camvec.x,  - camvec.y,  - camvec.z);
//
//
//        //渲染曲线
//        List<BlockPos> curvePosList = lineBase.getCurvePosList();
//        if(curvePosList.size() > 1){
//            for(int i = 0; i <= curvePosList.size()-1; i++){
//                BlockPos p1 = curvePosList.get(i);
//                if(i < curvePosList.size()-1){
//
//                    BlockPos r2 = p1.offset(1,1,1);
////                    renderLine(p1, linePos2, stack, vertexConsumer, 1, 255, 255, 1);
//                    AABB aabb2 =  new AABB(p1, r2);
//                    LevelRenderer.renderLineBox(stack, vertexConsumer, aabb2, 67, 17, 151, 1);
//                }
//            }
//        }
//
//
//        for(int i = 0; i <= posList.size()-1; i++){
//            BlockPos p1 = posList.get(i);
//            BlockPos p2 = p1.offset(1,1,1);
//            AABB aabb =  new AABB(p1, p2);
//            //渲染控制点（白色）
//            LevelRenderer.renderLineBox(stack, vertexConsumer, aabb, 1, 1, 1, 1);
//
//            //渲染直线
//            if(i < posList.size()-1){
//                BlockPos linePos2 = posList.get(i+1);
//                int r,g,b;
//                if(i % 2 == 0){
//                    r = 1;
//                    g = 200;
//                    b = 200;
//                }else{
//                    r = 200;
//                    g = 55;
//                    b = 55;
//                }
//                renderLine(p1, linePos2, stack, vertexConsumer, r, g, b, 1);
//            }
//
//        }
//
//        List<BlockPos> innPosList = lineBase.getInnerCurvePosList();
//
////        for(BlockPos pos: innPosList){
////            LevelRenderer.renderLineBox(stack, vertexConsumer, new AABB(pos, pos.offset(1,1,1)), 255, 255, 1, 1);
////        }
//
//        innPosList.forEach(pos->{LevelRenderer.renderLineBox(stack, vertexConsumer, new AABB(pos, pos.offset(1,1,1)), 255, 255, 1, 1);});
//
//
//
//
//
//        BlockPos rightPos = lineBase.getRightPos();
//        if(rightPos != null){
//            BlockPos r2 = rightPos.offset(1,1,1);
//            AABB aabb2 =  new AABB(rightPos, r2);
//            LevelRenderer.renderLineBox(stack, vertexConsumer, aabb2, 1, 255, 255, 1);
//
//        }
//
//
//
//
//        stack.popPose();
//
//
//    }
//
//    public static void renderLine(BlockPos pos1, BlockPos pos2,PoseStack pPoseStack, VertexConsumer pConsumer, float pRed, float pGreen, float pBlue, float pAlpha) {
//
//        renderLine(pPoseStack, pConsumer, pos1.getX()+0.5f,(float)pos1.getY()+1f,pos1.getZ()+0.5f,pos2.getX()+0.5f,pos2.getY()+1f,pos2.getZ()+0.5f, pRed, pGreen, pBlue, pAlpha);
//    }
//
//
//    public static void renderLine(PoseStack pPoseStack, VertexConsumer pConsumer, float pMinX, float pMinY, float pMinZ, float pMaxX, float pMaxY, float pMaxZ, float pRed, float pGreen, float pBlue, float pAlpha) {
//        GL11.glEnable(GL11.GL_LINE_SMOOTH);
//        Matrix4f matrix4f = pPoseStack.last().pose();
//        Matrix3f matrix3f = pPoseStack.last().normal();
////        float f = (float)pMinX;
////        float f1 = (float)pMinY;
////        float f2 = (float)pMinZ;
////        float f3 = (float)pMaxX;
////        float f4 = (float)pMaxY;
////        float f5 = (float)pMaxZ;
//
//        pConsumer.vertex(matrix4f, pMinX, pMinY, pMinZ).color(pRed, pGreen, pBlue, pAlpha).normal(matrix3f, 1.0F, 0.0F, 1.0F).endVertex();
//        pConsumer.vertex(matrix4f, pMaxX, pMaxY, pMaxZ).color(pBlue, pGreen, pRed, pAlpha).normal(matrix3f, 1.0F, 0.0F, 1.0F).endVertex();
//
//        GL11.glDisable(GL11.GL_LINE_SMOOTH);
//    }
//
//
//
//}
