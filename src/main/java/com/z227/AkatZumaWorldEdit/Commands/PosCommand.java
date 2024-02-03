package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Items.WoodAxeItem;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

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
        Player player =  context.getSource().getPlayer();
        BlockPos pos = player.getOnPos();
        WoodAxeItem.clickPos(serverLevel,pos, player,b);
    }

}
