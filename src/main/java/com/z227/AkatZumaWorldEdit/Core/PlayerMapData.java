package com.z227.AkatZumaWorldEdit.Core;

import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.utilities.BoundedStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerMapData {
    private String name;
    private BlockPos pos1;
    private BlockPos pos2;

    private boolean flag;
    private BlockState queryBlockState;

    private CompoundTag invPosNBT;
    private Map<int[], BlockState> invPosMap;
    private int invPosIndex = 1;

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


    public PlayerMapData(String name) {
        this.name = name;
        this.flag = true;
        this.undoDataMap = new BoundedStack<>(Config.UNDOLimit.get());
        this.redoDataMap = new BoundedStack<>(Config.UNDOLimit.get());
        this.invPosNBT = new CompoundTag();
        this.invPosMap = new LinkedHashMap<>();
//        this.lineBase = new LineBase();
//        this.copyBlockMap = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

//    public boolean isVip() {
//        return vip;
//    }

//    public void setVip(boolean vip) {
//        this.vip = vip;
//    }

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

    //    public StackBlock getStackBlock() {
//        return stackBlock;
//    }
//
//    public void setStackBlock(StackBlock stackBlock) {
//        this.stackBlock = stackBlock;
//    }


    public CompoundTag getInvPosNBT() {
        return invPosNBT;
    }

    public void setInvPosNBT(CompoundTag invPosNBT) {
        this.invPosNBT = invPosNBT;
        if(!invPosNBT.isEmpty()){
            for (int i = 1; i <= invPosNBT.size(); ++i) {
                int[] ints =  invPosNBT.getIntArray("pos" + i);
                BlockState blockState = Blocks.AIR.defaultBlockState();;
                if(ints.length == 3) {
                    BlockPos pos = new BlockPos(ints[0], ints[1], ints[2]);
                    Level level = Minecraft.getInstance().level;
                    if (level !=null && level.hasChunkAt(pos)) {
                        blockState = level.getBlockState(pos);
                    }
                }
                this.invPosMap.put(ints, blockState);
            }
        }
    }

    public Map<int[], BlockState> getInvPosMap() {
        return invPosMap;
    }

    public int getInvPosIndex() {
        return invPosIndex;
    }

    public void setInvPosIndex(int invPosIndex) {
        this.invPosIndex = invPosIndex;
    }
    public void addInvPosIndex() {
        this.invPosIndex++;
        if (this.invPosIndex > 5)this.invPosIndex=1;
    }
    public void deInvPosIndex() {
        this.invPosIndex--;
        if (this.invPosIndex < 1)this.invPosIndex=5;

    }
}
