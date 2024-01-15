package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class CopyCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                    .then(Commands.literal("copy")
                            .then(Commands.argument("方块ID", BlockStateArgument.block(pContext))
                            .executes((context)->{
                                copyBlock(context);
                                return 1;
                            })
                            )

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

        boolean p = player.getInventory().contains(Items.OAK_LOG.getDefaultInstance());

        BlockInput blockInput =  BlockStateArgument.getBlock(context, "方块ID");
        BlockState blockState =  blockInput.getState();
        Map<String, Integer> blockInInvMap = Util.findBlockFromPlayerInv(blockState,player);
        if (blockInInvMap !=null){
            System.out.println("blockInInvMap is not null ");
        }
        System.out.println("背包中的方块"+ blockInInvMap);

        String sld = serverlevel.getLevel().toString();
        String leavedata =  serverlevel.getLevelData().toString();
//        serverlevel.getLevelData()
//        serverlevel.getChunk()
        System.out.println("serverlevel is= "+ sld);
        System.out.println("leveldata is= "+ leavedata);
//        player.getInventory().getContainerSize();
//        int itemcount = player.getInventory().getItem(1);
//        if(itemcount != null) .getCount();

        System.out.println(blockState.getBlock().toString());
        System.out.println("defaultBlockMap is= "+ AkatZumaWorldEdit.defaultBlockMap);
        System.out.println("VipBlockMap is= "+ AkatZumaWorldEdit.VipBlockMap);

//        player.getInventory().findSlotMatchingItem(ItemStack.of());


//        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
//        BlockPos bp1= PMD.getPos1(), bp2 = PMD.getPos2();

//        PlaceBlock.copy(bp1,bp2,serverlevel,player);



//        if(PlaceBlock.canSetBlock(bp1,bp2,serverlevel,player, blockState,playerPermission)){
////            AkatZumaWorldEdit.LOGGER.info("this is server side");
//            PlaceBlock.traverseCube(bp1,bp2,serverlevel,player, blockState);
//            Component blockName = blockState.getBlock().getName().withStyle(ChatFormatting.GREEN);
//            Component setSuccess = Component.translatable("chat.akatzuma.set.success").append(blockName);
//            AkatZumaWorldEdit.sendAkatMessage(setSuccess, player);
//        }






    }
}
