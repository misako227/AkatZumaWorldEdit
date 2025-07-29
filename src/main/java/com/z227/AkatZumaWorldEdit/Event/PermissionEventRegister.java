
package com.z227.AkatZumaWorldEdit.Event;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.permission.events.PermissionGatherEvent;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import net.minecraftforge.server.permission.nodes.PermissionTypes;
//
@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PermissionEventRegister {

    public static final PermissionNode<Boolean> Check_Inv = new PermissionNode<>(AkatZumaWorldEdit.MODID, "check_inv",
            PermissionTypes.BOOLEAN, ((player, playerUUID, context) -> player != null && player.hasPermissions(2)));

    public static final PermissionNode<Boolean> Check_World = new PermissionNode<>(AkatZumaWorldEdit.MODID, "check_world",
            PermissionTypes.BOOLEAN, ((player, playerUUID, context) -> player != null && player.hasPermissions(2)));

    public static final PermissionNode<Boolean> Check_Blacklist_Block = new PermissionNode<>(AkatZumaWorldEdit.MODID, "check_blacklist_block",
            PermissionTypes.BOOLEAN, ((player, playerUUID, context) -> player != null && player.hasPermissions(2)));

    public static final PermissionNode<Boolean> Check_Area = new PermissionNode<>(AkatZumaWorldEdit.MODID, "check_area",
            PermissionTypes.BOOLEAN, ((player, playerUUID, context) -> player != null && player.hasPermissions(2)));
    @SubscribeEvent
    public static void permissionRegister(PermissionGatherEvent.Nodes event) {
        event.addNodes(Check_Inv);
    }
}
