package com.z227.AkatZumaWorldEdit.Core.modifyBlock;

import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.PlayerUtil;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.lighting.LightEngine;

import java.util.Collection;
import java.util.Map;

public class MySetBlock {

   ////////////////////////////////////////////////// shape //////////////////////////////////////////////
    //flag 是否更新方块, true则更新方块
    public static void setShapeBlockAddUndo(ServerLevel world, BlockPos pos, BlockState blockState, BlockState old,  UndoData undoMap) {
        undoMap.putIfAbsent(pos, old);
        if(old != blockState){
            setBlockState(world,pos,blockState,false);
            sendBlockUpdated(world,pos );
        }

    }

    //paste笔刷使用
    public static void shapeSetBlock(Level world, BlockPos pos, BlockState blockState, boolean isMask, boolean maskFlag, Map maskMap, boolean updateBlock, UndoData undoMap) {

        BlockState old = world.getBlockState(pos);
        if (old == blockState) return;
        if(isMask){
            if(maskFlag && maskMap.get(old) == null) {
                return;
            }else if(!maskFlag && maskMap.get(old) != null){
                return;
            }

        }

        undoMap.putIfAbsent(pos, old);
        if(updateBlock){
            world.setBlock(pos, blockState, 2);
        }else{
            setBlockState(world,pos,blockState,false);
            sendBlockUpdated((ServerLevel) world,pos);
        }

    }

    public static void setShapeFromList(Map<BlockPos, BlockState> posMap, ServerLevel serverlevel, Player player, BlockState blockState, boolean isMask, boolean maskFlag, Map maskMap) {

        PlayerMapData PMD = Util.getPMD(player);
        UndoData undoData = new UndoData(serverlevel);
        PMD.getUndoDataMap().push(undoData);//添加到undo
        boolean flag = PlayerUtil.isSetUpdateBlock(player);


        if (flag) {//原版更新方块
            for (Map.Entry<BlockPos, BlockState> entry : posMap.entrySet()){
                BlockPos v3 = entry.getKey();
                BlockState old = entry.getValue();
                if(isMask){//笔刷
                    if(maskFlag && maskMap.get(old) == null) {//白名单
                        return;
                    }else if(!maskFlag && maskMap.get(old) != null){
                        return;
                    }
                }
                undoData.putIfAbsent(v3, old);
                serverlevel.setBlock(v3, blockState, 2);
            }
        }else{//不更新方块
            for (Map.Entry<BlockPos, BlockState> entry : posMap.entrySet()){
                BlockPos v3 = entry.getKey();
                BlockState old = entry.getValue();
                if(isMask){//笔刷
                    if(maskFlag && maskMap.get(old) == null) {//白名单
                        return;
                    }else if(!maskFlag && maskMap.get(old) != null){
                        return;
                    }
                }
                MySetBlock.setShapeBlockAddUndo(serverlevel, v3, blockState,old, undoData);
            }
        }

    }


