package com.z227.AkatZumaWorldEdit.Commands.brush;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.ShapeBase;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.brushPacket.S2CPasteBrush;
import com.z227.AkatZumaWorldEdit.network.brushPacket.S2CSphere;
import com.z227.AkatZumaWorldEdit.network.brushPacket.S2CUnbind;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class BrushCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("brush")
                                .then(Commands.literal("sphere")
                                        .then(Commands.argument("block", BlockStateArgument.block(pContext))
                                        .then(Commands.argument("半径", IntegerArgumentType.integer(3))
                                        .executes((context)->{
                                            sphereBrush(context,false, "sphere");
                                            return 1;
                                        })
                                        .then(Commands.literal("-h")
                                                .executes((context)->{
                                            sphereBrush(context,true, "sphere");
                                            return 1;
                                                })))))
                                .then(Commands.literal("cyl")
                                        .then(Commands.argument("block", BlockStateArgument.block(pContext))
                                                .then(Commands.argument("半径", IntegerArgumentType.integer(3))
                                                        .then(Commands.argument("高度", IntegerArgumentType.integer(1,500))
                                                                .executes((context)->{
                                                                    sphereBrush(context,false, "cyl");
                                                                    return 1;
                                                                })
                                                                .then(Commands.literal("-h")
                                                                        .executes((context)->{
                                                                            sphereBrush(context,true, "cyl");
                                                                            return 1;
                                                                        }))))))
                                .then(Commands.literal("ellipse")
                                        .then(Commands.argument("block", BlockStateArgument.block(pContext))
                                                .then(Commands.argument("东西半径", IntegerArgumentType.integer(3))
                                                        .then(Commands.argument("南北半径", IntegerArgumentType.integer(3))
                                                                .then(Commands.argument("高度", IntegerArgumentType.integer(3,500))
                                                                        .executes((context)->{
                                                                            sphereBrush(context,false, "ellipse");
                                                                            return 1;
                                                                        })
                                                                        .then(Commands.literal("-h")
                                                                                .executes((context)->{
                                                                                    sphereBrush(context,true, "ellipse");
                                                                                    return 1;
                                                                                })))))
                                        )
                                ).then(Commands.literal("paste")
                                        .executes((context)->{pasteBrush(context,true);
                                            return 0;
                                        }).then(Commands.literal("-a")
                                                .executes((context)->{pasteBrush(context,false);
                                                    return 0;}
                                        ))
                        ).then(Commands.literal("unbind")
                                                .executes(BrushCommand::unbind)
                                        )


                        ));

    }

    private static void sphereBrush(CommandContext<CommandSourceStack> context, boolean hollow, String shape) {
        ServerPlayer player = context.getSource().getPlayer();
        PlayerMapData PMD = Util.getPMD(player);

        Item item = player.getMainHandItem().getItem();

        if(item == Items.AIR) {
            AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.error.brush_air"), player);
            return;
        }
        BlockInput blockInput =  BlockStateArgument.getBlock(context, "block");
        int radius =  0;
        int height = 0;
        int radiusZ = 0;
        BlockState blockState =  blockInput.getState();
        ServerLevel serverlevel = context.getSource().getLevel();

        switch (shape) {
            case "sphere" -> radius = IntegerArgumentType.getInteger(context, "半径");
            case "cyl" -> {
                radius = IntegerArgumentType.getInteger(context, "半径");
                height = IntegerArgumentType.getInteger(context, "高度");
            }
            case "ellipse" -> {
                radius = IntegerArgumentType.getInteger(context, "东西半径");
                height = IntegerArgumentType.getInteger(context, "高度");
                radiusZ = IntegerArgumentType.getInteger(context, "南北半径");
            }
        }
        ShapeBase shapeBase = new ShapeBase(PMD,serverlevel,player,blockState,radius, height,hollow, shape);
        shapeBase.setTeleport(false);

        if(shape.equals("ellipse")){
            shapeBase.setRadiusZ(radiusZ);
        }

        PMD.getBrushMap().put(item, new BrushBase(shapeBase));

        NetworkingHandle.sendToClient(new S2CSphere(blockState,radius,radiusZ,height,hollow), player);

    }

        private static void pasteBrush(CommandContext<CommandSourceStack> context, boolean air) {
            ServerPlayer player = context.getSource().getPlayer();
            PlayerMapData PMD = Util.getPMD(player);
            ServerLevel serverlevel = context.getSource().getLevel();
            Item item = player.getMainHandItem().getItem();
            if(item == Items.AIR) {
                AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.error.brush_air"), player);
                return;
            }
            CopyBlock copyBlock =  new CopyBlock(PMD, player);
            if(copyBlock.init(serverlevel)){

                copyBlock.modifyPlayerCopyPos();
                copyBlock.setAir(air);
                PMD.getBrushMap().put(item, new BrushBase(copyBlock));

                NetworkingHandle.sendToClient(new S2CPasteBrush(), player);


            }

            // 设置标志位
            PMD.setFlag(true);


        }


        public static int unbind(CommandContext<CommandSourceStack> context) {
            ServerPlayer player = context.getSource().getPlayer();
            PlayerMapData PMD = Util.getPMD(player);
            Item item = player.getMainHandItem().getItem();
            Map<Item, BrushBase> brushMap = PMD.getBrushMap();

            brushMap.remove(item);

            NetworkingHandle.sendToClient(new S2CUnbind(), player);

            return 1;
        }

}


