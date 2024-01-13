package com.z227.AkatZumaWorldEdit.ConfigFile;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

//@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final ForgeConfigSpec.Builder BUILDER;

    public static ForgeConfigSpec.IntValue DEFAULTValue;
    public static ForgeConfigSpec.IntValue VIPValue;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> VIPPlayerList;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> WHITEListBlock;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKListBlock;

    public static ForgeConfigSpec.BooleanValue CHECKInventory;

    static {
        BUILDER = new ForgeConfigSpec.Builder().comment("选区设置").push("area Settings");
        DEFAULTValue  = BUILDER.comment("默认选区大小").defineInRange("defaultValue", 100, 0, Integer.MAX_VALUE);
        VIPValue = BUILDER.comment("高级玩家选区大小").defineInRange("vipValue", 1000, 0, Integer.MAX_VALUE);
        VIPPlayerList = BUILDER.comment("高级玩家名单").defineListAllowEmpty("vipPlayerList", List.of("高级玩家名单"),Config::VipPlayerName);
        BUILDER.pop();

        BUILDER.comment("名单设置").push("listSettings");
        CHECKInventory = BUILDER.comment("放置的时候是否扣除背包方块").define("checkInventory", true);
        WHITEListBlock = BUILDER.comment("白名单方块\n此名单中的方块不扣除背包方块").defineListAllowEmpty("whiteListBlock", List.of("minecraft:air"), Config::validateBlockName);
        BLACKListBlock = BUILDER.comment("黑名单方块\n优先级比白名单高,此名单中的方块均不允许放置").defineListAllowEmpty("blackListBlock", List.of("minecraft:water"), Config::validateBlockName);
        BUILDER.pop();

    }
    public static ForgeConfigSpec COMMON_CONFIG = BUILDER.build();

    private static boolean VipPlayerName(final Object obj){
        return obj instanceof String;
    }

    private static boolean validateBlockName(final Object obj) {
        return obj instanceof final String blockName && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(blockName));
    }

//    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {

//         convert the list of strings into a set of items
//        blocks = whiteListBlock.get().stream()
//                .map(blockName -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName)))
//                .collect(Collectors.toSet());
    }
}
