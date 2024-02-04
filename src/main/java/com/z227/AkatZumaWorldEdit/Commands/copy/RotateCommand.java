//package com.z227.AkatZumaWorldEdit.Commands.copy;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.arguments.FloatArgumentType;
//import com.mojang.brigadier.context.CommandContext;
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import net.minecraft.commands.CommandBuildContext;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//
//public class RotateCommand {
//    public static void  register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {
//
//        dispatcher.register(
//                Commands.literal(AkatZumaWorldEdit.MODID)
//                        .then(Commands.literal("rotate")
//                        .then(Commands.argument("y角度", FloatArgumentType.floatArg(-360, 360))
//                                .executes((context)->{
//                                    float yAngle = FloatArgumentType.getFloat(context,"y角度");
//                                    rotate(context,0,yAngle,0);
//                                    return 1;
//                                    }
//                                )
//                                .then(Commands.argument("x角度", FloatArgumentType.floatArg(-360, 360))
//                                        .executes((context)->{
//                                            float xAngle = FloatArgumentType.getFloat(context,"x角度");
//                                            float yAngle = FloatArgumentType.getFloat(context,"y角度");
//                                            rotate(context,xAngle,yAngle,0);
//                                            return 1;
//                                                }
//                                        )
//                                .then(Commands.literal("-h")
//                                                .executes((context)->{
//                                                    rotate(context,0,0,1);
//                                                    return 1;
//                                                })
//                                .then(Commands.argument("z角度", FloatArgumentType.floatArg(-360, 360))
//                                        .executes((context)->{
//                                            float xAngle = FloatArgumentType.getFloat(context,"x角度");
//                                            float yAngle = FloatArgumentType.getFloat(context,"y角度");
//                                            float zAngle  = FloatArgumentType.getFloat(context,"z角度");
//                                            rotate(context,xAngle,yAngle,zAngle);
//                                            return 1;
//                                            }
//                                        )
//                        ))))
//        ));
//
//    }
//
//    public static void rotate(CommandContext<CommandSourceStack> context, float xAngle, float yAngle, float zAngle) {
//        System.out.println(context.getCommand());
//        System.out.println(context.getInput());
//        System.out.println(context.getChild());
//    }
//}
