package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.MySetBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class LineCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {

        dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID)
                        .then(Commands.literal("line")
                            .then(Commands.argument("方块ID", BlockStateArgument.block(pContext))
                                .executes((context) -> {
                            line(context);
                            return 1;
                        })))


        );
    }

    private static void line(CommandContext<CommandSourceStack> context) {
        ServerLevel serverLevel = context.getSource().getLevel();
        ServerPlayer player =  context.getSource().getPlayer();
        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
        BlockPos pos1 = PMD.getPos1();
        BlockPos pos2 = PMD.getPos2();
        BlockInput blockInput =  BlockStateArgument.getBlock(context, "方块ID");
        BlockState blockState =  blockInput.getState();

        if(PlaceBlock.checkPos(pos1, pos2, player, PMD)){
            Vec3i vec3i = PlaceBlock.calculateCubeDimensions(pos1,pos2);
            int num = (int) Math.sqrt(Math.pow(vec3i.getX(),2) + Math.pow(vec3i.getY(),2) + Math.pow(vec3i.getZ(),2));
            if(num<5 ){
                Component com = Component.translatable("chat.akatzuma.error.line_too_short");
                AkatZumaWorldEdit.sendAkatMessage(com,player);
                PMD.setFlag(true);
                return;
            }

            if(PlaceBlock.canPlaceBlock(pos1,pos2,serverLevel,player,blockState,num, player.hasPermissions(2),PMD)){
                if(drawLine(pos1, pos2, serverLevel, blockState, player, PMD)){
                    SendCopyMessage.sendSuccessMsg(blockState,player, context.getInput());
                }
            }
        }


        PMD.setFlag(true);


    }

    public static boolean drawLine(BlockPos pos1, BlockPos pos2, ServerLevel world, BlockState blockState,ServerPlayer player,PlayerMapData PMD ) {
        int dx = pos2.getX() - pos1.getX();
        int dy = pos2.getY() - pos1.getY();
        int dz = pos2.getZ() - pos1.getZ();

        int steps = Math.max(Math.max(Math.abs(dx), Math.abs(dy)), Math.abs(dz));
//        if(!PlaceBlock.canPlaceBlock(pos1,pos2,world, player, blockState,diagonal,player.hasPermissions(2), PMD)){
//            return false;
//        }

        //undo
        Map<BlockPos, BlockState> undoMap  = new HashMap<>();
        PMD.getUndoDataMap().push(undoMap);
        double t = 0.0;
        double increment = 0.01; // 定义参数t的增量
//        if(steps>100)increment = 1 / (steps/50);
        if(steps>100)increment = 1 / steps;

        while (t <= 1.0) { // 在参数范围内循环
            double x = pos1.getX() * (1 - t) + pos2.getX() * t;
            double y = Math.round(pos1.getY() * (1 - t) + pos2.getY() * t);
            double z = pos1.getZ() * (1 - t) + pos2.getZ() * t;

            BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
//            world.setBlock(blockPos, blockState,2);
            MySetBlock.setBlockNotUpdateAddUndo(world, blockPos, blockState,undoMap);

            t += increment; // 增加参数t
        }
        return true;
    }

}
