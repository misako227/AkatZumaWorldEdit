package com.z227.AkatZumaWorldEdit.Render;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.Map;

@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class BindInvGui {
    public  static ResourceLocation CHECK_BOX = new ResourceLocation(AkatZumaWorldEdit.MODID, "textures/gui/unchecked-checkbox-128.png");


    public static final IGuiOverlay InvPosHUD = ((gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        LocalPlayer player = Minecraft.getInstance().player;
        PlayerMapData PMD = Util.getPMD(player);

        ItemStack itemstack = player.getMainHandItem();
        if(itemstack.getItem() != AkatZumaWorldEdit.BindInventory.get())return;

        Map<int[], BlockState> invPosMap = PMD.getInvPosMap();
        int index = PMD.getInvPosIndex();
        int n = 1;
//        int x = guiGraphics.guiWidth() / 2;
        int y = guiGraphics.guiHeight() - 65;
        int offset = guiGraphics.guiWidth() / 6;
        int posx = offset;

        for (Map.Entry<int[], BlockState> entry : invPosMap.entrySet()) {
            int color = 0xffffff;
            int posColor = 0xffffff;
            String stringPos = Arrays.toString(entry.getKey());
            Component blockName = entry.getValue().getBlock().getName();
            int nameWidth = gui.getFont().width(blockName.getString());
            int posWidth = gui.getFont().width(stringPos);
            int maxWidth = Math.max(nameWidth,posWidth);
            if(index == n){
//                color = 0x5555ff;
//                posColor = 0xaa00aa;
                color = 0xaa00aa;
//                maxWidth = Math.max(gui.getFont().width(blockName.getString()),posWidth);
                guiGraphics.blit(CHECK_BOX, index * offset - (maxWidth/2) - 4,y-17, maxWidth+8,gui.getFont().lineHeight*2+4,0, 0, 128,52,128, 52);
            }
            guiGraphics.drawCenteredString(gui.getFont(),blockName, posx, y-15, color );     // name
            guiGraphics.drawCenteredString(gui.getFont(), stringPos, posx, y-15+gui.getFont().lineHeight, posColor );        //pos
            guiGraphics.renderItem(Items.CHEST.getDefaultInstance(),posx - gui.getFont().lineHeight, y-25-gui.getFont().lineHeight);       //item stack


            n++;
            posx += offset;
        }

        Component desc1 = Component.translatable("hud.akatzuma.ctrl_scroll");
        Component desc2 = Component.translatable("hud.akatzuma.ctrl_right");
        int height = guiGraphics.guiHeight();
        guiGraphics.drawCenteredString(gui.getFont(),desc1, 0, height - gui.getFont().lineHeight, 0xffffff);     // ctrl+滚轮
        guiGraphics.drawCenteredString(gui.getFont(),desc2, 0, height - (gui.getFont().lineHeight * 2) - 2, 0xffffff);     // ctrl+右键




//        guiGraphics.drawCenteredString(gui.getFont(), "Hello World", x, y, 0xffffff);
//        int x2 = x +1;
//        int y2 = y - 15;
//
//        guiGraphics.drawCenteredString(gui.getFont(), "Hello World2", x2, y2, 0xfcfc28);
//        guiGraphics.renderOutline(x,guiGraphics.guiHeight() /2, 50,20,-12303292);
//        guiGraphics.fill(x,guiGraphics.guiHeight() /2,70,20,-12303292);

//        guiGraphics.renderTooltip(gui.getFont(), Items.OAK_LOG.getDefaultInstance(),x,y);
    });

    @SubscribeEvent
    public static void onRenderText(RegisterGuiOverlaysEvent event){

        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(),"inv_pos_hud", InvPosHUD);
//        event.registerAboveAll("inv_pos_hud", InvPosHUD);

    }
}
