//package com.z227.AkatZumaWorldEdit.Event;
//
//import com.mojang.brigadier.CommandDispatcher;
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import net.minecraft.commands.CommandBuildContext;
//import net.minecraft.commands.CommandSourceStack;
//import net.minecraft.commands.Commands;
//import net.minecraft.data.registries.VanillaRegistries;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.client.event.RegisterClientCommandsEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
//@OnlyIn(Dist.CLIENT)
//public class ClientEventRegister {
//
//    //client 命令
//    @SubscribeEvent
//    public static void onServerStarting(RegisterClientCommandsEvent event) {
//        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
//        CommandBuildContext commandbuildcontext = Commands.createValidationContext(VanillaRegistries.createLookup());
//
////        PosClientCommand.register(dispatcher,commandbuildcontext);
//
//    }
//
//}
