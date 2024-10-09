//package com.z227.AkatZumaWorldEdit.Render;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.*;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//
//public class RenderBorder {
//    private static final ResourceLocation FORCEFIELD_LOCATION = new ResourceLocation("textures/misc/forcefield.png");
//    public static void renderWorldBorder(AABB pBox, float opacity, float red, float green, float blue) {
//
//        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
////        WorldBorder worldborder = this.level.getWorldBorder();
////        double d0 = (double)(this.minecraft.options.getEffectiveRenderDistance() * 16);
////        if (!(pCamera.getPosition().x < worldborder.getMaxX() - d0) || !(pCamera.getPosition().x > worldborder.getMinX() + d0) || !(pCamera.getPosition().z < worldborder.getMaxZ() - d0) || !(pCamera.getPosition().z > worldborder.getMinZ() + d0)) {
//
//            RenderSystem.enableBlend();
//            RenderSystem.enableDepthTest();
//            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//            RenderSystem.setShaderTexture(0, FORCEFIELD_LOCATION);
//            RenderSystem.depthMask(Minecraft.useShaderTransparency());
//            PoseStack posestack = RenderSystem.getModelViewStack();
//            posestack.pushPose();
//            RenderSystem.applyModelViewMatrix();
////            int i = worldborder.getStatus().getColor();
////            float f = (float)(i >> 16 & 255) / 255.0F;
////            float f1 = (float)(i >> 8 & 255) / 255.0F;
////            float f2 = (float)(i & 255) / 255.0F;
//            float f=1, f1 = 1, f2 = 1;
//            RenderSystem.setShaderColor(f, f1, f2, (float)1);
//            RenderSystem.setShader(GameRenderer::getPositionTexShader);
//            RenderSystem.polygonOffset(-3.0F, -3.0F);
//            RenderSystem.enablePolygonOffset();
//            RenderSystem.disableCull();
////            float f3 = (float)(Util.getMillis() % 3000L) / 3000.0F;
////            float f4 = (float)(-Mth.frac(pCamera.getPosition().y * 0.5D));
////            float f5 = f4 + (float)d4;
//        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
////            double d5 = Math.max((double)Mth.floor(d3 - d0), worldborder.getMinZ());
////            double d6 = Math.min((double)Mth.ceil(d3 + d0), worldborder.getMaxZ());
////            float f6 = (float)(Mth.floor(d5) & 1) * 0.5F;
//
//
//        double x = pBox.minX;
//        double y = pBox.minY;
//        double z = pBox.minZ;
//        double maxX = pBox.maxX;
//        double maxY = pBox.maxY;
//        double maxZ = pBox.maxZ;
////        buffer.vertex(x, y, z).color(red, green, blue, opacity).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0.0F, 0.0F, 1.0F).endVertex();
////        buffer.vertex(x, maxY, z).color(red, green, blue, opacity).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0.0F, 0.0F, 1.0F).endVertex();
////        buffer.vertex(x, maxY, maxZ).color(red, green, blue, opacity).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0.0F, 0.0F, 1.0F).endVertex();
////        buffer.vertex(x, y, maxZ).color(red, green, blue, opacity).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(0.0F, 0.0F, 1.0F).endVertex();
//
//        buffer.vertex(x, y, z).uv(0, (float) maxY).endVertex();
//        buffer.vertex(x, maxY, z).uv(0, 0).endVertex();
//        buffer.vertex(x, maxY, maxZ).uv((float) maxX, 0).endVertex();
//        buffer.vertex(x, y, maxZ).uv((float) maxX, (float) maxY).endVertex();
//        Vec3 view = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();
//        posestack.translate(-view.x, -view.y, -view.z);
//        BufferUploader.drawWithShader(buffer.end());
//            RenderSystem.enableCull();
//            RenderSystem.polygonOffset(0.0F, 0.0F);
//            RenderSystem.disablePolygonOffset();
//            RenderSystem.disableBlend();
//            RenderSystem.defaultBlendFunc();
//            posestack.popPose();
//            RenderSystem.applyModelViewMatrix();
//            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//            RenderSystem.depthMask(true);
//
//    }
//}
