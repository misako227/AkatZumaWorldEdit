package com.z227.AkatZumaWorldEdit.Core.modifyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class UndoBlock {

    public static void undoSetBlock(ServerLevel world, UndoData undoMap, UndoData redoMap, boolean flag) {
        //遍历undoMap
        for (Map.Entry<BlockPos, BlockState> entry : undoMap.getUndoMap().entrySet()) {
            BlockState old = world.getBlockState(entry.getKey());
            redoMap.getUndoMap().put(entry.getKey(),old);
//            world.setBlock(entry.getKey(),entry.getValue(), 2);
            MySetBlock.setBlockAddUndo(world, entry.getKey(),entry.getValue(),flag, redoMap);
        }
    }


    public static void redoSetBlock( ServerLevel world, UndoData redoMap, boolean flag) {
        //遍历
        for (Map.Entry<BlockPos, BlockState> entry : redoMap.getUndoMap().entrySet()) {
//            world.setBlock(entry.getKey(),entry.getValue(), 2);
//            BlockState old = world.getBlockState(entry.getKey());
            MySetBlock.setBlock(world,entry.getKey(), entry.getValue(), flag);
        }
    }




}
