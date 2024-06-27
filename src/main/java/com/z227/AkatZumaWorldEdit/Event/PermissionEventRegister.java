//package com.z227.AkatZumaWorldEdit.Event;
//
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.server.permission.events.PermissionGatherEvent;
//import net.minecraftforge.server.permission.nodes.PermissionNode;
//import net.minecraftforge.server.permission.nodes.PermissionTypes;
//
//@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
//public class PermissionEventRegister {
//
//    public static final PermissionNode<Boolean> use_set = new PermissionNode<>(AkatZumaWorldEdit.MODID, "akat.use_set",
//            PermissionTypes.BOOLEAN, (player, uuid, context) -> player!=null && player.hasPermissions(2));
//    @SubscribeEvent
//    public static void permissionRegister(PermissionGatherEvent.Nodes event) {
//        event.addNodes(use_set);
//
//
//    }
//}
