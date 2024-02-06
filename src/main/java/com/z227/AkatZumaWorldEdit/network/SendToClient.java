package com.z227.AkatZumaWorldEdit.network;

import com.z227.AkatZumaWorldEdit.network.clientPacket.ClientPacketHandle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SendToClient {
    private final int message;


    public SendToClient(FriendlyByteBuf buffer) {
        message = buffer.readInt();
    }

    public SendToClient(int message) {
        this.message = message;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.message);

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPacketHandle.handle(this.message);
        });
        ctx.get().setPacketHandled(true);
    }


}