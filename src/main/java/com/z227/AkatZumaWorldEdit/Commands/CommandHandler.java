package com.z227.AkatZumaWorldEdit.Commands;


import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommandHandler {
    @SubscribeEvent
    public static void onServerStarting(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandBuildContext commandbuildcontext = Commands.createValidationContext(VanillaRegistries.createLookup());

        SetCommand.register(dispatcher,commandbuildcontext);


//        }
    }






}

