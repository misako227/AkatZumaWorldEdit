package com.z227.AkatZumaWorldEdit.Commands.brush;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.ShapeBase;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.S2CSphere;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

public class BrushCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("brush")
                                .then(Commands.literal("sphere")
                                        .then(Commands.argument("block", BlockStateArgument.block(pContext))
                                        .then(Commands.argument("radius", IntegerArgumentType.integer(3))
                                        .executes((context)->{
                                            brush(context,false);
                                            return 1;
                                        })
                                        .then(Commands.literal("-h")
                                                .executes((context)->{
                                            brush(context,true);
                                            return 1;
                                                }))
                                        )))
                        ));

    }

    private static void brush(CommandContext<CommandSourceStack> context, boolean hollow) {
        ServerPlayer player = context.getSource().getPlayer();
        PlayerMapData PMD = Util.getPMD(player);
        BlockInput blockInput =  BlockStateArgument.getBlock(context, "block");
        int radius =  IntegerArgumentType.getInteger(context, "radius");
        int height = 0;
        BlockState blockState =  blockInput.getState();
        ServerLevel serverlevel = context.getSource().getLevel();
        ShapeBase shapeBase = new ShapeBase(PMD,serverlevel,player,blockState,radius, height,hollow, "sphere");
        Item item = player.getMainHandItem().getItem();

        PMD.getBrushMap().put(item, new BrushBase(shapeBase));

        NetworkingHandle.sendToClient(new S2CSphere(blockState,radius,hollow), player);

    }
}
