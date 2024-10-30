package com.z227.AkatZumaWorldEdit.Commands.copy;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.SendToClient;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class CopyCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                    .then(Commands.literal("copy")
//                    .then(Commands.argument("方块ID", BlockStateArgument.block())
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

        ServerPlayer player = null;
            try {
                player = context.getSource().getPlayerOrException();
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        ServerLevel serverlevel = context.getSource().getLevel();

        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());

        CopyBlock copyBlock = new CopyBlock(PMD, player);

        if(copyBlock.init(serverlevel)){
            PMD.setCopyBlock(copyBlock);        //添加到PMD
            Component component = new TranslatableComponent("chat.akatzuma.copyBlock.copy_success").append(String.valueOf(copyBlock.getCopyMap().size()));
            AkatZumaWorldEdit.sendAkatMessage(component,player);
            NetworkingHandle.sendToClient(new SendToClient(11), player);
            Util.recordPosLog(context.getInput(),player);
        }

        // 设置标志位
        PMD.setFlag(true);


    }






}
