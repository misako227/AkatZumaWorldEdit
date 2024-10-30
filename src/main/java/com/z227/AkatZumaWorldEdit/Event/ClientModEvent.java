package com.z227.AkatZumaWorldEdit.Event;


import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class ClientModEvent {

    @SubscribeEvent
    public static void registerKey(FMLClientSetupEvent event){
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(ClientEventRegister.UNDO_KEY));
    }


}
