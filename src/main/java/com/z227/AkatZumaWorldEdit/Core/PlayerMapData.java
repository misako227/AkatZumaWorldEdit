package com.z227.AkatZumaWorldEdit.Core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerMapData {
    private String name;
    private BlockPos pos1;
    private BlockPos pos2;
    private Map<BlockState, List<BlockPos>> undoDataMap;


    public Map<BlockState, List<BlockPos>> getUndoDataMap() {
        return this.undoDataMap;
    }

    public boolean setUndoDataMap(BlockState blockState, BlockPos blockPos) {
        if (this.undoDataMap == null)this.undoDataMap =new HashMap<>();
        if ( this.undoDataMap.get(blockState) == null){
            // 创建一个新的列表
            List<BlockPos> list = new ArrayList<>();
            list.add(blockPos);
            // 将新的列表添加到映射中
            this.undoDataMap.put(blockState, list);
            return true;
        }
        this.undoDataMap.get(blockState).add(blockPos);
        return true;
    }



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