    public static void setShapeFromMap(Map<BlockPos, BlockState> blockMap, ServerLevel serverlevel, Player player, boolean isMask, boolean maskFlag, Map maskMap) {
        PlayerMapData PMD = Util.getPMD(player);
        UndoData undoData = new UndoData(serverlevel);
        PMD.getUndoDataMap().push(undoData);//添加到undo
        boolean flag = PlayerUtil.isSetUpdateBlock(player);


        if (flag) {//原版更新方块
            for (Map.Entry<BlockPos, BlockState> entry : blockMap.entrySet()){
                BlockPos v3 = entry.getKey();
                BlockState blockState = entry.getValue();
                BlockState old = serverlevel.getBlockState(entry.getKey());
                if(isMask){//笔刷
                    if(maskFlag && maskMap.get(old) == null) {//白名单
                        return;
                    }else if(!maskFlag && maskMap.get(old) != null){
                        return;
                    }
                }
                undoData.put(v3, old);
                serverlevel.setBlock(v3, blockState, 2);
            }
        }else {
            for (Map.Entry<BlockPos, BlockState> entry : blockMap.entrySet()){
                BlockPos v3 = entry.getKey();
                BlockState blockState = entry.getValue();
                BlockState old = serverlevel.getBlockState(entry.getKey());
                if(isMask){//笔刷
                    if(maskFlag && maskMap.get(old) == null) {//白名单
                        return;
                    }else if(!maskFlag && maskMap.get(old) != null){
                        return;
                    }
                }
                undoData.put(v3, old);
                MySetBlock.setBlockAddUndo(serverlevel, v3, blockState, undoData);
            }
        }

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////



    public static void setBlockAddUndo(ServerLevel world, BlockPos pos, BlockState blockState,  UndoData undoMap) {
        BlockState old = world.getBlockState(pos);
        undoMap.put(pos, old);

        if(!old.equals(blockState)){
            setBlockState(world,pos,blockState,false);
            sendBlockUpdated(world,pos );
        }

    }

    //没有undo，给redo使用
    public static void setBlock(ServerLevel world, BlockPos pos, BlockState blockState, boolean flag) {
        BlockState old = world.getBlockState(pos);

        if (flag) {
            world.setBlock(pos, blockState, 2);
            return;
        }

        if(old != blockState){
            setBlockState(world,pos,blockState,false);
            sendBlockUpdated(world,pos );
        }

    }
    //flag 是否更新方块, true则更新方块
    public static void setSingleBlockAddUndo(ServerLevel world, BlockPos pos, BlockState blockState,boolean flag, UndoData undoMap) {
        BlockState old = world.getBlockState(pos);
        undoMap.put(pos, old);

        if (flag) {//原版更新方块
            world.setBlock(pos, blockState, 2);
            return;
        }

        if(!old.equals(blockState)){
            setBlockState(world,pos,blockState,false);
            sendBlockUpdated(world,pos );
        }

    }

    //遍历两个坐标放置方块，并添加到undo

    public static void setBlockFromPos(BlockPos pos1, BlockPos pos2, ServerLevel serverlevel, Player player, BlockState blockState) {
        int minX = Math.min(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxX = Math.max(pos1.getX(), pos2.getX());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());

        PlayerMapData PMD = Util.getPMD(player);
        UndoData undoData = new UndoData(serverlevel);
        PMD.getUndoDataMap().push(undoData);//添加到undo
        boolean flag = PlayerUtil.isSetUpdateBlock(player);

        if (flag) {//原版更新方块
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        BlockPos v3 = new BlockPos(x, y, z);
                        BlockState old = serverlevel.getBlockState(v3);
                        undoData.put(v3, old);
                        serverlevel.setBlock(v3, blockState, 2);
                    }
                }
            }
        }else{//不更新方块
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        BlockPos v3 = new BlockPos(x, y, z);
                        MySetBlock.setBlockAddUndo(serverlevel, v3, blockState, undoData);
                    }
                }
            }
        }

    }

    public static void setBlockFromList(Collection<BlockPos> posList, ServerLevel serverlevel, Player player, BlockState blockState) {
        PlayerMapData PMD = Util.getPMD(player);
        UndoData undoData = new UndoData(serverlevel);
        PMD.getUndoDataMap().push(undoData);//添加到undo
        boolean flag = PlayerUtil.isSetUpdateBlock(player);


        if (flag) {//原版更新方块
            for (BlockPos v3 : posList) {
                BlockState old = serverlevel.getBlockState(v3);
                undoData.put(v3, old);
                serverlevel.setBlock(v3, blockState, 2);
            }
        }else{//不更新方块
            for (BlockPos v3 : posList) {
                MySetBlock.setBlockAddUndo(serverlevel, v3, blockState, undoData);
            }
        }

    }

    public static void setBlockFromMap(Map<BlockPos, BlockState> blockMap, ServerLevel serverlevel, Player player) {
        PlayerMapData PMD = Util.getPMD(player);
        UndoData undoData = new UndoData(serverlevel);
        PMD.getUndoDataMap().push(undoData);//添加到undo
        boolean flag = PlayerUtil.isSetUpdateBlock(player);


        if (flag) {//原版更新方块
            for (Map.Entry<BlockPos, BlockState> entry : blockMap.entrySet()){
                BlockPos v3 = entry.getKey();
                BlockState blockState = entry.getValue();
                BlockState old = serverlevel.getBlockState(entry.getKey());
                undoData.put(v3, old);
                serverlevel.setBlock(v3, blockState, 2);
            }
        }else {
            for (Map.Entry<BlockPos, BlockState> entry : blockMap.entrySet()){
                BlockPos v3 = entry.getKey();
                BlockState blockState = entry.getValue();

                MySetBlock.setBlockAddUndo(serverlevel, v3, blockState, undoData);
            }
        }

    }


    //多个Map使用同一个undo，需要在外部设置undo
    public static void setBlockFromMaps(Map<BlockPos, BlockState> blockMap, ServerLevel serverlevel, Player player, UndoData undoData) {
//        PlayerMapData PMD = Util.getPMD(player);
//        UndoData undoData = new UndoData(serverlevel);
//        PMD.getUndoDataMap().push(undoData);//添加到undo
        boolean flag = PlayerUtil.isSetUpdateBlock(player);


        if (flag) {//原版更新方块
            for (Map.Entry<BlockPos, BlockState> entry : blockMap.entrySet()){
                BlockPos v3 = entry.getKey();
                BlockState blockState = entry.getValue();
                BlockState old = serverlevel.getBlockState(entry.getKey());
                undoData.put(v3, old);
                serverlevel.setBlock(v3, blockState, 2);
            }
        }else {
            for (Map.Entry<BlockPos, BlockState> entry : blockMap.entrySet()){
                BlockPos v3 = entry.getKey();
                BlockState blockState = entry.getValue();
                BlockState old = serverlevel.getBlockState(entry.getKey());
                undoData.put(v3, old);
                MySetBlock.setBlockAddUndo(serverlevel, v3, blockState, undoData);
            }
        }

    }

