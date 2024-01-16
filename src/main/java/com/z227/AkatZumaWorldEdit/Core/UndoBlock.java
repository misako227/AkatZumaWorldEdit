package com.z227.AkatZumaWorldEdit.Core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class UndoBlock {

    public static void undoSetBlock( Level world, Map<BlockPos,BlockState> undoMap) {
        //遍历undoMap
        for (Map.Entry<BlockPos, BlockState> entry : undoMap.entrySet()) {
            world.setBlock(entry.getKey(),entry.getValue(), 2);
        }
    }





}
