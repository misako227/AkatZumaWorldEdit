package com.z227.AkatZumaWorldEdit.network;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.network.brushPacket.*;
import com.z227.AkatZumaWorldEdit.network.lineItemPacket.C2SPlaceCurvePacket;
import com.z227.AkatZumaWorldEdit.network.messagePacket.S2CInventoryNotEnough;
import com.z227.AkatZumaWorldEdit.network.posPacket.C2SPos1;
import com.z227.AkatZumaWorldEdit.network.posPacket.C2SPos2;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class NetworkingHandle {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
//    private static int ID = 0;
//    public static int nextID() {
//        return ID++;
//    }

    public static void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(AkatZumaWorldEdit.MODID, "network"),
        () -> VERSION,
        (version) -> version.equals(VERSION),
        (version) -> version.equals(VERSION)
        );
//        INSTANCE.messageBuilder(SendToServer.class, nextID(), NetworkDirection.PLAY_TO_SERVER)
//                .encoder(SendToServer::toBytes)
//                .decoder(SendToServer::new)
//                .consumerNetworkThread(SendToServer::handler)
//                .add();
//        INSTANCE.messageBuilder(SendToClient.class, nextID(), NetworkDirection.PLAY_TO_CLIENT)
//                .encoder(SendToClient::toBytes)
//                .decoder(SendToClient::new)
//                .consumerNetworkThread(SendToClient::handler)
//                .add();
        INSTANCE.registerMessage(1,SendToServer.class, SendToServer::toBytes, SendToServer::new,SendToServer::handler, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(2,SendToClient.class, SendToClient::toBytes, SendToClient::new,SendToClient::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

        INSTANCE.registerMessage(3,SendToClientCompoundTag.class, SendToClientCompoundTag::toBytes, SendToClientCompoundTag::new,SendToClientCompoundTag::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(4, S2CSphere.class, S2CSphere::toBytes, S2CSphere::new,S2CSphere::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(5, C2SUseBrush.class, C2SUseBrush::toBytes, C2SUseBrush::new,C2SUseBrush::handler, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(6, S2CMask.class, S2CMask::toBytes, S2CMask::new,S2CMask::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(7, S2CPasteBrush.class, S2CPasteBrush::toBytes, S2CPasteBrush::new, S2CPasteBrush::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(8, C2SPos1.class, C2SPos1::toBytes, C2SPos1::new,C2SPos1::handler, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(9, C2SPos2.class, C2SPos2::toBytes, C2SPos2::new,C2SPos2::handler, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        INSTANCE.registerMessage(10, S2CInventoryNotEnough.class, S2CInventoryNotEnough::toBytes, S2CInventoryNotEnough::new,S2CInventoryNotEnough::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(11, S2CUnbind.class, S2CUnbind::toBytes, S2CUnbind::new,S2CUnbind::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        INSTANCE.registerMessage(12, C2SPlaceCurvePacket.class, C2SPlaceCurvePacket::toBytes, C2SPlaceCurvePacket::new,C2SPlaceCurvePacket::handler, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    public static <MSG> void sendToClient(MSG message , ServerPlayer player)
    {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }



}