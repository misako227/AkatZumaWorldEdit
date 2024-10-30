package com.z227.AkatZumaWorldEdit.Commands.brush;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.ShapeBase;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.brushPacket.S2CMask;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;

public class MaskCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("mask")
                                .then(Commands.argument("block", BlockStateArgument.block())
                                        .executes(context -> mask(context,true))
                                )
                        .then(Commands.literal("not")
                                .then(Commands.argument("block", BlockStateArgument.block())
                                .executes(context -> mask(context,false))
                        )))
        );
    }

    public static int mask(CommandContext<CommandSourceStack> context,boolean flag) {
        BlockInput blockInput = BlockStateArgument.getBlock(context, "block");
        BlockState blockState = blockInput.getState();
        ServerPlayer player = null;
            try {
                player = context.getSource().getPlayerOrException();
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        PlayerMapData PMD = Util.getPMD(player);
        Item item = player.getMainHandItem().getItem();
        BrushBase brushBase = PMD.getBrushMap().get(item);

        if(brushBase == null) {
            AkatZumaWorldEdit.sendAkatMessage(new TranslatableComponent("chat.akatzuma.mask_null"), player);
            return 0;
        }

        String shape = brushBase.getShape();
        if(shape.equals("paste")){
//            brushBase.setMaskFlag(flag);
            CopyBlock copyBlock = brushBase.getCopyBlock();
            copyBlock.setMask(flag);
            copyBlock.putMaskMap(blockState);
            brushBase.syncCopyBlock();
        }else{
            ShapeBase shapeBase = brushBase.getShapeBase();
            shapeBase.setMask(flag);
            shapeBase.putMaskMap(blockState);
            brushBase.syncShape();
        }



//        Component blockName = blockState.getBlock().getName();
        NetworkingHandle.sendToClient(new S2CMask(blockState,flag), player);
//        AkatZumaWorldEdit.sendAkatMessage(new TextComponent("")
//                .append(new TranslatableComponent("chat.akatzuma.success.add_viplayer")).withStyle(ChatFormatting.GREEN)
//                .append(blockName), player);
        return 1;
    }
}
