package com.z227.AkatZumaWorldEdit.Core;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.level.BlockEvent;

public class PlaceBlock {

    //判断能否放置方块
    public Boolean isPlaceBlock(Level world, Player player, BlockPos blockPos, BlockState blockState){

        BlockSnapshot snapshot = BlockSnapshot.create(world.dimension(), world, blockPos);
        BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(snapshot, blockState, player);
        MinecraftForge.EVENT_BUS.post(placeEvent);
        if(placeEvent.isCanceled()) {
            return false;
        }
        return true;
    }

        //计算长宽高
    public static Vec3i calculateCubeDimensions(BlockPos pos1, BlockPos pos2) {
        int length = Math.abs(pos1.getX() - pos2.getX()) + 1;
        int width = Math.abs(pos1.getY() - pos2.getY()) + 1;
        int height = Math.abs(pos1.getZ() - pos2.getZ()) + 1;
        return new Vec3i(length,width,height);
    }

    //遍历两个坐标之间的每个点
    public static void traverseCube(BlockPos pos1, BlockPos pos2, ServerLevel world, Player player, BlockState blockState) {
        for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++) {
            for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++) {
                for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++) {
                    // Process the coordinate (x, y, z) here
//                    world.setBlockAndUpdate(new BlockPos(x,y,z),blockState);
//                    world.setBlock(new BlockPos(x,y,z),blockState);
                    BlockPos v3 = new BlockPos(x,y,z);
                    world.setBlock(v3,blockState, 2);
//                    world.blockUpdated(v3, blockState.getBlock());
//                    System.out.println("Coordinate: (" + x + ", " +
//                    y + ", " + z + ")");
                }
            }
        }
    }
}
