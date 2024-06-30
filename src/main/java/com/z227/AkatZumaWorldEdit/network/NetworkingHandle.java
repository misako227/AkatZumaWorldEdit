package com.z227.AkatZumaWorldEdit.network;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
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
        INSTANCE.registerMessage(4,S2CSphere.class, S2CSphere::toBytes, S2CSphere::new,S2CSphere::handler, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

    }

    public static <MSG> void sendToClient(MSG message , ServerPlayer player)
    {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }


}