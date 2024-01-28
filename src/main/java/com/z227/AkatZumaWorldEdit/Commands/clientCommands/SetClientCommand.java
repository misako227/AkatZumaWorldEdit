//package com.z227.AkatZumaWorldEdit.Commands.clientCommands;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.context.CommandContext;
//import com.mojang.brigadier.tree.LiteralCommandNode;
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
//import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
//import net.minecraft.ChatFormatting;
//import net.minecraft.commands.CommandBuildContext;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraft.commands.arguments.blocks.BlockInput;
//import net.minecraft.commands.arguments.blocks.BlockStateArgument;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//
//public class SetClientCommand {
//    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {
//
//        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
//                Commands.literal(AkatZumaWorldEdit.MODID)
//                    .then(Commands.literal("set")
//                        .then(Commands.argument("方块ID", BlockStateArgument.block(pContext))
//                                .executes((context)->{
////                                    CompletableFuture.runAsync(()->{
////                                        setBlock(context);
////                                    });
//                                    new Thread(() -> setBlock(context)).start();
//                                    setBlock(context);
//                                    return 1;
//                                })
//                        )
//                    )
//        );
//        dispatcher.register(Commands.literal("b").redirect(cmd));
//
//    }
//
//    public static void setBlock(CommandContext<CommandSourceStack> context) {
//
//
//
//        Player player = context.getSource().getPlayer();
//        boolean playerPermission = context.getSource().hasPermission(2);
//        BlockInput blockInput =  BlockStateArgument.getBlock(context, "方块ID");
//        BlockState blockState =  blockInput.getState();
//
//        Level world = context.getSource().getUnsidedLevel();
//        System.out.println(world.getMapData("123"));
//        System.out.println("----------------");
//        System.out.println(AkatZumaWorldEdit.PlayerWEMap);
//
//        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
//        BlockPos bp1= PMD.getPos1(), bp2 = PMD.getPos2();
//
//
//
//
////        Level world = player.getCommandSenderWorld();
//        if(PlaceBlock.canSetBlock(bp1,bp2,world,player, blockState,playerPermission, PMD)){
//            AkatZumaWorldEdit.LOGGER.info("this is client side");
//            System.out.println("getDescriptionId:" + AkatZumaWorldEdit.defaultBlockMap);
//        }
//
//
//
//
//
//
//
//        AkatZumaWorldEdit.sendAkatMessage("已替换成",blockState.getBlock().getName().withStyle(ChatFormatting.GREEN), player);
//
//    }
//}
