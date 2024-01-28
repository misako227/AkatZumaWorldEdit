//package com.z227.AkatZumaWorldEdit.Commands;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.arguments.IntegerArgumentType;
//import com.mojang.brigadier.context.CommandContext;
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
//import com.z227.AkatZumaWorldEdit.Core.modifyBlock.StackBlock;
//import net.minecraft.commands.CommandBuildContext;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraft.core.BlockPos;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.block.state.BlockState;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MoveCommand {
//    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {
//        dispatcher.register(
//                Commands.literal(AkatZumaWorldEdit.MODID)
//                    .then(Commands.literal("move")
//                    .then(Commands.argument("移动距离", IntegerArgumentType.integer())
//                        .executes(context -> {
//                            move(context,0);return 1;
//                        })
//                        .then(Commands.literal("up")
//                                .executes((context)->{
//                                    move(context,2);
//                                    return 1;
//                                }))
//                        .then(Commands.literal("down")
//                                .executes((context)->{
//                                    move(context,4);
//                                    return 1;
//                                }))
//                    ))
//            );
//        }
//
//        //0 = null, 2 = up, 4 = down
//        public static void move(CommandContext<CommandSourceStack> context, int direction){
//            int moveNum =  IntegerArgumentType.getInteger(context, "移动距离");
//            ServerLevel serverlevel = context.getSource().getLevel();
//            Player player = context.getSource().getPlayer();
//            PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
//            //undo
//            Map<BlockPos, BlockState> undoMap  = new HashMap<>();
//            PMD.getUndoDataMap().push(undoMap);
//
//            StackBlock stackBlock = new StackBlock(PMD,serverlevel,player,moveNum,direction);
//            if(stackBlock.init()){
//                stackBlock.stack(undoMap);
//            }
//
//
//            PMD.setFlag(true);
//        }
//
//
//}
