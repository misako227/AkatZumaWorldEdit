package com.z227.AkatZumaWorldEdit.Core.modifyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ServerLevelData;

import java.util.HashMap;
import java.util.Map;

public class UndoData {
    String worldName;
    String dimension;
    Map<BlockPos, BlockState> undoMap;

    public UndoData(Level level) {
        this.worldName =  ((ServerLevelData)level.getLevelData()).getLevelName();
        this.dimension = level.dimension().location().toString();
        this.undoMap  = new HashMap<>();
    }

    public Map<BlockPos, BlockState> getUndoMap() {
        return undoMap;
    }

    public void setUndoMap(Map<BlockPos, BlockState> undoMap) {
        this.undoMap = undoMap;
    }

    public boolean equalsLevel(Level level) {
        return this.worldName.equals(((ServerLevelData)level.getLevelData()).getLevelName()) && this.dimension.equals(level.dimension().location().toString());
    }

    public void put(BlockPos pos, BlockState state) {
        this.undoMap.put(pos, state);
    }

    public void putIfAbsent(BlockPos pos, BlockState old) {
        this.undoMap.putIfAbsent(pos, old);
    }
}
