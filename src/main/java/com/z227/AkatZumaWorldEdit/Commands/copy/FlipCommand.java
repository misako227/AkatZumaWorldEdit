package com.z227.AkatZumaWorldEdit.Commands.copy;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.SendToClient;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class FlipCommand {

    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("flip")
                                .executes((context)->{
                                            flip(context,false);
                                            return 1;
                                        }
                                )
                        .then(Commands.literal("up")
                                .executes((context)->{
                                    flip(context,true);
                                    return 1;
                                })
                        )
        ));

    }

    public static void flip(CommandContext<CommandSourceStack> context,boolean up) {

        ServerPlayer player = context.getSource().getPlayer();
//        ServerLevel serverlevel = context.getSource().getLevel();
        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());

        CopyBlock copyBlock = PMD.getCopyBlock();
        if(copyBlock==null){
            Component component = Component.translatable("chat.akatzuma.error.no_copy_map");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return;
        }

        // 设置标志位
        if(!PlaceBlock.cheakFlag(PMD,player))return ;
        PMD.setFlag(false);

        copyBlock.flip(up,false);

        // 设置标志位
        PMD.setFlag(true);
        if(!up)NetworkingHandle.sendToClient(new SendToClient(12), player);
        else NetworkingHandle.sendToClient(new SendToClient(13), player);


    }
}
