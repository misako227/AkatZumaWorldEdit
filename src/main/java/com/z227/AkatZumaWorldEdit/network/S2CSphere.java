package com.z227.AkatZumaWorldEdit.network;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Commands.brush.BrushBase;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.ShapeBase;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CSphere{
    int radius;
    boolean hollow;
    BlockState blockState;

    public S2CSphere(FriendlyByteBuf buffer) {
        this.blockState = buffer.readById(Block.BLOCK_STATE_REGISTRY);
        this.radius = buffer.readInt();
        this.hollow = buffer.readBoolean();
    }

    public S2CSphere(BlockState blockState,int radius, boolean hollow) {
        this.blockState = blockState;
        this.radius = radius;
        this.hollow = hollow;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeId(Block.BLOCK_STATE_REGISTRY, this.blockState);
        buf.writeInt(this.radius);
        buf.writeBoolean(this.hollow);

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            sphere(blockState, radius, hollow);
        });
        ctx.get().setPacketHandled(true);
    }

    public void sphere(BlockState blockState,int radius, boolean hollow){
        System.out.println("radius:"+radius+"hollow:"+hollow);
        System.out.println("blockState:"+blockState);
        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        PlayerMapData PMD = Util.getPMD(player);
        ShapeBase shapeBase = new ShapeBase(PMD,level,player,blockState,radius, 0,hollow, "sphere");
        Item item = player.getMainHandItem().getItem();
        PMD.getBrushMap().put(item, new BrushBase(shapeBase));

        AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.success.bind_pos"), player);
    }
}
