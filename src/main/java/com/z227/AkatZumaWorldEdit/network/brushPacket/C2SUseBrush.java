package com.z227.AkatZumaWorldEdit.network.brushPacket;

import com.z227.AkatZumaWorldEdit.Commands.brush.BrushBase;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.ShapeBase;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class C2SUseBrush {
    BlockPos blockPos;


    public C2SUseBrush(FriendlyByteBuf buffer) {
        this.blockPos = buffer.readBlockPos();

    }

    public C2SUseBrush(BlockPos pos) {
        this.blockPos = pos;

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);


    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            useBrush(ctx.get().getSender(), blockPos);
        });
        ctx.get().setPacketHandled(true);

    }


    public void useBrush(ServerPlayer player, BlockPos pos){

        PlayerMapData PMD = Util.getPMD(player);
//        ShapeBase shapeBase = new ShapeBase(PMD,level,player,blockState,radius, 0,hollow, "sphere");
        Item item = player.getMainHandItem().getItem();
//        ShapeBase shapeBase =  .getShapeBase();
        BrushBase brushBase = PMD.getBrushMap().get(item);

        String shape = brushBase.getShape();
        if(shape.equals("paste")){
            ServerLevel serverlevel = (ServerLevel) player.getLevel();
            CopyBlock copyBlock =  brushBase.getCopyBlock();
            // 设置标志位
            if(!PlaceBlock.cheakFlag(PMD,player))return ;
            PMD.setFlag(false);
            if(!PlaceBlock.cheakLevel(serverlevel,player))return; //世界黑名单

            copyBlock.setPlayerPastePos(pos);//粘帖时位置
            copyBlock.setPasteVec3(player.getDirection().getNormal());//粘帖时朝向

            //undo
            Map<BlockPos, BlockState> undoMap  = new HashMap<>();

            copyBlock.pasteBlock(serverlevel, undoMap, copyBlock.isAir());


        }else {
            ShapeBase shapeBase = brushBase.getShapeBase();
            shapeBase.setPlayerPos(pos);
            if(shapeBase.init()){
                switch (shape){
                    case "sphere" -> brushBase.getShapeBase().sphere();
                    case "cyl" -> brushBase.getShapeBase().cyl();
                    case "ellipse" -> brushBase.getShapeBase().ellipse();
                }
            }

        }
        PMD.setFlag(true);
    }
}
