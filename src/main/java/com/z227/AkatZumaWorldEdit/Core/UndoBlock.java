package com.z227.AkatZumaWorldEdit.Core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class UndoBlock {

    public static void undoSetBlock( Level world, Map<BlockPos,BlockState> undoMap, Map<BlockPos,BlockState> redoMap) {
        //遍历undoMap
        for (Map.Entry<BlockPos, BlockState> entry : undoMap.entrySet()) {
            redoMap.put(entry.getKey(),world.getBlockState(entry.getKey()));
            world.setBlock(entry.getKey(),entry.getValue(), 2);
        }
    }


    public static void redoSetBlock( Level world, Map<BlockPos,BlockState> redoMap) {
        //遍历
        for (Map.Entry<BlockPos, BlockState> entry : redoMap.entrySet()) {
            world.setBlock(entry.getKey(),entry.getValue(), 2);
        }
    }




}
