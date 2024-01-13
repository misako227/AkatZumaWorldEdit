package com.z227.AkatZumaWorldEdit.Event;


import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.utilities.LanguageDataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModNetworkEvent {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var packOutput = gen.getPackOutput();
        gen.addProvider(event.includeClient(), new LanguageDataGenerator.EnglishLanguageProvider(packOutput));
        gen.addProvider(event.includeClient(), new LanguageDataGenerator.ChineseLanguageProvider(packOutput));
    }


    @SubscribeEvent
    public static void onModLoadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(ModNetworkEvent::addConfigMap);
    }


    public static void addConfigMap() {
        Config.WHITEListBlock.get().stream().forEach(k -> {
            AkatZumaWorldEdit.ConfigMap.put(k, true);
        });
        Config.BLACKListBlock.get().stream().forEach(k -> {
            AkatZumaWorldEdit.ConfigMap.put(k, false);
        });

    }




}
