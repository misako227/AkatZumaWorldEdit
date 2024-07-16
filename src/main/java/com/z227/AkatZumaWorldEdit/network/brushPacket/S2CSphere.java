package com.z227.AkatZumaWorldEdit.network.brushPacket;

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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CSphere{
    int radius, radiusZ,height;
    boolean hollow;
    BlockState blockState;

    public S2CSphere(FriendlyByteBuf buffer) {
        this.blockState = buffer.readById(Block.BLOCK_STATE_REGISTRY);
        this.radius = buffer.readInt();
        this.radiusZ = buffer.readInt();
        this.height = buffer.readInt();
        this.hollow = buffer.readBoolean();

    }

    public S2CSphere(BlockState blockState,int radius,int radiusZ,int height, boolean hollow) {
        this.blockState = blockState;
        this.radius = radius;
        this.radiusZ = radiusZ;
        this.height = height;
        this.hollow = hollow;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeId(Block.BLOCK_STATE_REGISTRY, this.blockState);
        buf.writeInt(this.radius);
        buf.writeInt(this.radiusZ);
        buf.writeInt(this.height);
        buf.writeBoolean(this.hollow);

    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(this::sphere);
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public void sphere(){
        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        if(!Minecraft.getInstance().isLocalServer()){
            PlayerMapData PMD = Util.getPMD(player);
            String shape = "sphere";
            if (this.height >0) shape = "cyl";
            if (this.radiusZ >0) shape = "ellipse";

            ShapeBase shapeBase = new ShapeBase(PMD,level,player,blockState,radius,radiusZ, height,hollow, shape);
            Item item = player.getMainHandItem().getItem();
            BrushBase brushBase = new BrushBase(shapeBase);
            PMD.getBrushMap().put(item, brushBase);
        }




        AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.success.bind_pos"), player);
    }
}
