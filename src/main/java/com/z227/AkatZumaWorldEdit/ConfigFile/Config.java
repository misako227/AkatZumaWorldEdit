package com.z227.AkatZumaWorldEdit.ConfigFile;

import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.regex.Matcher;


//@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final ForgeConfigSpec.Builder BUILDER;
    public static ForgeConfigSpec.IntValue LOWHight;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> VIPPlayerList;

    public static ForgeConfigSpec.IntValue DEFAULTValue;
    public static ForgeConfigSpec.IntValue VIPValue;
    public static ForgeConfigSpec.BooleanValue CHECKInventory;
    public static ForgeConfigSpec.BooleanValue VIPCHECKInventory;



    public static ForgeConfigSpec.ConfigValue<List<? extends String>> WHITEListBlock;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKListBlock;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> VIPWHITEListBlock;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> VIPBLACKListBlock;


;

    static {
        BUILDER = new ForgeConfigSpec.Builder().comment("设置").push("Settings");
        LOWHight  = BUILDER.comment("最低选区高度，可以防止破坏基岩").defineInRange("lowHeight", -60, -1000, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.comment("普通玩家").push("Default");
        DEFAULTValue  = BUILDER.comment("默认选区大小").defineInRange("defaultValue", 10, 0, Integer.MAX_VALUE);
        CHECKInventory = BUILDER.comment("\n放置的时候是否扣除背包方块").define("checkInventory", true);
        WHITEListBlock = BUILDER.comment("""
                        
                        白名单方块,此名单中的方块按比例扣除，#前面的数字为扣除比例，#号只做分隔使用，没有实际意义，后面的*为匹配这个MOD的所有方块
                        格式为 "比例值#方块名称"
                         0#：则次方块不计算背包数量，无限制放置
                         1#：则比例为1：1，放置1个方块需要扣除背包中1个
                         50#：则比例为50：1，放置50个方块需要扣除背包中1个
                         10#minecraft:* 则所有minecraft方块比例都为10：1，所有方块放置50个需要扣除背包中1个""")
                .defineListAllowEmpty("whiteListBlock", List.of("0#minecraft:oak_log","10#minecraft:oak_log"), Config::validateWhiteBlockName);

        BLACKListBlock = BUILDER.comment("\n黑名单方块\n优先级比白名单高,此名单中的方块均不允许放置，前面不需要#号")
                .defineListAllowEmpty("blackListBlock", List.of("minecraft:stone","minecraft:oak_log"), Config::validateBlackBlockName);
        BUILDER.pop();

        BUILDER.comment("高级玩家").push("VipSettings");
        VIPPlayerList = BUILDER.comment("高级玩家名单").defineListAllowEmpty("vipPlayerList", List.of("高级玩家名单"),Config::VipPlayerName);
        VIPValue = BUILDER.comment("\n高级玩家选区大小").defineInRange("vipValue", 1000, 0, Integer.MAX_VALUE);
        VIPCHECKInventory = BUILDER.comment("\n高级玩家放置的时候是否扣除背包方块").define("VipCheckInventory", true);
        VIPWHITEListBlock = BUILDER.comment("""
                        
                        白名单方块,此名单中的方块按比例扣除，#前面的数字为扣除比例，#号只做分隔使用，没有实际意义，后面的*为匹配这个MOD的所有方块
                        格式为 "比例值#方块名称"
                         0#：则次方块不计算背包数量，无限制放置
                         1#：则比例为1：1，放置1个方块需要扣除背包中1个
                         50#：则比例为50：1，放置50个方块需要扣除背包中1个
                         10#minecraft:* 则所有minecraft方块比例都为10：1，所有方块放置50个需要扣除背包中1个""")
                .defineListAllowEmpty("VipWhiteListBlock", List.of("0#minecraft:oak_log","5#minecraft:oak_log"), Config::validateWhiteBlockName);

        VIPBLACKListBlock = BUILDER.comment("\n高级玩家黑名单方块\n优先级比白名单高,此名单中的方块均不允许放置，前面不需要#号")
                .defineListAllowEmpty("VipBlackListBlock", List.of("minecraft:water","minecraft:air"), Config::validateBlackBlockName);
        BUILDER.pop();

    }
    public static ForgeConfigSpec COMMON_CONFIG = BUILDER.build();

    private static boolean VipPlayerName(final Object obj){
        return obj instanceof String;
    }

    private static boolean validateBlackBlockName(final Object obj) {
        if(!(obj instanceof String))return false;

        Matcher matcher = BlockStateString.findBlackBlockName((String)obj);
        if(matcher==null) return false;
        return ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(matcher.group(1),matcher.group(2)));
    }
    private static boolean validateWhiteBlockName(final Object obj) {
        if(!(obj instanceof String))return false;

        Matcher matcher = BlockStateString.findWhiteBlockName((String)obj);
        if(matcher==null) return false;
        return true;
//        return ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(matcher.group(2),matcher.group(3)));
    }

//    @SubscribeEvent
//    static void onLoad(final ModConfigEvent event)
//    {
////         convert the list of strings into a set of items
////        blocks = whiteListBlock.get().stream()
////                .map(blockName -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName)))
////                .collect(Collectors.toSet());
//    }
}

