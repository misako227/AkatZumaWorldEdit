package com.z227.AkatZumaWorldEdit.Core.modifyBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class MySetBlock {
    public static void shapeSetBlock(Level world, Player player, BlockPos pos, BlockState blockState,int flag,Map<BlockPos, BlockState> undoMap) {
        undoMap.putIfAbsent(pos, world.getBlockState(pos));
        world.setBlock(pos,blockState, flag);
    }




}