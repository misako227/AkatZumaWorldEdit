//package com.z227.AkatZumaWorldEdit.Commands.admin;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.mojang.brigadier.arguments.StringArgumentType;
//import com.mojang.brigadier.context.CommandContext;
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import com.z227.AkatZumaWorldEdit.ConfigFile.schematic.Schematic;
//import net.minecraft.commands.CommandBuildContext;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//
//public class LoadSchematicCommand {
//    static String fileName = Component.translatable("chat.command.file_name").getString();
//    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {
//
//        dispatcher.register(
//                Commands.literal(AkatZumaWorldEdit.MODID)
//                        .then(Commands.literal("load")
//                                .requires((commandSource) -> commandSource.hasPermission(2))
//                                .then(Commands.argument(fileName, StringArgumentType.string())
//                                .executes((context) -> {
//                                    loadSchematic(context);
//                                    return 1;
//                                }))
//        ));
//    }
//
//    public static void loadSchematic(CommandContext<CommandSourceStack> context) {
//        Player player = context.getSource().getPlayer();
//        Level level = player.level();
//        String schematicName = StringArgumentType.getString(context, fileName);
//        System.out.println(schematicName);
//        Schematic schematic = new Schematic("test4", player);
//
//
////        AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.get_world_name").append(copyMessage), player);
//
//    }
//}
