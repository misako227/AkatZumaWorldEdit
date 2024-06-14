package com.z227.AkatZumaWorldEdit.Render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class BindInvGui {
    public static ResourceLocation CHECK_BOX = new ResourceLocation(AkatZumaWorldEdit.MODID, "textures/gui/unchecked-checkbox-128.png");
    public static Component HUDdesc1 = Component.translatable("hud.akatzuma.right");
    public static Component HUDdesc2 = Component.translatable("hud.akatzuma.ctrl_right");

    public static final IGuiOverlay InvPosHUD = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;
        ItemStack itemstack = player.getMainHandItem();
        if(itemstack.getItem() != AkatZumaWorldEdit.BindInventory.get())return;

        PlayerMapData PMD = Util.getPMD(player);
        Map<String, BlockState> invPosMap = PMD.getInvPosMap();
        CompoundTag tag = PMD.getInvPosNBT();
//        int index = PMD.getInvPosIndex();

//        int x = guiGraphics.guiWidth() / 2;
        int guiWidth = gui.getMinecraft().getWindow().getGuiScaledWidth();
        int guiHeight = gui.getMinecraft().getWindow().getGuiScaledHeight();
        int y = guiHeight - 65;
        int offset = guiWidth / 2;
        int posx = offset;

//        for (int i=1; i <= 5; i++) {
        BlockState blockState = invPosMap.get("pos");
//            int color = 0xffffff;
//            int posColor = 0xaaaaaa;
        int color = 0xaa00aa;
        int posColor = 0xffffff;
        int[] pos = tag.getIntArray("pos");
        Component blockName = blockState.getBlock().getName();
        int nameWidth = gui.getFont().width(blockName.getString());
        int posWidth = gui.getFont().width("-72345");
        int maxWidth = Math.max(nameWidth,posWidth);

        //渲染边框
        blit(CHECK_BOX, poseStack, offset - (maxWidth/2) - 4,y-17, maxWidth+8,gui.getFont().lineHeight*4+6,0, 0, 128,52,128, 52);

//        gui.getMinecraft().getItemRenderer().render();

        drawCenteredString(gui.getFont(),poseStack, blockName.getString(), posx, y-15, color );     // name
        if(pos.length == 3){
            drawCenteredString(gui.getFont(),poseStack, String.valueOf(pos[0]), posx, y-14+gui.getFont().lineHeight, posColor );        //posX
            drawCenteredString(gui.getFont(),poseStack, String.valueOf(pos[1]), posx, y-13+gui.getFont().lineHeight*2, posColor );        //posY
            drawCenteredString(gui.getFont(),poseStack, String.valueOf(pos[2]), posx, y-12+gui.getFont().lineHeight*3, posColor );        //posZ
        }else{
            drawCenteredString(gui.getFont(),poseStack, Arrays.toString(pos), posx, y-15+gui.getFont().lineHeight, posColor );        //空数组
        }

//        guiGraphics.renderItem(blockState.getBlock().asItem().getDefaultInstance(),posx - gui.getFont().lineHeight, y-25-gui.getFont().lineHeight);       //item stack

//        posx += offset;
//        }


        int height = gui.getMinecraft().getWindow().getGuiScaledHeight();
//        guiGraphics.drawString(gui.getFont(),desc1, 3, height - gui.getFont().lineHeight-2, 0xffffff);     // ctrl+滚轮
        drawString(gui.getFont(),poseStack, HUDdesc1.getString(), 3, height - (gui.getFont().lineHeight * 2) - 4, 0xffffff, true);     // 右键
        drawString(gui.getFont(),poseStack, HUDdesc2.getString(), 3, height - gui.getFont().lineHeight-2, 0xffffff,true);     // ctrl+右键
//        gui.getMinecraft().gui.

    });


    @SubscribeEvent
    public static void onRenderText(RegisterGuiOverlaysEvent event){

        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(),"inv_pos_hud", InvPosHUD);
//        event.registerAboveAll("inv_pos_hud", InvPosHUD);

    }


    public static void innerBlit(ResourceLocation pAtlasLocation, PoseStack pose, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV){
        RenderSystem.setShaderTexture(0, pAtlasLocation);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = pose.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, (float)pX1, (float)pY1, (float)pBlitOffset).uv(pMinU, pMinV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX1, (float)pY2, (float)pBlitOffset).uv(pMinU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX2, (float)pY2, (float)pBlitOffset).uv(pMaxU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX2, (float)pY1, (float)pBlitOffset).uv(pMaxU, pMinV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
    public static void blit(ResourceLocation pAtlasLocation, PoseStack pose,int pX1, int pX2, int pY1, int pY2, int pBlitOffset, int pUWidth, int pVHeight, float pUOffset, float pVOffset, int pTextureWidth, int pTextureHeight) {
        innerBlit(pAtlasLocation,pose, pX1, pX2, pY1, pY2, pBlitOffset, (pUOffset + 0.0F) / (float)pTextureWidth, (pUOffset + (float)pUWidth) / (float)pTextureWidth, (pVOffset + 0.0F) / (float)pTextureHeight, (pVOffset + (float)pVHeight) / (float)pTextureHeight);
    }

    public static void blit(ResourceLocation pAtlasLocation, PoseStack pose, int pX, int pY, int pWidth, int pHeight, float pUOffset, float pVOffset, int pUWidth, int pVHeight, int pTextureWidth, int pTextureHeight) {
        blit(pAtlasLocation, pose, pX, pX + pWidth, pY, pY + pHeight, 0, pUWidth, pVHeight, pUOffset, pVOffset, pTextureWidth, pTextureHeight);
    }


//    public static int drawString(Font pFont, PoseStack pose, @Nullable String pText, float pX, float pY, int pColor, boolean pDropShadow) {
//        if (pText == null) {
//            return 0;
//        } else {
//            MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
//            int i = pFont.drawInBatch(pText, (float)pX, (float)pY, pColor, pDropShadow, pose.last().pose(), multibuffersource$buffersource,pFont.isBidirectional(),  0, 15728880);
////            this.flushIfUnmanaged();
//            return i;
//        }
//    }
    public static void drawString(Font pFont, PoseStack pose, @Nullable String pText, float pX, float pY, int pColor, boolean pDropShadow) {
        pFont.draw(pose, pText, pX, pY, pColor);

    }

    public static void drawCenteredString(Font pFont, PoseStack pose, String pText, int pX, int pY, int pColor) {
        drawString(pFont,pose, pText, pX - pFont.width(pText) / 2, pY, pColor,true);
    }



}
