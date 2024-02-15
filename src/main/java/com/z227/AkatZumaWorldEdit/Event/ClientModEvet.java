package com.z227.AkatZumaWorldEdit.Event;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@OnlyIn(Dist.CLIENT)
public class ClientModEvet {

    @SubscribeEvent
    public static void registerKey(RegisterKeyMappingsEvent event){
        event.register(ClientEventRegister.UNDO_KEY);
    }


}
