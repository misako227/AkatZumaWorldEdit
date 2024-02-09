//package com.z227.AkatZumaWorldEdit.Commands.admin;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.context.CommandContext;
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import com.z227.AkatZumaWorldEdit.akatArgumentType.AkatArgumentType;
//import net.minecraft.commands.CommandBuildContext;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraft.commands.arguments.item.ItemInput;
//
//public class AddWhiteListBlockCommand {
//    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {
//        dispatcher.register(
//                Commands.literal(AkatZumaWorldEdit.MODID)
//                        .then(Commands.literal("add")
//                                        .requires((commandSource) -> commandSource.hasPermission(2))
////                                        .requires((commandSource) -> commandSource.getPlayer()!=null)
//                                        .then(Commands.literal("value")
//                                                .then(Commands.argument("值", AkatArgumentType.block(pContext))
//                                                        .executes((context)->{
//                                                            add(context);
//                                                            return 1;
//                                                        })))
//
//                        )
//
//        );
//
//    }
//
//    public static void add(CommandContext<CommandSourceStack> context){
//        ItemInput blockInput =  AkatArgumentType.getItem(context, "值");
//        System.out.println(blockInput.toString());
//    }
//}
