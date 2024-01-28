package com.z227.AkatZumaWorldEdit.Event;


import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.utilities.AkatRecipe;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import com.z227.AkatZumaWorldEdit.utilities.LanguageDataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModNetworkEvent {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var packOutput = gen.getPackOutput();
        gen.addProvider(event.includeClient(), new LanguageDataGenerator.EnglishLanguageProvider(packOutput));
        gen.addProvider(event.includeClient(), new LanguageDataGenerator.ChineseLanguageProvider(packOutput));
        gen.addProvider(event.includeServer(), new AkatRecipe(packOutput));
    }


    @SubscribeEvent
    public static void onModLoadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(ModNetworkEvent::addConfigMap);

        AkatZumaWorldEdit.USEITEM.put(AkatZumaWorldEdit.WOOD_AXE.get(), true);
        AkatZumaWorldEdit.USEITEM.put(AkatZumaWorldEdit.Query_Item.get(), true);
    }



    public static void addConfigMap() {
        addBlackWhiteToMap(Config.WHITEListBlock.get(), true,AkatZumaWorldEdit.defaultBlockMap);
        addBlackWhiteToMap(Config.BLACKListBlock.get(), false,AkatZumaWorldEdit.defaultBlockMap);


        //vip
        addBlackWhiteToMap(Config.VIPWHITEListBlock.get(), true,AkatZumaWorldEdit.VipBlockMap);
        addBlackWhiteToMap(Config.VIPBLACKListBlock.get(), false,AkatZumaWorldEdit.VipBlockMap);
        updateVipPlayerMap(Config.VIPPlayerList.get(), AkatZumaWorldEdit.VipPlayerMap);

    }

    //@param b true=白名单 false=黑名单
    public static void addBlackWhiteToMap(List<? extends String> input, boolean b, Map output) {
        input.forEach(k ->{
            Matcher matcher;String tempKey;
            if(b)matcher = BlockStateString.findWhiteBlockName(k);
            else matcher = BlockStateString.findBlackBlockName(k);
            if(matcher==null) return;
            if(b)tempKey = matcher.group(2) +  ":" + matcher.group(3);
            else tempKey = matcher.group(1) +  ":" + matcher.group(2);
            output.put(tempKey, b?Integer.parseInt(matcher.group(1)) : -1);

        });
    }
    public static void updateVipPlayerMap(List<? extends String> input, Map output){
        input.forEach(k ->{
            output.put(k,true);
        });
    }

    //

}
