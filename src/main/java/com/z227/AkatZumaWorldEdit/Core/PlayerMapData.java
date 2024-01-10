package com.z227.AkatZumaWorldEdit.Core;

import net.minecraft.core.BlockPos;

public class PlayerMapData {
    private String name;
    private BlockPos pos1;
    private BlockPos pos2;

    public PlayerMapData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BlockPos getPos1() {
        return pos1;
    }

    public void setPos1(BlockPos pos1) {
        this.pos1 = pos1;
    }

    public BlockPos getPos2() {
        return pos2;
    }

    public void setPos2(BlockPos pos2) {
        this.pos2 = pos2;
    }
}
