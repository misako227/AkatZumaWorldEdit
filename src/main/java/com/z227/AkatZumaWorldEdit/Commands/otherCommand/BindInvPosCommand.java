package com.z227.AkatZumaWorldEdit.Commands.otherCommand;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Capability.BindInventoryPos;
import com.z227.AkatZumaWorldEdit.Capability.BindInventoryPosCapability;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.MySetBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.SendToClientCompoundTag;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.p3pp3rf1y.sophisticatedstorage.block.WoodStorageBlockBase;

import java.util.List;
import java.util.Map;

public class BindInvPosCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("other")
                                        .then(Commands.literal("bind")

                                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                                        .executes((context)->{
                                                            bind(context);
                                                            return 1;
                                                        })
                                                ))
                                        .then(Commands.literal("tp")
                                                .executes((context)->{
                                                    tpBind(context);
                                                    return 1;
                                                }))
                                        .then(Commands.literal("set")
                                                .then(Commands.argument("queryFlag", IntegerArgumentType.integer(1,2))
                                                .then(Commands.argument("blockState", BlockStateArgument.block(pContext))
                                                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes((context)->{
                                            queryBlock(context);
                                            return 1;
                                        })))))

                        ));

    }

    public static void bind(CommandContext<CommandSourceStack> context)
    {
        Player player = context.getSource().getPlayer();
        ServerLevel serverlevel = context.getSource().getLevel();
        PlayerMapData PMD = Util.getPMD(player);
//        int index =  IntegerArgumentType.getInteger(context, "index");
        BlockPos pos = BlockPosArgument.getBlockPos(context, "pos");

        //区块未加载
        if (!serverlevel.hasChunkAt(pos)) {
            Component component = Component.translatable("chat.akatzuma.error.chunk_not_loaded");
            AkatZumaWorldEdit.sendAkatMessage(component,player);
            return;
        }

        Block block =  serverlevel.getBlockState(pos).getBlock();

        Component component = Component.translatable("chat.akatzuma.error.bind_pos");
        boolean isRight = isRightClickBlock(player, pos);
        if(isRight){
            if(block instanceof ChestBlock || block instanceof BarrelBlock){
                bindPos(player, pos);
                PMD.setInvPosMap(pos,player);
                return;
            }else if(Util.isLoadSopStorage()) {
                if (block instanceof WoodStorageBlockBase) {
                    bindPos(player, pos);
                    PMD.setInvPosMap(pos,player);
                    return;
                }
            }
        }



        AkatZumaWorldEdit.sendAkatMessage(component,player);
    }

    public static void bindPos(Player player,BlockPos pos){
        LazyOptional<BindInventoryPos> bindPos = player.getCapability(BindInventoryPosCapability.BIND_INV_POS_CAP);
        if (bindPos.isPresent()) {
                BindInventoryPos bp = bindPos.orElse(null);
                CompoundTag tag = bp.getCompoundNBT();
                tag.putIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
                bp.deserializeNBT(tag);
                NetworkingHandle.sendToClient(new SendToClientCompoundTag(bp.getCompoundNBT()), (ServerPlayer) player);

                Component component = Component.translatable("chat.akatzuma.success.bind_pos");
                AkatZumaWorldEdit.sendAkatMessage(component, player);

            }

    }

    public static void tpBind(CommandContext<CommandSourceStack> context){
        Player player = context.getSource().getPlayer();
        ServerLevel serverlevel = context.getSource().getLevel();
        PlayerMapData PMD = Util.getPMD(player);
        int[] ints = PMD.getInvPosNBT().getIntArray("pos");
        Component component = Component.translatable("chat.akatzuma.error.chunk_not_loaded");
        if(ints.length==3){
            BlockPos pos = new BlockPos(ints[0],ints[1]+1,ints[2]);
            //区块未加载
            if (!serverlevel.hasChunkAt(pos)) {

                AkatZumaWorldEdit.sendAkatMessage(component,player);
                return;
            }
            player.teleportTo(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
            component = Component.translatable("chat.akatzuma.success.tp");
            AkatZumaWorldEdit.sendAkatMessage(component, player);

        }

    }


    public static Boolean isRightClickBlock(Player player, BlockPos blockPos){

//        BlockHitResult blockHitResult = new BlockHitResult(blockPos, Direction.UP, blockPos,false);
        Vec3 vec3 = new Vec3(blockPos.getX(),blockPos.getY(),blockPos.getZ());
        PlayerInteractEvent.RightClickBlock rightClickBlock = new PlayerInteractEvent.RightClickBlock(player, InteractionHand.MAIN_HAND,blockPos,BlockHitResult.miss(vec3, Direction.UP,blockPos));

        MinecraftForge.EVENT_BUS.post(rightClickBlock);
        return !rightClickBlock.isCanceled();
    }


    public static void queryBlock(CommandContext<CommandSourceStack> context){
        Player player = context.getSource().getPlayer();
        ServerLevel world = context.getSource().getLevel();
        int queryFlag =  IntegerArgumentType.getInteger(context, "queryFlag");
        BlockInput blockInput =  BlockStateArgument.getBlock(context, "blockState");
        BlockState blockState =  blockInput.getState();
        BlockPos blockPos = BlockPosArgument.getBlockPos(context, "pos");
//        System.out.println(queryFlag);
//        System.out.println(blockState);
//        System.out.println(blockPos);

        if(setSingleBlock(world, blockPos, blockState, player, queryFlag)){
//            world.setBlock(blockPos, blockState, 16);
            MySetBlock.setBlockNotUpdate(world, blockPos,world.getBlockState(blockPos), blockState);
        }

    }

    public static boolean setSingleBlock(ServerLevel world, BlockPos pos, BlockState blockState, Player player, int queryFlag){
        Component component;
        if (queryFlag == 1 && (blockState == null || blockState == Blocks.AIR.defaultBlockState())) {
            component = Component.translatable("chat.item.query_block_state.null");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return false;
        }
        //检查要放置的位置是不是空气
        if(queryFlag == 1 && !world.getBlockState(pos).is(Blocks.AIR)){
            component = Component.translatable("chat.item.query_block_state.not_air");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return false;
        }
        if (!player.hasPermissions(2)) {
            Map<String, Integer> blackWhiteMap = AkatZumaWorldEdit.defaultBlockMap;    //黑白名单方块
            if(PlaceBlock.checkVip(player))blackWhiteMap = AkatZumaWorldEdit.VipBlockMap;;
            MutableComponent descriptBlockName = blockState.getBlock().getName();
            String blockName = BlockStateString.getBlockName(blockState);
            int n = PlaceBlock.getLimit(blockName, blackWhiteMap);
            //检查黑名单
            if (!PlaceBlock.checkBlackList(player, n, descriptBlockName)) {
                return false;
            }

            //检查是否替换成本MOD的建筑耗材方块
            blockName = PlaceBlock.isReplaceToBuildItem(player,blockName,blackWhiteMap);
            if(blockName.equals("akatzumaworldedit:building_consumable") ){
                blockState = AkatZumaWorldEdit.Building_Consumable_Block.get().defaultBlockState();
            }

            List<Map<Integer, Integer>> blockInInvMap = null;
            if(n > 0 && !player.isCreative()){
                blockInInvMap = PlaceBlock.checkInv(blockName, 1, 1, player, blockState);
                if (blockInInvMap == null ) {
                    return false;
                }
            }


            //检查放置权限
            if (!PlaceBlock.isPlaceBlock(world, player, pos, blockState)) {
                component = Component.translatable("chat.akatzuma.error.not_permission_place");
                AkatZumaWorldEdit.sendAkatMessage(component,player);
                return false;
            }



            //扣除背包
            if(blockInInvMap!=null) {
                PlaceBlock.removeItemInPlayerInv(blockInInvMap, 1, 1, player);
            }

            component = Component.translatable("chat.akatzuma.set.success")
                    .append(descriptBlockName.withStyle(ChatFormatting.GREEN));
            AkatZumaWorldEdit.sendClientMessage(component, player);
         }


        return true;
    }


}
