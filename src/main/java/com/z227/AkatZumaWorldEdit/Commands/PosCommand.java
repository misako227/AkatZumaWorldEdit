package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Items.WoodAxeItem;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.SendToClient;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
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
                        }))
                        .then(Commands.literal("pos2").executes((context) -> {
                            pos(context,false);
                            return 1;
                        }))



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

}
