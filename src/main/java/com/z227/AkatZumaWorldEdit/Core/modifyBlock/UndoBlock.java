package com.z227.AkatZumaWorldEdit.Core.modifyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class UndoBlock {

    public static void undoSetBlock( Level world, Map<BlockPos,BlockState> undoMap, Map<BlockPos,BlockState> redoMap) {
        //遍历undoMap
        for (Map.Entry<BlockPos, BlockState> entry : undoMap.entrySet()) {
            BlockState old = world.getBlockState(entry.getKey());
            redoMap.put(entry.getKey(),old);
//            world.setBlock(entry.getKey(),entry.getValue(), 2);
            MySetBlock.setBlockNotUpdate(world,entry.getKey(),old,entry.getValue());
        }
    }


    public static void redoSetBlock( Level world, Map<BlockPos,BlockState> redoMap) {
        //遍历
        for (Map.Entry<BlockPos, BlockState> entry : redoMap.entrySet()) {
//            world.setBlock(entry.getKey(),entry.getValue(), 2);
            BlockState old = world.getBlockState(entry.getKey());
            MySetBlock.setBlockNotUpdate(world,entry.getKey(),old,entry.getValue());
        }
    }




}
