package com.z227.AkatZumaWorldEdit.Render;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Commands.brush.BrushBase;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Items.BindInventoryItem;
import com.z227.AkatZumaWorldEdit.Items.QueryBlockStateItem;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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

    public static final IGuiOverlay InvPosHUD = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) return;
        Item item = player.getMainHandItem().getItem();

        PlayerMapData PMD = Util.getPMD(player);
        if(item instanceof BindInventoryItem){
            renderInvPosHUD(gui, guiGraphics, partialTick, screenWidth, screenHeight, PMD);
            return;
        };
        if(item instanceof QueryBlockStateItem){
            renderQueryItemHUD(gui, guiGraphics, partialTick, screenWidth, screenHeight, PMD);
            return;
        };

        if(PMD.getBrushMap().isEmpty()) return;
        BrushBase brushBase =  PMD.getBrushMap().get(item);
        if(brushBase == null) return;
        renderBrushHUD(gui, guiGraphics, partialTick, screenWidth, screenHeight, brushBase);




    });

    private static void renderBrushHUD(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight, BrushBase brushBase) {
        int height = guiGraphics.guiHeight();


        guiGraphics.drawString(gui.getFont(),brushBase.getShape(),3, height - (gui.getFont().lineHeight * 6) - 4, 0xffffff);     //形状
        guiGraphics.drawString(gui.getFont(),brushBase.isMaskFlag() ? BrushMaskModeWhite : BrushMaskModeBlack, 3, height - (gui.getFont().lineHeight * 5) - 4, 0xffffff);     // 右键


//todo
        if(brushBase.getMaskMap() == null)return;
        int n = 0;
        for (BlockState blockState : brushBase.getMaskMap().keySet()){
            guiGraphics.renderItem(blockState.getBlock().asItem().getDefaultInstance(),3 + n * 17, height - 40 );
            n++;
        }

    }

    @SubscribeEvent
    public static void onRenderText(RegisterGuiOverlaysEvent event){

        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(),"inv_pos_hud", InvPosHUD);
//        event.registerAboveAll("inv_pos_hud", InvPosHUD);

    }

    public static void renderInvPosHUD(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight, PlayerMapData PMD) {
        Map<String, BlockState> invPosMap = PMD.getInvPosMap();
        CompoundTag tag = PMD.getInvPosNBT();
//        int index = PMD.getInvPosIndex();

//        int x = guiGraphics.guiWidth() / 2;
        int y = guiGraphics.guiHeight() - 65;
        int offset = guiGraphics.guiWidth() / 2;
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
        guiGraphics.blit(CHECK_BOX, offset - (maxWidth/2) - 4,y-17, maxWidth+8,gui.getFont().lineHeight*4+6,0, 0, 128,52,128, 52);

        guiGraphics.drawCenteredString(gui.getFont(),blockName, posx, y-15, color );     // name
        if(pos.length == 3){
            guiGraphics.drawCenteredString(gui.getFont(), String.valueOf(pos[0]), posx, y-14+gui.getFont().lineHeight, posColor );        //posX
            guiGraphics.drawCenteredString(gui.getFont(), String.valueOf(pos[1]), posx, y-13+gui.getFont().lineHeight*2, posColor );        //posY
            guiGraphics.drawCenteredString(gui.getFont(), String.valueOf(pos[2]), posx, y-12+gui.getFont().lineHeight*3, posColor );        //posZ
        }else{
            guiGraphics.drawCenteredString(gui.getFont(), Arrays.toString(pos), posx, y-15+gui.getFont().lineHeight, posColor );        //空数组
        }

        guiGraphics.renderItem(blockState.getBlock().asItem().getDefaultInstance(),posx - gui.getFont().lineHeight, y-25-gui.getFont().lineHeight);       //item stack

//        posx += offset;
//        }


        int height = guiGraphics.guiHeight();
//        guiGraphics.drawString(gui.getFont(),desc1, 3, height - gui.getFont().lineHeight-2, 0xffffff);     // ctrl+滚轮
        guiGraphics.drawString(gui.getFont(),HUDdesc1, 3, height - (gui.getFont().lineHeight * 2) - 4, 0xffffff);     // 右键
        guiGraphics.drawString(gui.getFont(),HUDdesc2, 3, height - gui.getFont().lineHeight-2, 0xffffff);     // ctrl+右键

    }

    public static void renderQueryItemHUD(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight, PlayerMapData PMD) {
        int y = guiGraphics.guiHeight() - 65;
        int center = guiGraphics.guiWidth() / 2;
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

        guiGraphics.drawCenteredString(gui.getFont(), mode, posx, y-15, 0xffffff );
        guiGraphics.renderItem(blockState1.getBlock().asItem().getDefaultInstance(),center , y-25-gui.getFont().lineHeight);
        guiGraphics.drawCenteredString(gui.getFont(), query_block1, center, y-15, 0xffffff );

        if(queryFlag == 2){
            int px = center + center/2;
            Component query_block2 = Component.translatable("hud.akatzuma.query_block2").append(blockState2.getBlock().getName().withStyle(ChatFormatting.GOLD));
            guiGraphics.drawCenteredString(gui.getFont(), query_block2, px, y-15, 0xffffff );
            guiGraphics.renderItem(blockState2.getBlock().asItem().getDefaultInstance(), px, y-25-gui.getFont().lineHeight);
        }
    }

}
