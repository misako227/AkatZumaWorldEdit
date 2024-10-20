package com.z227.AkatZumaWorldEdit.network.brushPacket;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Commands.brush.BrushBase;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class S2CUnbind {
    public S2CUnbind(FriendlyByteBuf buffer) {


    }

    public S2CUnbind() {


    }

    public void toBytes(FriendlyByteBuf buf) {



    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(this::unbind);
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private void unbind() {
        Player player = Minecraft.getInstance().player;
        PlayerMapData PMD = Util.getPMD(player);

        Item item = player.getMainHandItem().getItem();
        Map<Item, BrushBase> brushMap = PMD.getBrushMap();

        brushMap.remove(item);

        AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.success.del_viplayer"), player);

    }

}
