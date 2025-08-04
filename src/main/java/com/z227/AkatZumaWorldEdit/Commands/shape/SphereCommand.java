package com.z227.AkatZumaWorldEdit.Commands.shape;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.ShapeBase;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class SphereCommand {

    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("sphere")
                                .then(Commands.argument("方块ID", BlockStateArgument.block(pContext))
                                        .then(Commands.argument("半径", IntegerArgumentType.integer(3))
                                            .executes((context)->{
                                                setSphere(context,false);
                                                return 1;
                                            })
                                            .then(Commands.literal("-h")
                                                .executes((context)->{
                                                    setSphere(context,true);
                                                    return 1;
                                                })))
                                )
                        )
        );
    }


    public static void setSphere(CommandContext<CommandSourceStack> context, boolean hollow){
        Player player = context.getSource().getPlayer();

        PlayerMapData PMD = Util.getPMD(player);

        BlockInput blockInput =  BlockStateArgument.getBlock(context, "方块ID");
        int radius =  IntegerArgumentType.getInteger(context, "半径");
        int height = 0;
        BlockState blockState =  blockInput.getState();
        ServerLevel serverlevel = context.getSource().getLevel();


        ShapeBase shapeBase = new ShapeBase(PMD,serverlevel,player,blockState,radius, height,hollow, "sphere");
        if(shapeBase.place()){
//            shapeBase.sphere();
//            shapeBase.placeBlocks();
            SendCopyMessage.sendSuccessMsg(blockState,player, context.getInput());
        }
        // 设置标志位
        PMD.setFlag(true);
    }
}
