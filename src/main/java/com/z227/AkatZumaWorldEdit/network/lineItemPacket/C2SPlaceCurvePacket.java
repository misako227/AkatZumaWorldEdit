package com.z227.AkatZumaWorldEdit.network.lineItemPacket;

import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.MySetBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.LineBase;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class C2SPlaceCurvePacket {
    BlockState blockState;

    List<BlockPos> posList;

    public C2SPlaceCurvePacket(FriendlyByteBuf buffer) {
        this.blockState = buffer.readById(Block.BLOCK_STATE_REGISTRY);

        this.posList = Arrays.stream(buffer.readLongArray()).mapToObj(BlockPos::of).toList();
    }

    public C2SPlaceCurvePacket(BlockState blockState,List<BlockPos> posList) {
        this.blockState = blockState;

        this.posList = posList;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeId(Block.BLOCK_STATE_REGISTRY, this.blockState);

        buf.writeLongArray(posList.stream().mapToLong(BlockPos::asLong).toArray());


    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(()->{
            PlaceCurve(ctx.get().getSender());
        });
        ctx.get().setPacketHandled(true);
    }

    public void PlaceCurve(Player player) {
        ServerLevel level = (ServerLevel) player.getLevel();
        LineBase lineBase = new LineBase(2,posList);
        List<BlockPos> curvePosList = lineBase.getCurvePosList();

        if(PlaceBlock.canPlaceBlockList(curvePosList, level, player, blockState, curvePosList.size(), player.hasPermissions(2))){
            PlayerMapData PMD = Util.getPMD(player);
            Map<BlockPos, BlockState> undoMap  = new HashMap<>();
            PMD.getUndoDataMap().push(undoMap);
            for(BlockPos pos : curvePosList){
                MySetBlock.setBlockNotUpdateAddUndo(level, pos,blockState, undoMap);
            }


            SendCopyMessage.sendSuccessMsg(blockState,player, "curve_item");

            PMD.setFlag(true);
        }

    }


}
