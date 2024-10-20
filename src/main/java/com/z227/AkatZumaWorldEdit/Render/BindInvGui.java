package com.z227.AkatZumaWorldEdit.Render;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Commands.brush.BrushBase;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Items.BindInventoryItem;
import com.z227.AkatZumaWorldEdit.Items.QueryBlockStateItem;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class BindInvGui {
    public static ResourceLocation CHECK_BOX = new ResourceLocation(AkatZumaWorldEdit.MODID, "textures/gui/unchecked-checkbox-128.png");
    public static Component HUDdesc1 = Component.translatable("hud.akatzuma.right");
    public static Component HUDdesc2 = Component.translatable("hud.akatzuma.ctrl_right");

//    public static Component BrushShapeHUD = Component.translatable("hud.akatzuma.brush_shape");
//    public static Component BrushMaskModeHUD = Component.translatable("hud.akatzuma.mask_mode");
    public static Component BrushMaskModeWhite = Component.translatable("hud.akatzuma.mask_mode_white");
    public static Component BrushMaskModeBlack = Component.translatable("hud.akatzuma.mask_mode_black");

    public static final IGuiOverlay InvPosHUD = ((gui, poseStack, partialTick, screenWidth, screenHeight) -> {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;
        Item item = player.getMainHandItem().getItem();

        PlayerMapData PMD = Util.getPMD(player);

        int guiWidth = gui.getMinecraft().getWindow().getGuiScaledWidth();
        int guiHeight = gui.getMinecraft().getWindow().getGuiScaledHeight();
        int y = guiHeight - 65;
        int offset = guiWidth / 2;
        int posx = offset;
        if(item instanceof BindInventoryItem){
            renderInvPosHUD(gui, poseStack, partialTick, screenWidth, screenHeight, PMD);
            return;
        };
        if(item instanceof QueryBlockStateItem){
            renderQueryItemHUD(gui, poseStack, partialTick, screenWidth, screenHeight, PMD);
            return;
        };

        if(PMD.getBrushMap().isEmpty()) return;
        BrushBase brushBase =  PMD.getBrushMap().get(item);
        if(brushBase == null) return;
        renderBrushHUD(gui, poseStack, partialTick, screenWidth, screenHeight, brushBase);




    });

    private static void renderBrushHUD(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight, BrushBase brushBase) {


        drawString(gui.getFont(), poseStack, brushBase.getShape(),3, screenHeight - (gui.getFont().lineHeight * 6) - 4, 0xffffff);     //形状
        drawString(gui.getFont(), poseStack, brushBase.isMaskFlag() ? BrushMaskModeWhite.getString() : BrushMaskModeBlack.getString(), 3, screenHeight - (gui.getFont().lineHeight * 5) - 4, 0xffffff);     // 右键



        if(brushBase.getMaskMap() == null)return;
        int n = 0;
        for (BlockState blockState : brushBase.getMaskMap().keySet()){
//            poseStack.renderItem(blockState.getBlock().asItem().getDefaultInstance(),3 + n * 17, height - 40 ); //
            renderItem(poseStack,blockState.getBlock().asItem().getDefaultInstance(), 3 + n * 17, screenHeight - 40 , 0, 0);
            n++;
        }

    }

    @SubscribeEvent
    public static void onRenderText(RegisterGuiOverlaysEvent event){

        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(),"inv_pos_hud", InvPosHUD);
//        event.registerAboveAll("inv_pos_hud", InvPosHUD);

    }

    public static void renderInvPosHUD(ForgeGui gui, PoseStack poseStack, float partialTick, int guiWidth, int guiHeight, PlayerMapData PMD) {
        Map<String, BlockState> invPosMap = PMD.getInvPosMap();
        CompoundTag tag = PMD.getInvPosNBT();
//        int index = PMD.getInvPosIndex();

//        int x = poseStack.guiWidth() / 2;
        int y = guiHeight - 65;
        int offset = guiWidth / 2;
        int posx = offset;


        BlockState blockState = invPosMap.get("pos");

        int color = 0xaa00aa;
        int posColor = 0xffffff;
        int[] pos = tag.getIntArray("pos");
        Component blockName = blockState.getBlock().getName();
        int nameWidth = gui.getFont().width(blockName.getString());
        int posWidth = gui.getFont().width("-72345");
        int maxWidth = Math.max(nameWidth,posWidth);

        //渲染边框
        blit(CHECK_BOX,poseStack, offset - (maxWidth/2) - 4,y-17, maxWidth+8,gui.getFont().lineHeight*4+6,0, 0, 128,52,128, 52);

        drawCenteredString(gui.getFont(),poseStack, blockName, posx, y-15, color );     // name
        if(pos.length == 3){
            drawCenteredString(gui.getFont(),poseStack, String.valueOf(pos[0]), posx, y-14+gui.getFont().lineHeight, posColor );        //posX
            drawCenteredString(gui.getFont(),poseStack, String.valueOf(pos[1]), posx, y-13+gui.getFont().lineHeight*2, posColor );        //posY
            drawCenteredString(gui.getFont(),poseStack, String.valueOf(pos[2]), posx, y-12+gui.getFont().lineHeight*3, posColor );        //posZ
        }else{
            drawCenteredString(gui.getFont(),poseStack, Arrays.toString(pos), posx, y-15+gui.getFont().lineHeight, posColor );        //空数组
        }

//        renderItem(blockState.getBlock().asItem().getDefaultInstance(),posx - gui.getFont().lineHeight, y-25-gui.getFont().lineHeight);       //item stack
        renderItem(poseStack,blockState.getBlock().asItem().getDefaultInstance(), posx - gui.getFont().lineHeight, y-25-gui.getFont().lineHeight, 0, 0);




//        int height = gui.getMinecraft().getWindow().getGuiScaledHeight();
//        poseStack.drawString(gui.getFont(),desc1, 3, height - gui.getFont().lineHeight-2, 0xffffff);     // ctrl+滚轮
        drawString(gui.getFont(), poseStack, HUDdesc1, 3, guiHeight - (gui.getFont().lineHeight * 2) - 4, 0xffffff);     // 右键
        drawString(gui.getFont(), poseStack, HUDdesc2, 3, guiHeight - gui.getFont().lineHeight-2, 0xffffff);     // ctrl+右键

    }

    public static void renderQueryItemHUD(ForgeGui gui, PoseStack poseStack, float partialTick, int guiWidth, int guiHeight, PlayerMapData PMD) {
        int y = guiHeight - 65;
        int center = guiWidth / 2;
        int posx = center / 2;

        int queryFlag = PMD.getQueryFlag();
        BlockState blockState1 = PMD.getQueryBlockState();
        BlockState blockState2 = PMD.getReplaceBlockState();

        Component mode_name;
        if(queryFlag == 1){
            mode_name = Component.translatable("hud.akatzuma.query_mode_1").withStyle(ChatFormatting.GREEN);
        }else{
            mode_name = Component.translatable("hud.akatzuma.query_mode_2").withStyle(ChatFormatting.GOLD);
        }

        Component mode = Component.translatable("hud.akatzuma.query_mode").append(mode_name);
        Component query_block1 = Component.translatable("hud.akatzuma.query_block1").append(blockState1.getBlock().getName().withStyle(ChatFormatting.GREEN));

        drawCenteredString(gui.getFont(),poseStack, mode, posx, y-15, 0xffffff );
//        renderItem(blockState1.getBlock().asItem().getDefaultInstance(),center , y-25-gui.getFont().lineHeight);//
        renderItem(poseStack,blockState1.getBlock().asItem().getDefaultInstance(), center, y-25-gui.getFont().lineHeight, 0, 0);
        drawCenteredString(gui.getFont(),poseStack, query_block1, center, y-15, 0xffffff );

        if(queryFlag == 2){
            int px = center + center/2;
            Component query_block2 = Component.translatable("hud.akatzuma.query_block2").append(blockState2.getBlock().getName().withStyle(ChatFormatting.GOLD));
            drawCenteredString(gui.getFont(),poseStack, query_block2, px, y-15, 0xffffff );
//            renderItem(blockState2.getBlock().asItem().getDefaultInstance(), px, y-25-gui.getFont().lineHeight); ////
            renderItem(poseStack,blockState2.getBlock().asItem().getDefaultInstance(), px, y-25-gui.getFont().lineHeight, 0, 0);
        }

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

    public static void drawString(Font pFont, PoseStack pose, String pText, float pX, float pY, int pColor) {
        pFont.draw(pose, pText, pX, pY, pColor);
    }

    public static void drawString(Font pFont, PoseStack pose, Component pText, float pX, float pY, int pColor) {
        pFont.draw(pose, pText, pX, pY, pColor);
    }



    public static void drawCenteredString(Font pFont, PoseStack pose, String pText, int pX, int pY, int pColor) {
        drawString(pFont,pose, pText, pX - pFont.width(pText) / 2, pY, pColor);
    }

    public static void drawCenteredString(Font pFont, PoseStack pose, Component pText, int pX, int pY, int pColor) {
        drawString(pFont,pose, pText, pX - pFont.width(pText) / 2, pY, pColor);
    }

    public static void renderItem(PoseStack pose,ItemStack pStack, int pX, int pY, int pSeed, int pGuiOffset) {
        if (!pStack.isEmpty()) {
            Minecraft minecraft = Minecraft.getInstance();
            MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
            ItemRenderer itemRenderer = minecraft.getItemRenderer();
            BakedModel bakedmodel = minecraft.getItemRenderer().getModel(pStack, minecraft.level, minecraft.player, pSeed);
            pose.pushPose();
            pose.translate((float)(pX + 8), (float)(pY + 8), (float)(150 + (bakedmodel.isGui3d() ? pGuiOffset : 0)));

            try {
//                pose.mulPoseMatrix((new org.joml.Matrix4f()).scaling(1.0F, -1.0F, 1.0F)));
                pose.scale(16.0F, 16.0F, 16.0F);
                pose.scale(1.0F, -1.0F, 1.0F);
                boolean flag = !bakedmodel.usesBlockLight();
                if (flag) {
                    Lighting.setupForFlatItems();
                }

                itemRenderer.render(pStack, ItemTransforms.TransformType.GUI, false, pose, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);

                RenderSystem.disableDepthTest();
                bufferSource.endBatch();
                RenderSystem.enableDepthTest();

                if (flag) {
                    Lighting.setupFor3DItems();
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
                crashreportcategory.setDetail("Item Type", () -> {
                    return String.valueOf((Object)pStack.getItem());
                });
                crashreportcategory.setDetail("Registry Name", () -> String.valueOf(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(pStack.getItem())));
                crashreportcategory.setDetail("Item Damage", () -> {
                    return String.valueOf(pStack.getDamageValue());
                });
                crashreportcategory.setDetail("Item NBT", () -> {
                    return String.valueOf((Object)pStack.getTag());
                });
                crashreportcategory.setDetail("Item Foil", () -> {
                    return String.valueOf(pStack.hasFoil());
                });
                throw new ReportedException(crashreport);
            }

            pose.popPose();
        }
    }

}
