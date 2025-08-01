package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.MySetBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class SetCommand {
    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                    .then(Commands.literal("set")
                        .then(Commands.argument("方块ID", BlockStateArgument.block(pContext))
                        .executes((context)->{
                            setBlock(context);
                            return 1;
                        })
                    )

//                            .requires((commandSource) -> commandSource.hasPermission(1))
                )
        );
        dispatcher.register(Commands.literal("a").redirect(cmd));

    }

    public static void setBlock(CommandContext<CommandSourceStack> context) {

        ServerPlayer player = context.getSource().getPlayer();

        PlayerMapData PMD = Util.getPMD(player);
        BlockPos bp1= PMD.getPos1(), bp2 = PMD.getPos2();


        BlockInput blockInput =  BlockStateArgument.getBlock(context, "方块ID");
        BlockState blockState =  blockInput.getState();
        boolean playerPermission = context.getSource().hasPermission(2);
        ServerLevel serverlevel = context.getSource().getLevel();

//        Boolean permission = PermissionAPI.getPermission(player, PermissionEventRegister.use_set);

        //判断权限，背包等
        if(PlaceBlock.canSetBlock(bp1,bp2,serverlevel,player, blockState,playerPermission, PMD)){
//            Map<BlockPos,BlockState> undoMap  = new HashMap<>();
//            PMD.getUndoDataMap().push(undoMap);//添加到undo
//            UndoData undoData = new UndoData(serverlevel);
//            PMD.getUndoDataMap().push(undoData);//添加到undo
            //放置方块
            MySetBlock.setBlockFromPos(bp1,bp2,serverlevel,player, blockState);
//            Component blockName = blockState.getBlock().getName().withStyle(ChatFormatting.GREEN);
//            Component setSuccess = Component.translatable("chat.akatzuma.set.success").append(blockName).append(Component.translatable("chat.akatzuma.undo.tip"));
//            AkatZumaWorldEdit.sendClientMessage(setSuccess, player);
            SendCopyMessage.sendSuccessMsg(blockState,player, context.getInput());


        }

        // 设置标志位 此处再把flag设为true
        PMD.setFlag(true);





    }
}
