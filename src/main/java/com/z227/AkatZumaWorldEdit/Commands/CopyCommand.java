package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlaceBlock;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class CopyCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                    .then(Commands.literal("copy")
                            .executes((context)->{
                                copyBlock(context);
                                return 1;
                            })

//                            .requires((commandSource) -> commandSource.hasPermission(1))
                    )
        );
        dispatcher.register(Commands.literal("a").redirect(cmd));

    }

    public static void copyBlock(CommandContext<CommandSourceStack> context) {

        Player player = context.getSource().getPlayer();

//        BlockState blockState =  blockInput.getState();
        boolean playerPermission = context.getSource().hasPermission(2);
        ServerLevel serverlevel = context.getSource().getLevel();

        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
        BlockPos bp1= PMD.getPos1(), bp2 = PMD.getPos2();

        PlaceBlock.copy(bp1,bp2,serverlevel,player);
        System.out.println(PMD.getUndoDataMap().size());

//        if(PlaceBlock.canSetBlock(bp1,bp2,serverlevel,player, blockState,playerPermission)){
////            AkatZumaWorldEdit.LOGGER.info("this is server side");
//            PlaceBlock.traverseCube(bp1,bp2,serverlevel,player, blockState);
//            Component blockName = blockState.getBlock().getName().withStyle(ChatFormatting.GREEN);
//            Component setSuccess = Component.translatable("chat.akatzuma.set.success").append(blockName);
//            AkatZumaWorldEdit.sendAkatMessage(setSuccess, player);
//        }






    }
}
