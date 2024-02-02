package com.z227.AkatZumaWorldEdit.Commands;


import com.mojang.brigadier.CommandDispatcher;
import com.z227.AkatZumaWorldEdit.Commands.admin.AddVipPlayerCommand;
import com.z227.AkatZumaWorldEdit.Commands.admin.DelVipPlayerCommand;
import com.z227.AkatZumaWorldEdit.Commands.copy.CopyCommand;
import com.z227.AkatZumaWorldEdit.Commands.copy.FlipCommand;
import com.z227.AkatZumaWorldEdit.Commands.copy.PasteCommand;
import com.z227.AkatZumaWorldEdit.Commands.shape.CylinderCommand;
import com.z227.AkatZumaWorldEdit.Commands.shape.SphereCommand;
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
        CopyCommand.register(dispatcher,commandbuildcontext);
        PasteCommand.register(dispatcher,commandbuildcontext);
        FlipCommand.register(dispatcher,commandbuildcontext);
        UndoCommand.register(dispatcher,commandbuildcontext);
        RedoCommand.register(dispatcher,commandbuildcontext);
        StackCommand.register(dispatcher,commandbuildcontext);
        ReplaceCommand.register(dispatcher,commandbuildcontext);
        CylinderCommand.register(dispatcher,commandbuildcontext);
        SphereCommand.register(dispatcher,commandbuildcontext);

        AddVipPlayerCommand.register(dispatcher,commandbuildcontext);
        DelVipPlayerCommand.register(dispatcher,commandbuildcontext);

    }

//    //client 命令
//    @SubscribeEvent
//    public static void onServerStarting(RegisterClientCommandsEvent event) {
//        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
//        CommandBuildContext commandbuildcontext = Commands.createValidationContext(VanillaRegistries.createLookup());
//
////        SetClientCommand.register(dispatcher,commandbuildcontext);
//
//
//    }






}

