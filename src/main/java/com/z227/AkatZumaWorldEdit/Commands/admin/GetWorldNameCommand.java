package com.z227.AkatZumaWorldEdit.Commands.admin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;

public class GetWorldNameCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("world")
                                .requires((commandSource) -> commandSource.hasPermission(2))
                                .executes((context) -> {
                                    getWorldName(context);
                                            return 1;
                                        }))
        );
    }

    public static void getWorldName(CommandContext<CommandSourceStack> context) {
        Player player = context.getSource().getPlayer();
        Level level = player.level();
        String worldName =  ((ServerLevelData)level.getLevelData()).getLevelName();
        String dimension = level.dimension().location().toString();
        String tempName = worldName + "/" + dimension;
        Component copyMessage = SendCopyMessage.send(tempName);
        AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.get_world_name").append(copyMessage), player);

    }

}
