package com.z227.ImGuiRender;

import com.z227.AkatZumaWorldEdit.Event.ClientEventRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class EditModeScreen extends Screen {

    public EditModeScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
//        ImGuiInit.onFrameRender();

    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == 256 || pKeyCode == ClientEventRegister.EDITMODE_KEY.getKey().getValue()) {
            this.minecraft.setScreen((Screen)null);
            EditModeData.setOpenEditMode();
            return true;
        }else if(pKeyCode == 87){
//            Minecraft.getInstance().options.keyUp.isDown();
            Player player = Minecraft.getInstance().player;
            Vec3 vec3 = player.getEyePosition();
            player.moveTo(vec3.x+1, vec3.y, vec3.z);

            return true;
        }

        return true;
    }
}
