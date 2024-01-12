package com.z227.AkatZumaWorldEdit.ConfigFile;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

//@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder().comment("General settings").push("general");

    public static ForgeConfigSpec.IntValue VALUE = BUILDER
            .comment("测试值").defineInRange("value", 10, 0, Integer.MAX_VALUE);

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> whiteListBlock =BUILDER
            .comment("白名单方块")
            .defineListAllowEmpty("whiteListBlock", List.of("minecraft:air"), Config::validateItemName);

    static {
//        BUILDER;
        BUILDER.pop();

    }
    public static ForgeConfigSpec COMMON_CONFIG = BUILDER.build();

//    public static Set<Block> blocks;

    private static boolean validateItemName(final Object obj) {
        return obj instanceof final String blockName && ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(blockName));
    }

//    @SubscribeEvent
//    static void onLoad(final ModConfigEvent event)
//    {
//
//        // convert the list of strings into a set of items
//        blocks = whiteListBlock.get().stream()
//                .map(blockName -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName)))
//                .collect(Collectors.toSet());
//    }
}
