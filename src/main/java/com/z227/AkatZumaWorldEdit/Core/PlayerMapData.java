package com.z227.AkatZumaWorldEdit.Core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class PlayerMapData {
    private String name;
    private BlockPos pos1;
    private BlockPos pos2;
    private boolean vip;
    private boolean flag;
//    private Map<BlockState, List<BlockPos>> undoDataMap;
    private Map<BlockPos, BlockState> undoDataMap = new HashMap<>();


    public  Map<BlockPos, BlockState> getUndoDataMap() {
        return this.undoDataMap;
    }

    public void setUndoDataMap(BlockPos blockPos,BlockState blockState) {
//        if (this.undoDataMap == null)this.undoDataMap =new HashMap<>();
        this.undoDataMap.put(blockPos, blockState);
//        return true;
    }



    public PlayerMapData(String name) {
        this.name = name;
        this.flag = true;
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

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
