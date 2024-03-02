package com.z227.AkatZumaWorldEdit.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SendToServer {
    private final int message;
//    private static final Logger LOGGER = LogManager.getLogger();

    public SendToServer(FriendlyByteBuf buffer) {
        message = buffer.readInt();
    }

    public SendToServer(int n) {
        this.message = n;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.message);
    }

    public boolean handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
//            LOGGER.info("服务端收到的数据：" + this.message);  //接收到数据以后，如何使用这些数据

        });
        ctx.get().setPacketHandled(true);
        return true;
    }


}