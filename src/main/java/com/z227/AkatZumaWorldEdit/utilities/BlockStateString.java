package com.z227.AkatZumaWorldEdit.utilities;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockStateString {

    public static Matcher findWhiteBlockName(String blockName) {
        // 匹配:999#minecraft:oak_log[axis=y]
        // 结果:
        // matcher.group(1) = 999
        // matcher.group(2) = minecraft
        // matcher.group(3) = oak_log
        Pattern pattern = Pattern.compile("^(\\d+)#(\\w+):([\\w]+|[*])");
//        Pattern pattern = Pattern.compile("^(\\d+)#(\\w+:([\\w]+|[*]))");
        Matcher matcher = pattern.matcher(blockName);
        if (matcher.find()) {
            return matcher;
        }
        return null;
    }

    public static Matcher findBlackBlockName(String blockName) {
        // 匹配:minecraft:oak_log[axis=y]
        // 结果:
        // matcher.group(1) = minecraft
        // matcher.group(2) = oak_log
        Pattern pattern = Pattern.compile("^(\\w+):([\\w]+|[*])");
        Matcher matcher = pattern.matcher(blockName);
        if (matcher.find()) {
            return matcher;
        }
        return null;
    }

    public static Matcher findWorldName(String worldName) {
        // 匹配:minecraft:oak_log[axis=y]
        // 结果:
        // matcher.group(1) = minecraft
        // matcher.group(2) = oak_log
        Pattern pattern = Pattern.compile("^[\\u4e00-\\u9fa5\\w]+/\\w+:\\w+");
        Matcher matcher = pattern.matcher(worldName);
        if (matcher.find()) {
            return matcher;
        }
        return null;
    }

    public static String getBlockName(BlockState blockState) {
        return blockState.getBlock().toString().replace("Block{","").replace("}" ,"");
    }

    public static String getBlockName(Block block) {
        return block.toString().replace("Block{","").replace("}" ,"");
    }

}
