package com.z227.AkatZumaWorldEdit.Commands.otherCommand;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Capability.BindInventoryPos;
import com.z227.AkatZumaWorldEdit.Capability.BindInventoryPosCapability;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraftforge.common.util.LazyOptional;
import net.p3pp3rf1y.sophisticatedstorage.block.WoodStorageBlockBase;

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
        if(block instanceof ChestBlock || block instanceof BarrelBlock){
            bindPos(player, pos);
            PMD.setInvPosMap(pos,player);
            return;
//            bindPos.ifPresent(bp -> {
//                CompoundTag tag = bp.getCompoundNBT();
//                tag.putIntArray("pos"+index, new int[]{pos.getX(),pos.getY(),pos.getZ()});
//                bp.setCompoundNBT(tag);
//                System.out.println(bp.getCompoundNBT());
//                Component mess = Component.translatable("chat.akatzuma.success.bind_pos");
//                AkatZumaWorldEdit.sendAkatMessage(mess,player);
//                return;
//            });
        }else if(Util.isLoadSopStorage()) {
            if (block instanceof WoodStorageBlockBase) {
                bindPos(player, pos);
                PMD.setInvPosMap(pos,player);
                return;
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

        }



    }


}