//###################################################################################################


    //弃用
    private static void setBlockNotUpdate(ServerLevel world, BlockPos pos,BlockState old, BlockState blockState) {
//        world.setBlock(pos,blockState, 16);
//        world.sendBlockUpdated(pos, old,blockState,16);
//        setBlockState(world,pos,blockState,false);
        if(old != blockState){
            setBlockState(world,pos,blockState,false);
            sendBlockUpdated(world,pos );
        }
    }

    //弃用
    private static void setBlockNotUpdateAddUndo(ServerLevel world, BlockPos pos, BlockState blockState,UndoData undoMap) {
        BlockState old = world.getBlockState(pos);
        undoMap.put(pos, old);
//        world.setBlock(pos,blockState, 16);
//        world.sendBlockUpdated(pos, old,blockState,16);
        if(old != blockState){
//            chunkSetBlock(world,pos,blockState);
            setBlockState(world,pos,blockState,false);
            sendBlockUpdated(world,pos);

        }
    }


    public static void sendBlockUpdated(ServerLevel world, BlockPos pos) {
        world.getChunkSource().blockChanged(pos);
    }


//    public static void chunkSetBlock(Level world, BlockPos pos, BlockState blockState){
//        LevelChunk levelchunk = world.getChunkAt(pos);
//        int i = pos.getY();
//        LevelChunkSection levelChunkSection = levelchunk.getSection(levelchunk.getSectionIndex(i));
//        int j = pos.getX() & 15;
//        int k = i & 15;
//        int l = pos.getZ() & 15;
//
//        BlockState oldState = levelChunkSection.setBlockState(j, k, l, blockState, false);
//
//        boolean flag = levelChunkSection.hasOnlyAir();
//        boolean flag1 = levelChunkSection.hasOnlyAir();
//        if (flag != flag1) {
//            levelchunk.getLevel().getChunkSource().getLightEngine().updateSectionStatus(pos, flag1);
//        }
//        if (LightEngine.hasDifferentLightProperties(world, pos, oldState, blockState)) {
//            ProfilerFiller profilerfiller = levelchunk.getLevel().getProfiler();
//            profilerfiller.push("updateSkyLightSources");
//            levelchunk.getSkyLightSources().update(levelchunk, j, i, l);
//            profilerfiller.popPush("queueCheckLight");
//            levelchunk.getLevel().getChunkSource().getLightEngine().checkBlock(pos);
//            profilerfiller.pop();
//        }
//
//        //移除方块实体
//        boolean flag2 = oldState.hasBlockEntity();
//        Block block = blockState.getBlock();
//        if (!levelchunk.getLevel().isClientSide) {
//            oldState.onRemove(levelchunk.getLevel(), pos, blockState, false);
//        } else if ((!oldState.is(block) || !blockState.hasBlockEntity()) && flag2) {
//            levelchunk.removeBlockEntity(pos);
//        }
//
//        levelchunk.setUnsaved(true);
//
//
//    }

    public static BlockState setBlockState(Level world, BlockPos pPos, BlockState pState, boolean pIsMoving) {
        LevelChunk levelchunk = world.getChunkAt(pPos);
        int i = pPos.getY();
        LevelChunkSection levelchunksection = levelchunk.getSection(levelchunk.getSectionIndex(i));
        boolean flag = levelchunksection.hasOnlyAir();
        if (flag && pState.isAir()) {
            return null;
        } else {
            int j = pPos.getX() & 15;
            int k = i & 15;
            int l = pPos.getZ() & 15;
            BlockState blockstate = levelchunksection.setBlockState(j, k, l, pState,false);
            if (blockstate == pState) {
                return null;
            } else {
                Block block = pState.getBlock();

//                levelchunk.getHeightmaps().get(Heightmap.Types.MOTION_BLOCKING).update(j, i, l, pState);
//                levelchunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES).update(j, i, l, pState);
//                levelchunk.heightmaps.get(Heightmap.Types.OCEAN_FLOOR).update(j, i, l, pState);
//                levelchunk.heightmaps.get(Heightmap.Types.WORLD_SURFACE).update(j, i, l, pState);
                boolean flag1 = levelchunksection.hasOnlyAir();
                if (flag != flag1) {
                    levelchunk.getLevel().getChunkSource().getLightEngine().updateSectionStatus(pPos, flag1);
                }

                if (LightEngine.hasDifferentLightProperties(levelchunk, pPos, blockstate, pState)) {
                    ProfilerFiller profilerfiller = levelchunk.getLevel().getProfiler();
                    profilerfiller.push("updateSkyLightSources");
                    levelchunk.getSkyLightSources().update(levelchunk, j, i, l);
                    profilerfiller.popPush("queueCheckLight");
                    levelchunk.getLevel().getChunkSource().getLightEngine().checkBlock(pPos);
                    profilerfiller.pop();
                }

                boolean flag2 = blockstate.hasBlockEntity();
                if (!levelchunk.getLevel().isClientSide) {
                    blockstate.onRemove(levelchunk.getLevel(), pPos, pState, pIsMoving);
                } else if ((!blockstate.is(block) || !pState.hasBlockEntity()) && flag2) {
                    levelchunk.removeBlockEntity(pPos);
                }

                if (!levelchunksection.getBlockState(j, k, l).is(block)) {
                    return null;
                } else {
                    if (!levelchunk.getLevel().isClientSide && !levelchunk.getLevel().captureBlockSnapshots) {
                        pState.onPlace(levelchunk.getLevel(), pPos, blockstate, pIsMoving);
                    }

//                    if (pState.hasBlockEntity()) {
//                        BlockEntity blockentity = levelchunk.getBlockEntity(pPos, LevelChunk.EntityCreationType.CHECK);
//                        if (blockentity == null) {
//                            blockentity = ((EntityBlock)block).newBlockEntity(pPos, pState);
//                            if (blockentity != null) {
//                                levelchunk.addAndRegisterBlockEntity(blockentity);
//                            }
//                        } else {
//                            blockentity.setBlockState(pState);
//                            levelchunk.updateBlockEntityTicker(blockentity);
//                        }
//                    }

                    levelchunk.setUnsaved(true);
                    return blockstate;
                }
            }
        }
    }




}
