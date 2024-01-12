package com.z227.AkatZumaWorldEdit.Event;


import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.utilities.LanguageDataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModNetworkEvent {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var packOutput = gen.getPackOutput();
        gen.addProvider(event.includeClient(), new LanguageDataGenerator.EnglishLanguageProvider(packOutput));
        gen.addProvider(event.includeClient(), new LanguageDataGenerator.ChineseLanguageProvider(packOutput));
    }



}
