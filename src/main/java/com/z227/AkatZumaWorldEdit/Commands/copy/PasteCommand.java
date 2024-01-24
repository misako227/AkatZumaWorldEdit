package com.z227.AkatZumaWorldEdit.Commands.copy;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.CopyBlock;
import com.z227.AkatZumaWorldEdit.Core.PlaceBlock;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class PasteCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
            Commands.literal(AkatZumaWorldEdit.MODID)
                .then(Commands.literal("paste")
                    .executes((context)->{
                        paste(context);
                        return 1;
                    }
                )
            )
        );

    }

    public static void paste(CommandContext<CommandSourceStack> context) {

        Player player = context.getSource().getPlayer();
        boolean playerPermission = context.getSource().hasPermission(2);
        ServerLevel serverlevel = context.getSource().getLevel();
        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
        //undo
        Map<BlockPos, BlockState> undoMap  = new HashMap<>();
        PMD.getUndoDataMap().push(undoMap);


        CopyBlock copyBlock = PMD.getCopyBlock();
        if(copyBlock==null){
            Component component = Component.translatable("chat.akatzuma.error.no_copy_map");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return;
        }

        // 设置标志位
        if(!PlaceBlock.cheakFlag(PMD,player))return ;
        PMD.setFlag(false);

        copyBlock.setPlayerPastePos(player.getOnPos());//粘帖时位置
        copyBlock.setPasteVec3(player.getDirection().getNormal());//粘帖时朝向
        copyBlock.pasteBlock(serverlevel, undoMap);

        // 设置标志位
        PMD.setFlag(true);


    }




}
