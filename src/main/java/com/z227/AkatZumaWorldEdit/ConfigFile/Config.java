package com.z227.AkatZumaWorldEdit.ConfigFile;

import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.regex.Matcher;


public class Config {
    public static final ForgeConfigSpec.Builder BUILDER;
    public static ForgeConfigSpec.IntValue LOWHeight;
    public static ForgeConfigSpec.IntValue UNDOLimit;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKListWorld;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> VIPPlayerList;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ReplaceBlockList;

    public static ForgeConfigSpec.IntValue DEFAULTValue;
    public static ForgeConfigSpec.IntValue VIPValue;
    public static ForgeConfigSpec.BooleanValue CHECKInventory;
    public static ForgeConfigSpec.BooleanValue VIPCHECKInventory;



    public static ForgeConfigSpec.ConfigValue<List<? extends String>> WHITEListBlock;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKListBlock;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BLACKListTags;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> VIPWHITEListBlock;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> VIPBLACKListBlock;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> VIPBLACKListTags;

    static {
        BUILDER = new ForgeConfigSpec.Builder().comment("设置").push("Settings");
        LOWHeight  = BUILDER.comment("最低选区高度，可以防止破坏基岩").defineInRange("lowHeight", -60, -1000, Integer.MAX_VALUE);
        UNDOLimit = BUILDER.comment("undo和redo撤销的最大次数").defineInRange("undoLimit", 5, 0, 100);
        BLACKListWorld = BUILDER.comment("""
                世界黑名单，格式为"世界名/维度名"，世界名是创建世界时候的名字
                "主世界/minecraft:overworld"，"下界/minecraft:the_nether"，"末地/minecraft:the_end\"""")
                .defineList("blackListWorld", List.of("新的世界/minecraft:the_nether"), Config::validateWorldName);
        ReplaceBlockList = BUILDER.comment("""
                使用本MOD的‘建筑耗材’方块代替的方块列表，比如空气、水等不能获取的方块可以填入，会扣除对应数量的‘建筑耗材’方块
                如果白名单中也有这个方块则会按比例扣除‘建筑耗材’方块， 否则比例为1：1，推荐只填入不能获取的方块
                会先检查黑名单""")
                .defineList("replaceBlockList", List.of("minecraft:water"), Config::validateBlackBlockName);
        BUILDER.pop();

        BUILDER.comment("普通玩家").push("Default");
        DEFAULTValue  = BUILDER.comment("普通玩家默认选区大小").defineInRange("defaultValue", 1000, 0, Integer.MAX_VALUE);
        CHECKInventory = BUILDER.comment("\n放置的时候是否扣除背包方块").define("checkInventory", true);
        WHITEListBlock = BUILDER.comment("""
                        
                        白名单方块,此名单中的方块按比例扣除，#前面的数字为扣除比例，#号只做分隔使用，没有实际意义，后面的*为匹配这个MOD的所有方块
                        格式为 "比例值#方块名称"
                         0#ID：则此方块不计算背包数量，无限制放置
                         1#：则比例为1：1，放置1个方块需要扣除背包中1个
                         50#：则比例为50：1，放置50个方块需要扣除背包中1个
                         10#minecraft:* 则所有minecraft方块比例都为10：1，所有minecraft方块放置10个需要扣除背包中1个""")
                .defineList("whiteListBlock", List.of("10#minecraft:oak_log","5#minecraft:stone"), Config::validateWhiteBlockName);

        BLACKListBlock = BUILDER.comment("""
                        \n黑名单方块，优先级：黑名单 > 白名单比例值 > *
                        优先级比白名单高,此名单中的方块均不允许放置，只填入名字即可，不需要前面的#""")
                .defineList("blackListBlock", List.of("minecraft:water","minecraft:tnt","akatzumaworldedit:building_consumable"), Config::validateBlackBlockName);
        BLACKListTags = BUILDER.comment("""
                        \n黑名单标签，此标签中的方块均不允许放置（默认添加了矿物、箱子、潜影盒的标签）
                        可以安装CraftTweaker使用指令/ct hand来查看手中方块的标签
                        只需要填入最后一个:前后的值即可，不需要填入前缀tag:blocks""")
                .defineList("blackListTags", List.of("forge:ores","forge:storage_blocks","forge:chests","minecraft:shulker_boxes","forge:barrels","minecraft:guarded_by_piglins"), Config::validateTagsName);
        BUILDER.pop();

        BUILDER.comment("高级玩家").push("VipSettings");
        VIPPlayerList = BUILDER.comment("高级玩家名单").defineList("vipPlayerList", List.of("高级玩家名单"),Config::VipPlayerName);
        VIPValue = BUILDER.comment("\n高级玩家选区大小").defineInRange("vipValue", 100000, 0, Integer.MAX_VALUE);
        VIPCHECKInventory = BUILDER.comment("\n高级玩家放置的时候是否扣除背包方块").define("VipCheckInventory", true);
        VIPWHITEListBlock = BUILDER.comment("""
                        
                        白名单方块,此名单中的方块按比例扣除，#前面的数字为扣除比例，#号只做分隔使用，没有实际意义，后面的*为匹配这个MOD的所有方块
                        格式为 "比例值#方块名称"
                         0#：则此方块不计算背包数量，无限制放置
                         1#：则比例为1：1，放置1个方块需要扣除背包中1个
                         50#：则比例为50：1，放置50个方块需要扣除背包中1个
                         10#minecraft:* 则所有minecraft方块比例都为10：1，所有minecraft方块放置10个需要扣除背包中1个""")
                .defineList("VipWhiteListBlock", List.of("0#minecraft:oak_log","10#minecraft:stone"), Config::validateWhiteBlockName);

        VIPBLACKListBlock = BUILDER.comment("""
                        \n黑名单方块，优先级：黑名单 > 白名单比例值 > *
                        优先级比白名单高,此名单中的方块均不允许放置，只填入名字即可，不需要前面的#""")
                .defineList("VipBlackListBlock", () -> List.of("minecraft:water"), Config::validateBlackBlockName);
        VIPBLACKListTags = BUILDER.comment("""
                        \n黑名单标签，此标签中的方块均不允许放置（默认添加了矿物、箱子、潜影盒的标签）
                        可以安装CraftTweaker使用指令/ct hand来查看手中方块的标签
                        只需要填入最后一个:前后的值即可，不需要填入前缀tag:blocks""")
                .defineList("blackListTags", List.of("forge:ores","forge:storage_blocks","forge:chests","minecraft:shulker_boxes","forge:barrels","minecraft:guarded_by_piglins"), Config::validateTagsName);
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
//        return ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(matcher.group(1),matcher.group(2)));
        return true;
    }
    private static boolean validateWhiteBlockName(final Object obj) {
        if(!(obj instanceof String))return false;

        Matcher matcher = BlockStateString.findWhiteBlockName((String)obj);
        if(matcher==null) return false;
        return true;
//        return ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(matcher.group(2),matcher.group(3)));
    }

    private static boolean validateTagsName(final Object obj) {
        if(!(obj instanceof String))return false;

        Matcher matcher = BlockStateString.findBlackBlockName((String)obj);
        if(matcher==null) return false;
        return true;
//        return ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(matcher.group(2),matcher.group(3)));
    }

    private static boolean validateWorldName(final Object obj) {
        if(!(obj instanceof String))return false;

        Matcher matcher = BlockStateString.findWorldName((String)obj);
        if(matcher==null) return false;
        return true;
//        return ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(matcher.group(2),matcher.group(3)));
    }

}

