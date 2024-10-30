package com.z227.AkatZumaWorldEdit.network.brushPacket;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Commands.brush.BrushBase;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CMask{

    BlockState blockState;
    boolean maskFlag;

    public S2CMask(FriendlyByteBuf buffer) {
        this.blockState = Block.BLOCK_STATE_REGISTRY.byId(buffer.readVarInt());
        this.maskFlag = buffer.readBoolean();

    }

    public S2CMask(BlockState blockState, boolean maskFlag) {
        this.blockState = blockState;
        this.maskFlag = maskFlag;

    }

    public void toBytes(FriendlyByteBuf buf) {

        buf.writeVarInt(Block.BLOCK_STATE_REGISTRY.getId(this.blockState));
        buf.writeBoolean(maskFlag);

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(this::mask);
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public void mask(){
        Player player = Minecraft.getInstance().player;
        PlayerMapData PMD = Util.getPMD(player);
        if(!Minecraft.getInstance().isLocalServer()){
            Item item = player.getMainHandItem().getItem();
            BrushBase BrushBase =  PMD.getBrushMap().get(item);

            BrushBase.putMaskMap(blockState);
            BrushBase.setMaskFlag(maskFlag);

        }

        Component blockName = blockState.getBlock().getName();

        AkatZumaWorldEdit.sendAkatMessage(new TextComponent("")
                .append(new TranslatableComponent("chat.akatzuma.success.add_viplayer")).withStyle(ChatFormatting.GREEN)
                .append(blockName), player);
    }
}
