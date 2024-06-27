package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Items.WoodAxeItem;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.SendToClient;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class PosCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("pos1").executes((context) -> {
                            pos(context,true);
                            return 1;
                        })
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes((context) -> setPos(context,true))
                                ))
                        .then(Commands.literal("pos2").executes((context) -> {
                            pos(context,false);
                            return 1;
                        })
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes((context) -> setPos(context,false))
                        ))
        );
    }

    private static void pos(CommandContext<CommandSourceStack> context,boolean b) {
        ServerLevel serverLevel = context.getSource().getLevel();
        ServerPlayer player =  context.getSource().getPlayer();
        BlockPos pos = BlockPos.containing(player.getEyePosition());
        if(WoodAxeItem.clickPos(serverLevel,pos, player,b)){
            if(b)NetworkingHandle.sendToClient(new SendToClient(1), player);
            else NetworkingHandle.sendToClient(new SendToClient(2), player);
        }

    }

    private static int setPos(CommandContext<CommandSourceStack> context,boolean b) {
        BlockPos blockPos = BlockPosArgument.getBlockPos(context, "pos");
        ServerPlayer player =  context.getSource().getPlayer();
        PlayerMapData PMD = Util.getPMD(player);
        ServerLevel serverLevel = context.getSource().getLevel();
        if(!WoodAxeItem.clickPos(serverLevel,blockPos, player,b)){
            if(b)NetworkingHandle.sendToClient(new SendToClient(3), player);
            else NetworkingHandle.sendToClient(new SendToClient(4), player);
        }
        return 1;
    }

}
