package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlaceBlock;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class SetCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                    .then(Commands.literal("set")
                        .then(Commands.argument("方块ID", BlockStateArgument.block(pContext))
                        .executes((context)->{
                            setBlock(context);
                            return 1;
                        })
                    )

//                            .requires((commandSource) -> commandSource.hasPermission(1))
                )
        );
        dispatcher.register(Commands.literal("a").redirect(cmd));

    }

    public static void setBlock(CommandContext<CommandSourceStack> context) {

        Player player = context.getSource().getPlayer();

        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
        BlockPos bp1= PMD.getPos1(), bp2 = PMD.getPos2();


        BlockInput blockInput =  BlockStateArgument.getBlock(context, "方块ID");
        BlockState blockState =  blockInput.getState();
        boolean playerPermission = context.getSource().hasPermission(2);
        ServerLevel serverlevel = context.getSource().getLevel();

//        Level world = player.getCommandSenderWorld();
        if(PlaceBlock.canSetBlock(bp1,bp2,serverlevel,player, blockState,playerPermission, PMD)){
            Map<BlockPos,BlockState> undoMap  = new HashMap<>();
            PMD.getUndoDataMap().push(undoMap);
            PlaceBlock.traverseCube(bp1,bp2,serverlevel,player, blockState, undoMap);
            Component blockName = blockState.getBlock().getName().withStyle(ChatFormatting.GREEN);
            Component setSuccess = Component.translatable("chat.akatzuma.set.success").append(blockName).append(Component.translatable("chat.akatzuma.undo.tip"));
            AkatZumaWorldEdit.sendAkatMessage(setSuccess, player);


        }

        // 设置标志位 此处再把flag设为true
        PMD.setFlag(true);





    }
}
