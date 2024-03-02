package com.z227.AkatZumaWorldEdit.network;


import com.z227.AkatZumaWorldEdit.network.clientPacket.ClientPacketHandle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SendToClientCompoundTag {
    private final CompoundTag tag;


    public SendToClientCompoundTag(FriendlyByteBuf buffer) {
        this.tag = buffer.readNbt();
    }

    public SendToClientCompoundTag(CompoundTag t) {
        this.tag = t;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPacketHandle.handle(this.tag);
        });
        ctx.get().setPacketHandled(true);
    }
}
