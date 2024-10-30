package com.z227.AkatZumaWorldEdit.Commands;


import com.mojang.brigadier.CommandDispatcher;
import com.z227.AkatZumaWorldEdit.Commands.admin.AddVipPlayerCommand;
import com.z227.AkatZumaWorldEdit.Commands.admin.DelVipPlayerCommand;
import com.z227.AkatZumaWorldEdit.Commands.brush.BrushCommand;
import com.z227.AkatZumaWorldEdit.Commands.brush.MaskCommand;
import com.z227.AkatZumaWorldEdit.Commands.copy.CopyCommand;
import com.z227.AkatZumaWorldEdit.Commands.copy.FlipCommand;
import com.z227.AkatZumaWorldEdit.Commands.copy.PasteCommand;
import com.z227.AkatZumaWorldEdit.Commands.otherCommand.BindInvPosCommand;
import com.z227.AkatZumaWorldEdit.Commands.shape.CylinderCommand;
import com.z227.AkatZumaWorldEdit.Commands.shape.EllipseCommand;
import com.z227.AkatZumaWorldEdit.Commands.shape.HollowCylinderCommand;
import com.z227.AkatZumaWorldEdit.Commands.shape.SphereCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommandHandler {
    @SubscribeEvent
    public static void onServerStarting(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
//        CommandBuildContext commandbuildcontext = Commands.createValidationContext(VanillaRegistries.createLookup());
//        CommandBuildContext commandbuildcontext = new CommandBuildContext(RegistryAccess.BUILTIN.get());

        SetCommand.register(dispatcher);
        CopyCommand.register(dispatcher);
        PasteCommand.register(dispatcher);
        FlipCommand.register(dispatcher);
//        RotateCommand.register(dispatcher);

        UndoCommand.register(dispatcher);
        RedoCommand.register(dispatcher);
        StackCommand.register(dispatcher);
        ReplaceCommand.register(dispatcher);
        CylinderCommand.register(dispatcher);
        HollowCylinderCommand.register(dispatcher);
        SphereCommand.register(dispatcher);
        EllipseCommand.register(dispatcher);
        PosCommand.register(dispatcher);
        LineCommand.register(dispatcher);
        BindInvPosCommand.register(dispatcher);
        BrushCommand.register(dispatcher);
        MaskCommand.register(dispatcher);

        AddVipPlayerCommand.register(dispatcher);
        DelVipPlayerCommand.register(dispatcher);


    }







}

