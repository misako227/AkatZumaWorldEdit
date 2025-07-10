package com.z227.AkatZumaWorldEdit.Commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.MySetBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.UndoData;
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

import java.util.ArrayList;
import java.util.List;

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

            if(PlaceBlock.canPlaceBlock(pos1,pos2,serverLevel,player,blockState,num, player.hasPermissions(2))){
//                if(drawLine(pos1, pos2, serverLevel, blockState, player, PMD)){
//                    SendCopyMessage.sendSuccessMsg(blockState,player, context.getInput());
//                }
                UndoData undoMap  = new UndoData(serverLevel);
                PMD.getUndoDataMap().push(undoMap);
                List<BlockPos> posList = drawLine(pos1, pos2);
                for (BlockPos pos : posList) {
                    MySetBlock.setBlockAddUndo(serverLevel, pos, blockState,player, undoMap);
                }
            }
        }


        PMD.setFlag(true);


    }


    public static List drawLine(BlockPos pos1, BlockPos pos2){
        List<BlockPos> points = new ArrayList<>();
        // 计算方向向量
        int x1 = pos1.getX(), y1 = pos1.getY(), z1 = pos1.getZ();
        int x2 = pos2.getX(), y2 = pos2.getY(), z2 = pos2.getZ();


        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int dz = Math.abs(z2 - z1);

        // 确定步数，选择dx, dy, dz中的最大值
        int steps = Math.max(dx, Math.max(dy, dz));

        // 确定增量的方向
        int x_dir = x1 < x2 ? 1 : -1;
        int y_dir = y1 < y2 ? 1 : -1;
        int z_dir = z1 < z2 ? 1 : -1;

        // 计算每一步在每个轴上的增量
        double x_inc = (double) dx / steps;
        double y_inc = (double) dy / steps;
        double z_inc = (double) dz / steps;

        // 初始化起始点
        double x = x1;
        double y = y1;
        double z = z1;

        // 生成并添加点到列表中
        for (int i = 0; i <= steps; i++) {
            points.add(new BlockPos((int) Math.round(x), (int) Math.round(y), (int) Math.round(z)));
            x += x_inc * x_dir;
            y += y_inc * y_dir;
            z += z_inc * z_dir;
        }

        return points;
    }

}
