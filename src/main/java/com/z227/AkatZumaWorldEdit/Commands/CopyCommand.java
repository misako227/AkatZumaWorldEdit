package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CopyCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                    .then(Commands.literal("copy")
                            .then(Commands.argument("方块ID", BlockStateArgument.block(pContext))
                            .executes((context)->{
                                copyBlock(context);
                                return 1;
                            })
                            )

//                            .requires((commandSource) -> commandSource.hasPermission(1))
                    )
        );
        dispatcher.register(Commands.literal("a").redirect(cmd));

    }

    public static void copyBlock(CommandContext<CommandSourceStack> context) {

        Player player = context.getSource().getPlayer();

//        BlockState blockState =  blockInput.getState();
        boolean playerPermission = context.getSource().hasPermission(2);
        ServerLevel serverlevel = context.getSource().getLevel();

        boolean p = player.getInventory().contains(Items.OAK_LOG.getDefaultInstance());

        BlockInput blockInput =  BlockStateArgument.getBlock(context, "方块ID");
        BlockState blockState =  blockInput.getState();
//        Map<String, Map<Integer,Integer>> blockInInvMap = Util.findPlayerInv(player);
        String blockName = BlockStateString.getBlockName(blockState);


        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());

//        ArrayDeque undoMap  = PMD.getUndoDataMap().getStack();
//        System.out.println(undoMap);

        Map<BlockPos,BlockState> undoMap  = new HashMap<>();
        PMD.getUndoDataMap().push(undoMap);
        BlockPos pos1= PMD.getPos1(), pos2 = PMD.getPos2();

        ExecutorService executor = Executors.newFixedThreadPool(4);
//////        PlaceBlock.traverseCube(bp1,bp2,serverlevel,player, blockState, undoMap, executor);
//
//        List<BlockPos> testList = new ArrayList<>();
//        for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++) {
//            for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++) {
//                for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++) {
//                    BlockPos bp = new BlockPos(x,y,z);
//                    testList.add(bp);
//                }
//            }
//        }
//        System.out.println(testList);
//        //遍历testList
//        for (BlockPos bp : testList) {
//            executor
//                    .execute(()-> serverlevel.setBlock(bp, blockState, 2));
//        }
        copyCube(pos1, pos2, serverlevel, player, blockState, undoMap, executor);
        executor.shutdown();
//        testList.parallelStream()
//                .forEach(bp -> );

//        player.getEyePosition()
//        Level world = context.getSource().getLevel().getChunk(PMD.getPos1()).getSections();
//        System.out.println(Arrays.toString(context.getSource().getLevel().getChunk(PMD.getPos1()).getSections()));
//        Component blockName = blockState.getBlock().getName().withStyle(ChatFormatting.GREEN);
        Component setSuccess = Component.translatable("chat.akatzuma.set.success").append(blockName).append(Component.translatable("chat.akatzuma.undo.tip"));
        AkatZumaWorldEdit.sendAkatMessage(setSuccess, player);





    }

    //遍历两个坐标之间的每个点
    public static void copyCube(BlockPos pos1, BlockPos pos2, ServerLevel world, Player player, BlockState blockState, Map<BlockPos,BlockState> undoMap, ExecutorService executor) {
        for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++) {
            for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++) {
                for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++) {

                    BlockPos v3 = new BlockPos(x, y, z);
                    BlockPos blockpos = v3.immutable();
//                    undoMap.put(v3,world.getBlockState(v3));
//                    world.setBlock(blockpos,blockState, 2);
                    int chunkXOffset = Math.abs(x%16);
                    int chunkYOffset = Math.abs(y%16);
                    int chunkZOffset = Math.abs(z%16);
//                    LevelChunk levelchunk = world.getChunkAt(blockpos);
                    world.getChunk(blockpos).getSection(Math.abs((y+64)/16)).setBlockState(chunkXOffset,chunkYOffset,chunkZOffset, blockState, false);
//                    world.onBlockStateChange(blockpos,old,blockState);
                    Optional<Holder<PoiType>> optional1 = PoiTypes.forState(blockState);
                    optional1.ifPresent(poiTypeHolder ->{
                        world.getServer().execute(() -> {
                            world.getPoiManager().add(blockpos, poiTypeHolder);
                            DebugPackets.sendPoiAddedPacket(world, blockpos);
                        });
                    });

//                    int j = x & 15;
//                    int k = y & 15;
//                    int l = z & 15;
//                    world.getChunk(blockpos).getSection(world.getSectionIndex(y)).setBlockState(j,k,l, blockState, false);
//                    world.getChunkSource().getLightEngine().updateSectionStatus(blockpos,true);
//                    ChunkPos chunkPos = new ChunkPos(blockpos);
//                    CoreLevelChunk coreLevelChunk = new CoreLevelChunk(world,chunkPos);
//                    BlockState coreblockstate = coreLevelChunk.setBlockState(blockpos,blockState,(2 & 64) != 0);
//                    if (coreblockstate == null) {
//                        System.out.println("failed to set blockstate is null");
//                        return ;
//                    }else{
//                        BlockState blockstate1 = world.getBlockState(blockpos);
//                        world.markAndNotifyBlock(blockpos, levelchunk, coreblockstate, blockState, 2, 512);
//                        System.out.println("set blockstate success");
//
//                    }
////
//                    world.onBlockStateChange(blockpos,old,blockState);
//                    Optional<Holder<PoiType>> optional1 = PoiTypes.forState(blockState);
//                    optional1.ifPresent(poiTypeHolder ->{
//                        world.getServer().execute(() -> {
//                            world.getPoiManager().add(blockpos, poiTypeHolder);
//                            DebugPackets.sendPoiAddedPacket(world, blockpos);
//                    });
//                        });
//                    world.getServer().execute();

//
//                    executor.execute(()->{
//
//                    });

//                     int  finalx = x;
//                    final int finaly = y;
//                    int finalz = z;
//                    executor.execute(() -> {
//                        BlockPos v3 = new BlockPos(finalx, finaly, finalz);
////                        undoMap.put(v3,world.getBlockState(v3));
////                        world.setBlock(v3,blockState, 2);
//                        BlockPos pPos = v3.immutable();
//                        if (world.isOutsideBuildHeight(pPos)) return;
//                        int i2 = world.getChunk(pPos).getSectionsCount() - 1;
//                        LevelChunkSection levelchunksection = world.getChunk(pPos).getSection(i2);
//                        levelchunksection.setBlockState(finalx,finaly,finalz, blockState, false);
////                        world.getChunk(pPos).setBlockState(pPos,blockState,false);
////                        LevelChunkSection.setBlockState(finalx,finaly,finalz, blockState, false);
//                            });


                }
            }
        }
    }


}
