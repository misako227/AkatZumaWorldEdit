package com.z227.AkatZumaWorldEdit.Core;

import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.utilities.BoundedStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class PlayerMapData {
    private String name;
    private BlockPos pos1;
    private BlockPos pos2;
//    private boolean vip;
    private boolean flag;
    private BlockState queryBlockState;

    private BoundedStack<Map<BlockPos,BlockState>> undoDataMap;
    private BoundedStack<Map<BlockPos,BlockState>> redoDataMap;

    private CopyBlock copyBlock;
    private CopyBlock copyBlockClient;
//    private StackBlock stackBlock;


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
}
