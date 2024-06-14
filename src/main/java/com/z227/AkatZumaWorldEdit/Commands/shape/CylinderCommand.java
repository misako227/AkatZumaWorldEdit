package com.z227.AkatZumaWorldEdit.Commands.shape;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.ShapeBase;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class CylinderCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("cyl")
                                .then(Commands.argument("方块ID", BlockStateArgument.block(pContext))
                                        .then(Commands.argument("半径", IntegerArgumentType.integer(3))
                                                .then(Commands.argument("高度", IntegerArgumentType.integer(1,500))
                                                .executes((context)->{
                                                    setCyl(context,false,0,0,0);
                                                    return 1;
                                                })
//                                                .then(Commands.argument("x角度", FloatArgumentType.floatArg(-360, 360))
//                                                        .executes((context)->{
//                                                            float xAngle = FloatArgumentType.getFloat(context,"x角度");
//                                                            setCyl(context,false,xAngle,0,0);
//                                                            return 1;
//                                                        }
//                                                    )
////                                                .then(Commands.argument("y角度", FloatArgumentType.floatArg(-360, 360))
////                                                            .executes((context)->{
////                                                                        float xAngle = FloatArgumentType.getFloat(context,"x角度");
////                                                                        float yAngle  = FloatArgumentType.getFloat(context,"y角度");
////                                                                        setCyl(context,false,xAngle,yAngle,0);
////                                                                        return 1;
////                                                                    }
////                                                            )
//
//                                                .then(Commands.argument("z角度", FloatArgumentType.floatArg(-360, 360))
//                                                        .executes((context)->{
//                                                                    float xAngle = FloatArgumentType.getFloat(context,"x角度");
////                                                                    float yAngle  = FloatArgumentType.getFloat(context,"y角度");
//                                                                    float zAngle  = FloatArgumentType.getFloat(context,"z角度");
//                                                                    setCyl(context,false,xAngle,0,zAngle);
//                                                                    return 1;
//                                                                }
//                                                        )
//                                                )
//
//                        )
                                                ))))
        );
    }

    public static void setCyl(CommandContext<CommandSourceStack> context,boolean hollow,float xAngle,float yAngle,float zAngle){
        Player player = context.getSource().getPlayer();

        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());

        BlockInput blockInput =  BlockStateArgument.getBlock(context, "方块ID");
        int radius =  IntegerArgumentType.getInteger(context, "半径");
        int height =  IntegerArgumentType.getInteger(context, "高度");
        BlockState blockState =  blockInput.getState();
        ServerLevel serverlevel = context.getSource().getLevel();

        ShapeBase shapeBase = new ShapeBase(PMD,serverlevel,player,blockState, radius, height, hollow, "cyl",xAngle,yAngle,zAngle);
        if(shapeBase.init()){
            shapeBase.cyl();
            SendCopyMessage.sendSuccessMsg(blockState,player, context.getInput());
        }
        // 设置标志位
        PMD.setFlag(true);
    }
}
