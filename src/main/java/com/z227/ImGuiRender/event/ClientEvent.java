package com.z227.ImGuiRender.event;


import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.ImGuiRender.EditModeData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvent {

    @SubscribeEvent
    public static void onClientTickEvent(ScreenEvent.Opening event) {

        if (event.getNewScreen() != null){
            EditModeData.setOpenEditMode(false);
        }


    }
}
