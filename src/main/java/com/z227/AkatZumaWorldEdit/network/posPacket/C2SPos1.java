package com.z227.AkatZumaWorldEdit.network.posPacket;

import com.z227.AkatZumaWorldEdit.Items.WoodAxeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SPos1 {
    BlockPos blockPos;


    public C2SPos1(FriendlyByteBuf buffer) {
        this.blockPos = buffer.readBlockPos();

    }

    public C2SPos1(BlockPos pos) {
        this.blockPos = pos;

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);


    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            WoodAxeItem.clickPos(ctx.get().getSender().level, blockPos, ctx.get().getSender(), true);
        });
        ctx.get().setPacketHandled(true);

    }


}
