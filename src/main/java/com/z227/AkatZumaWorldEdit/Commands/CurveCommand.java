//package com.z227.AkatZumaWorldEdit.Commands;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.context.CommandContext;
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
//import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
//import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
//import net.minecraft.commands.CommandBuildContext;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraft.commands.arguments.blocks.BlockInput;
//import net.minecraft.commands.arguments.blocks.BlockStateArgument;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Vec3i;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.level.block.state.BlockState;
//
//public class CurveCommand {
//
//    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
//
//        dispatcher.register(
//                Commands.literal(AkatZumaWorldEdit.MODID)
//                        .then(Commands.literal("curve")
//                            .then(Commands.argument("方块ID", BlockStateArgument.block())
//                                .executes((context) -> {
//                                    curve(context);
//                            return 1;
//                        })))
//
//
//        );
//    }
//
//    private static void curve(CommandContext<CommandSourceStack> context) {
//        ServerLevel serverLevel = context.getSource().getLevel();
//        ServerPlayer player = null;

//        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
//        BlockPos pos1 = PMD.getPos1();
//        BlockPos pos2 = PMD.getPos2();
//        BlockInput blockInput =  BlockStateArgument.getBlock(context, "方块ID");
//        BlockState blockState =  blockInput.getState();
//        boolean permission = player.hasPermissions(2);
//
//        if(PlaceBlock.checkPos(pos1, pos2, player, PMD)){
//            Vec3i vec3i = PlaceBlock.calculateCubeDimensions(pos1,pos2);
//            int num = (int) Math.sqrt(Math.pow(vec3i.getX(),2) + Math.pow(vec3i.getY(),2) + Math.pow(vec3i.getZ(),2));
//            if(PlaceBlock.canPlaceBlock(pos1, pos2, serverLevel, player, blockState, num, permission, PMD)){
//                if(drawLine(pos1, pos2, serverLevel, blockState, player,num, PMD)){
//                    SendCopyMessage.sendSuccessMsg(blockState,player);
//                }
//            }
//        }
//
//
//        PMD.setFlag(true);
//
//
//    }
//
//    public static boolean drawLine(BlockPos pos1, BlockPos pos2, ServerLevel world, BlockState blockState,ServerPlayer player, int num, PlayerMapData PMD ) {
//
//        double deltaX = pos2.getX() - pos1.getX();
//        double deltaY = pos2.getY() - pos1.getY();
//        double deltaZ = pos2.getZ() - pos1.getZ();
//
//
//        int  pos3x = (int) (pos1.getX()+deltaX);
//        int  pos3y = (int) (pos1.getY()+deltaY/2);
//        int  pos3z = pos1.getZ();
//        BlockPos pos3 = new BlockPos(pos3x, pos3y, pos3z);
//        double step = 0.05;
//        if(num>50)step = 1 / (num * 2);
//
//        for (double t = 0; t <= 1; t += step) {
//            BlockPos pointOnCurve = calculateBezierPoint(pos1, pos3, pos2, t);
//            world.setBlock(pointOnCurve, blockState,2);
//        }
//
//
//
//
//        return true;
//    }
//
//    public static BlockPos calculateBezierPoint(BlockPos p0, BlockPos p1, BlockPos p2, double t) {
//
//        double x = Math.pow(1 - t, 2) * p0.getX() + 2 * (1 - t) * t * p1.getX() + Math.pow(t, 2) * p2.getX();
//        double y = Math.pow(1 - t, 2) * p0.getY() + 2 * (1 - t) * t * p1.getY() + Math.pow(t, 2) * p2.getY();
//        double z = Math.pow(1 - t, 2) * p0.getZ() + 2 * (1 - t) * t * p1.getZ() + Math.pow(t, 2) * p2.getZ();
//        return new BlockPos((int)x, (int)y, (int)z);
//    }
//
//}
