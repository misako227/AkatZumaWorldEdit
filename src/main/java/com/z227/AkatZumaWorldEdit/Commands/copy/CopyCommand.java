package com.z227.AkatZumaWorldEdit.Commands.copy;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.CopyBlock;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class CopyCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                    .then(Commands.literal("copy")
//                    .then(Commands.argument("方块ID", BlockStateArgument.block(pContext))
                    .executes((context)->{
                        copyBlock(context);
                        return 1;
                        }
                    )
                )
        );
//        dispatcher.register(Commands.literal("a").redirect(cmd));

    }

    public static void copyBlock(CommandContext<CommandSourceStack> context) {

        Player player = context.getSource().getPlayer();
        ServerLevel serverlevel = context.getSource().getLevel();

        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());

        CopyBlock copyBlock = new CopyBlock(PMD, player);

        if(copyBlock.init(serverlevel)){
            PMD.setCopyBlock(copyBlock);        //添加到PMD
            Component component = Component.translatable("chat.akatzuma.copyBlock.copy_success").append(String.valueOf(copyBlock.getCopyMap().size()));
            AkatZumaWorldEdit.sendAkatMessage(component,player);
        }

        // 设置标志位
        PMD.setFlag(true);

    }






}
