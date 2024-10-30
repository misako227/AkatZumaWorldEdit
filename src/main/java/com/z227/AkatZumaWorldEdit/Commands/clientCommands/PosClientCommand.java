//package com.z227.AkatZumaWorldEdit.Commands.clientCommands;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.context.CommandContext;
//import com.mojang.brigadier.tree.LiteralCommandNode;
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
//import net.minecraft.commands.CommandBuildContext;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.player.Player;
//
//public class PosClientCommand
//{
//    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
//
//        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
//                Commands.literal(AkatZumaWorldEdit.MODID)
//                        .then(Commands.literal("pos1").executes((context) -> {
//                            pos(context,true);
//                            return 1;
//                        }))
//                        .then(Commands.literal("pos2").executes((context) -> {
//                            pos(context,false);
//                            return 1;
//                        }))
//
//
//
//        );
//        dispatcher.register(Commands.literal("aa").redirect(cmd));
//    }
//
//    private static void pos(CommandContext<CommandSourceStack> context,boolean b) {
//
//        Player player =  context.getSource().getPlayer();
//        BlockPos pos = player.getOnPos();
//        PlayerMapData PMD =  AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
//
//        if(b)PMD.setPos1(pos);
//        else PMD.setPos2(pos);
//
//    }
//
//}
