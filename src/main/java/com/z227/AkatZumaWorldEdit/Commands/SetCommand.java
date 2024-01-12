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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class SetCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                    .then(Commands.literal("set")
                        .then(Commands.argument("方块ID", BlockStateArgument.block(pContext))
                                .executes((context)->{
//                                    CompletableFuture.runAsync(()->{
//                                        setBlock(context);
//                                    });
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
        BlockInput blockInput =  BlockStateArgument.getBlock(context, "方块ID");
        BlockState blockState =  blockInput.getState();

        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
        BlockPos bp1= PMD.getPos1(), bp2 = PMD.getPos2();

        ServerLevel serverlevel = context.getSource().getLevel();
//        Level world = player.getCommandSenderWorld();
        PlaceBlock.traverseCube(bp1,bp2,serverlevel,player, blockState);


        AkatZumaWorldEdit.sendAkatMessage("已替换成",blockState.getBlock().getName().withStyle(ChatFormatting.GREEN), player);

    }
}
