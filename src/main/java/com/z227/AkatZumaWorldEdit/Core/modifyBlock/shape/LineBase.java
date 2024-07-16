package com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape;

import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;

public class LineBase {
    // 1:对角线
    // 2:曲线
    private int type;
    private Map<Integer,Component> lineMap = new HashMap<>();

    public LineBase()
    {
        this.type = 1;
        lineMap.put(1, Component.translatable("chat.line_base.line"));
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Map<Integer, Component> getLineMap() {
        return lineMap;
    }

    public void setLineMap(Map<Integer, Component> lineMap) {
        this.lineMap = lineMap;
    }

    enum LineType{
        LINE,
        CURVE
    }

}
