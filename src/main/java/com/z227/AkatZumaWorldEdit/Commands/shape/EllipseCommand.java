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

public class EllipseCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("ellipse")
                                .then(Commands.argument("方块ID", BlockStateArgument.block(pContext))
                                        .then(Commands.argument("东西半径", IntegerArgumentType.integer(3))
                                            .then(Commands.argument("南北半径", IntegerArgumentType.integer(3))
                                                .then(Commands.argument("高度", IntegerArgumentType.integer(3))
                                                        .executes((context)->{
                                                            setEll(context,false);
                                                            return 1;
                                                        })
                                                        .then(Commands.literal("-h")
                                                                .executes((context)->{
                                                                    setEll(context,true);
                                                                    return 1;
                                                                })))))
                                )
                        )
        );
    }

    public static void setEll(CommandContext<CommandSourceStack> context,boolean hollow){
        Player player = context.getSource().getPlayer();

        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());

        BlockInput blockInput =  BlockStateArgument.getBlock(context, "方块ID");
        int radius =  IntegerArgumentType.getInteger(context, "东西半径");
        int radiusZ =  IntegerArgumentType.getInteger(context, "南北半径");
        int height =  IntegerArgumentType.getInteger(context, "高度");
        BlockState blockState =  blockInput.getState();
        ServerLevel serverlevel = context.getSource().getLevel();

        ShapeBase shapeBase = new ShapeBase(PMD,serverlevel,player,blockState, radius,radiusZ, height, hollow, "ellipse");
        if(shapeBase.init()){
            shapeBase.ellipse();
            SendCopyMessage.sendSuccessMsg(blockState,player);
        }
        // 设置标志位
        PMD.setFlag(true);
    }
}
