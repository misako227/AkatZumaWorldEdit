package com.z227.AkatZumaWorldEdit.Core.modifyBlock;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class ReplaceBlock {
    BlockPos pos1,pos2;
    ServerLevel world;
    Player player;
    PlayerMapData PMD;
    BlockState inputState,outputState;
    boolean permissionLevel;
    Map<BlockPos, BlockState> replaceMap;


    public ReplaceBlock(PlayerMapData PMD, ServerLevel level, Player player, BlockState inputState, BlockState outputState) {
        this.player = player;
        this.PMD = PMD;
        this.permissionLevel = player.hasPermissions(2);;
        this.world = level;
        this.pos1 = this.PMD.getPos1();
        this.pos2 = this.PMD.getPos2();
        this.inputState =  inputState;
        this.outputState = outputState;
        this.replaceMap = new HashMap<>();
    }

    public boolean init(){
        if(PlaceBlock.checkPos(this.pos1,this.pos2,this.player,this.PMD)){
            if(checkReplaceBlock()){
                addToReplaceMap();
                if(this.replaceMap.size()>0){
                    return PlaceBlock.canPlaceBlock(this.pos1, this.pos2, this.world, this.player, this.outputState,this.replaceMap.size(), this.permissionLevel, this.PMD);
                }
            }

        }
        return false;
    }

    public boolean checkReplaceBlock(){
        if(!this.permissionLevel){
            Map<String, Integer> blackWhiteMap = AkatZumaWorldEdit.defaultBlockMap;    //黑白名单方块
            if (PlaceBlock.checkVip(this.player)) {
                blackWhiteMap = AkatZumaWorldEdit.VipBlockMap;    //黑白名单方块
            }
            String blockName = BlockStateString.getBlockName(this.inputState);
            int n = PlaceBlock.getLimit(blockName, blackWhiteMap);  //比例值
            MutableComponent deBlockName = this.inputState.getBlock().getName();
            //检查黑名单
            return PlaceBlock.checkBlackList(player, n, deBlockName);
        }return  true;
    }


    public void addToReplaceMap(){
        for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++) {
            for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++) {
                for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    if (state == this.inputState || state.getBlock().defaultBlockState() == this.inputState){
                        this.replaceMap.put(pos, state);
                    }
                }
            }
        }
        if(this.replaceMap.size() == 0){
            AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.error.no_replace"),this.player);
        }
    }


    public void replace(Map<BlockPos,BlockState> undoMap){
        for (Map.Entry<BlockPos, BlockState> entry : this.replaceMap.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();
//            undoMap.put(pos, state);
//            this.world.setBlock(pos, this.outputState, 2);
            MySetBlock.setBlockNotUpdateAddUndo(this.world, pos ,this.outputState, undoMap);
        }
//        MutableComponent blockName = this.outputState.getBlock().getName().withStyle(ChatFormatting.GREEN);
//        Component setSuccess = Component.translatable("chat.akatzuma.set.success").append(blockName).append(Component.translatable("chat.akatzuma.undo.tip"));
//        AkatZumaWorldEdit.sendAkatMessage(setSuccess, player);

    }


}
