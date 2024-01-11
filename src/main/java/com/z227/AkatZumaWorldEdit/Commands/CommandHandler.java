package com.z227.AkatZumaWorldEdit.Commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.permission.PermissionAPI;

@Mod.EventBusSubscriber
public class CommandHandler {
    @SubscribeEvent
    public static void onServerStarting(RegisterCommandsEvent event) {



        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register(
                Commands.literal(AkatZumaWorldEdit.MODID).then(
                        Commands.literal("set")
                                .requires((commandSource) -> commandSource.hasPermission(1))

                                .executes(InfoCommand.instance)
                )
        );
        dispatcher.register(Commands.literal("a").redirect(cmd));


    }
}