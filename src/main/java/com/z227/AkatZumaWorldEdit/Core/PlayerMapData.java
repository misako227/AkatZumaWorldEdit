package com.z227.AkatZumaWorldEdit.Core;

import com.z227.AkatZumaWorldEdit.Commands.brush.BrushBase;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.utilities.BoundedStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class PlayerMapData {

    private BlockPos pos1;
    private BlockPos pos2;
    private BlockPos tempPos;
    private boolean isUpdatePos = false;

    private boolean flag;
    private BlockState queryBlockState;
    private BlockState replaceBlockState;
    private int queryFlag;

    private CompoundTag invPosNBT;
    private Map<String, BlockState> invPosMap;
    private BlockPos invPos;
    // 1 = curious，2 = sop backpack，3 = 玩家背包
    private byte sopBackpackFlag = 2;


    private BoundedStack<Map<BlockPos,BlockState>> undoDataMap;
    private BoundedStack<Map<BlockPos,BlockState>> redoDataMap;

    private CopyBlock copyBlock;
    private CopyBlock copyBlockClient;
//    private LineBase lineBase;


    public  BoundedStack<Map<BlockPos,BlockState>> getUndoDataMap() {
        return this.undoDataMap;
    }
    public  BoundedStack<Map<BlockPos,BlockState>> getRedoDataMap() {
        return this.redoDataMap;
    }

    public Map<Item, BrushBase> BrushMap;


    public PlayerMapData() {

        this.flag = true;
        this.undoDataMap = new BoundedStack<>(Config.UNDOLimit.get());
        this.redoDataMap = new BoundedStack<>(Config.UNDOLimit.get());
        this.invPosNBT = new CompoundTag();
        this.invPosMap = new HashMap<>();
        this.BrushMap = new HashMap<>();
        this.queryBlockState = Blocks.AIR.defaultBlockState();
        this.replaceBlockState = Blocks.AIR.defaultBlockState();
        this.queryFlag = 1;

    }

    public boolean isUpdatePos() {
        return isUpdatePos;
    }

    public void setUpdatePos(boolean updatePos) {
        isUpdatePos = updatePos;
    }

    public Map<Item, BrushBase> getBrushMap() {
        return BrushMap;
    }

    public BlockPos getTempPos() {
        return tempPos;
    }

    public void setTempPos(BlockPos tempPos) {
        this.tempPos = tempPos;
    }

    public int getQueryFlag() {
        return queryFlag;
    }

    public void setQueryFlag() {
        this.queryFlag = this.queryFlag == 1 ? 2 : 1;
    }

    public BlockState getReplaceBlockState() {
        return replaceBlockState;
    }

    public void setReplaceBlockState(BlockState replaceBlockState) {
        this.replaceBlockState = replaceBlockState;
    }

    public BlockPos getPos1() {
        return pos1;
    }

    public void setPos1(BlockPos pos1) {
        this.pos1 = pos1;
    }

    public BlockPos getPos2() {
        return pos2;
    }

    public void setPos2(BlockPos pos2) {
        this.pos2 = pos2;
    }


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public BlockState getQueryBlockState() {
        return queryBlockState;
    }

    public void setQueryBlockState(BlockState queryBlockState) {
        this.queryBlockState = queryBlockState;
    }

    public CopyBlock getCopyBlock() {
        return copyBlock;
    }

    public void setCopyBlock(CopyBlock copyBlock) {
        this.copyBlock = copyBlock;
    }

    public CopyBlock getCopyBlockClient() {
        return copyBlockClient;
    }

    public void setCopyBlockClient(CopyBlock copyBlockClient) {
        this.copyBlockClient = copyBlockClient;
    }

    public BlockPos getInvPos() {
        return invPos;
    }

    public void setInvPos(BlockPos invPos) {
        this.invPos = invPos;
    }

    public byte getSopBackpackFlag() {
        return sopBackpackFlag;
    }

    public void setSopBackpackFlag(byte sopBackpackLocation) {
        this.sopBackpackFlag = sopBackpackLocation;
    }

    public CompoundTag getInvPosNBT() {
        return invPosNBT;
    }

    public void setInvPosNBT(CompoundTag invPosNBT, Player player) {
        this.invPosNBT = invPosNBT;
        if(!this.invPosNBT.isEmpty()){
            int[] ints =  invPosNBT.getIntArray("pos");
            BlockState blockState = Blocks.AIR.defaultBlockState();;
            if(ints.length == 3) {
                BlockPos pos = new BlockPos(ints[0], ints[1], ints[2]);
                Level level = player.level();
                if (level !=null && level.hasChunkAt(pos)) {
                    blockState = level.getBlockState(pos);
                }
            }
            this.invPosMap.put("pos" , blockState);
//            for (int i = 1; i <= invPosNBT.size(); ++i) {
//                int[] ints =  invPosNBT.getIntArray("pos" + i);
//                BlockState blockState = Blocks.AIR.defaultBlockState();;
//                if(ints.length == 3) {
//                    BlockPos pos = new BlockPos(ints[0], ints[1], ints[2]);
//                    Level level = player.level();
//                    if (level !=null && level.hasChunkAt(pos)) {
//                        blockState = level.getBlockState(pos);
//                    }
//                }
//                this.invPosMap.put("pos" + i, blockState);
//            }


        }

    }

    public Map<String, BlockState> getInvPosMap() {
        return invPosMap;
    }

    public void setInvPosMap(BlockPos pos, Player player) {
        this.invPosNBT.putIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
        BlockState blockState = Blocks.AIR.defaultBlockState();
        Level level = player.level();
//        if (level.hasChunkAt(pos)) {
//            blockState = level.getBlockState(pos);
//        }
        this.invPosMap.put("pos", level.getBlockState(pos));
    }

    public void updateInvPosMap(Player player) {
        int[] intPos = invPosNBT.getIntArray("pos");
        if(intPos.length==3){
            BlockPos pos = new BlockPos(intPos[0],intPos[1],intPos[2]);
            Level level = player.level();
            this.invPosMap.put("pos", level.getBlockState(pos));
        }
    }


}
