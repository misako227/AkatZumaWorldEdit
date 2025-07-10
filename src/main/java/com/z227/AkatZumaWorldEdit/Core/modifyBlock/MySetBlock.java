package com.z227.AkatZumaWorldEdit.Core.modifyBlock;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.lighting.LightEngine;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MySetBlock {
    public static void shapeSetBlock(Level world, Player player, BlockPos pos, BlockState blockState,int flag,Map<BlockPos, BlockState> undoMap) {
//        undoMap.putIfAbsent(pos, world.getBlockState(pos));
//        world.setBlock(pos,blockState, flag);
        BlockState old = world.getBlockState(pos);
        undoMap.putIfAbsent(pos, old);
        if(old != blockState){
            setBlockState(world,pos,blockState,false);
            sendBlockUpdated((ServerLevel) world,pos);
        }
    }

    public static void shapeSetBlock(@NotNull Level world, BlockPos pos, BlockState blockState, boolean isMask, boolean maskFlag, Map maskMap, UndoData undoMap) {

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
        setBlockState(world,pos,blockState,false);
        sendBlockUpdated((ServerLevel) world,pos);

    }

    //flag 是否更新方块
    public static void setBlockAddUndo(ServerLevel world, BlockPos pos, BlockState blockState, Player player, UndoData undoMap) {
        BlockState old = world.getBlockState(pos);
        undoMap.put(pos, old);

        AttributeInstance attribute = player.getAttribute(AkatZumaWorldEdit.SET_FLAG_ATTRIBUTE.get());
        if(attribute != null) {
            double v = attribute.getBaseValue();
            boolean flag = v > 0;
            if (flag) {
                world.setBlock(pos, blockState, 2);
                return;
            }
        }

        if(old != blockState){
            setBlockState(world,pos,blockState,false);
            sendBlockUpdated(world,pos );
        }

    }

    public static void setBlock(ServerLevel world, BlockPos pos, BlockState blockState, Player player) {
        BlockState old = world.getBlockState(pos);
//        undoMap.put(pos, old);

        AttributeInstance attribute = player.getAttribute(AkatZumaWorldEdit.SET_FLAG_ATTRIBUTE.get());
        if(attribute != null) {
            boolean flag = attribute.getValue() > 0;
            if (flag) {
                world.setBlock(pos, blockState, 2);
                return;
            }
        }

        if(old != blockState){
            setBlockState(world,pos,blockState,false);
            sendBlockUpdated(world,pos );
        }

    }

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
