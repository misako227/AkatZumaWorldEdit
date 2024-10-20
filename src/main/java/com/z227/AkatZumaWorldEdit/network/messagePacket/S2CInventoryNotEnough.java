package com.z227.AkatZumaWorldEdit.network.messagePacket;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CInventoryNotEnough {

    BlockState blockState;
    int num;    //需要的数量
    int sum;    //当前背包数量


    public S2CInventoryNotEnough(FriendlyByteBuf buffer) {
        this.blockState = buffer.readById(Block.BLOCK_STATE_REGISTRY);
        this.num = buffer.readVarInt();
        this.sum = buffer.readVarInt();
    }

    public S2CInventoryNotEnough(BlockState blockState, int num, int sum) {
        this.blockState = blockState;
        this.num = num;
        this.sum = sum;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeId(Block.BLOCK_STATE_REGISTRY, this.blockState);
        buf.writeVarInt(num);
        buf.writeVarInt(sum);

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(this::sendMessage);
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public void sendMessage() {
        Player player = Minecraft.getInstance().player;
        MutableComponent deBlockName = blockState.getBlock().getName();
        MutableComponent component = Component.translatable("chat.akatzuma.error.inventory_not_enough")
                .append(deBlockName).withStyle(ChatFormatting.GREEN).append(":" + num)
                .append(Component.translatable("chat.akatzuma.error.current_num"));
        AkatZumaWorldEdit.sendAkatMessage(component.append(":" + sum), player);
    }
}
