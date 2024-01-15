package com.z227.AkatZumaWorldEdit.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlockStateString {

    public static Matcher findWhiteBlockName(String blockName) {
        // 匹配:999%minecraft:oak_log[axis=y]
        // 结果:
        // matcher.group(1) = 999
        // matcher.group(2) = minecraft
        // matcher.group(3) = oak_log
        Pattern pattern = Pattern.compile("^(\\d+)%(\\w+):(\\w+)");
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
        Pattern pattern = Pattern.compile("^(\\w+):(\\w+)");
        Matcher matcher = pattern.matcher(blockName);
        if (matcher.find()) {
            return matcher;
        }
        return null;
    }
}
