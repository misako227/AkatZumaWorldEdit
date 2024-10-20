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

import java.util.function.Supplier;

public class S2CPasteBrush {



    public S2CPasteBrush(FriendlyByteBuf buffer) {


    }

    public S2CPasteBrush() {


    }

    public void toBytes(FriendlyByteBuf buf) {



    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(this::sphere);
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public void sphere(){
        Player player = Minecraft.getInstance().player;
        if(!Minecraft.getInstance().isLocalServer()){
            PlayerMapData PMD = Util.getPMD(player);

//            CopyBlock copyBlock =  PMD.getCopyBlock();
            Item item = player.getMainHandItem().getItem();
            BrushBase brushBase = new BrushBase();
            brushBase.setShape("paste");
            brushBase.initMaskMap();
            PMD.getBrushMap().put(item, brushBase);
        }




        AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.success.bind_pos"), player);
    }
}
