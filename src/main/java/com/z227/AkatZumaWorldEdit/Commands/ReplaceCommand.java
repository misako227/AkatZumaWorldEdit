package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.ReplaceBlock;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class ReplaceCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {
        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("replace")
                        .then(Commands.argument("被替换的方块", BlockStateArgument.block(pContext))
                            .then(Commands.argument("替换成的方块", BlockStateArgument.block(pContext))
                                .executes((context)->{
                                    replace(context);
                                    return 1;
                                        })
                                ))
                        )
        );

    }

    public static void replace(CommandContext<CommandSourceStack> context) {
        Player player = context.getSource().getPlayer();
        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());

        BlockInput blockInput =  BlockStateArgument.getBlock(context, "被替换的方块");
        BlockInput blockOutput =  BlockStateArgument.getBlock(context, "替换成的方块");
        BlockState inputState =  blockInput.getState();
        BlockState outputState =  blockOutput.getState();
        ServerLevel serverlevel = context.getSource().getLevel();

        ReplaceBlock replaceBlock = new ReplaceBlock(PMD,serverlevel,player,inputState,outputState);
        if(replaceBlock.init()){
            Map<BlockPos,BlockState> undoMap  = new HashMap<>();
            PMD.getUndoDataMap().push(undoMap);//添加到undo
            replaceBlock.replace(undoMap);
            SendCopyMessage.sendSuccessMsg(outputState, player, context.getInput());
        }


        // 设置标志位
        PMD.setFlag(true);

    }
}
