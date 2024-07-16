package com.z227.AkatZumaWorldEdit.Commands.brush;

import com.z227.AkatZumaWorldEdit.Core.modifyBlock.CopyBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.ShapeBase;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BrushBase {
    ShapeBase shapeBase;

    CopyBlock copyBlock;
    String shape;
    boolean maskFlag;
    Map<BlockState, Boolean> maskMap;





    public BrushBase(ShapeBase shapeBase) {
        this.shapeBase = shapeBase;

        this.shapeBase.initMaskMap();
        syncShape();
    }

    public BrushBase(CopyBlock copyBlock) {
        this.copyBlock = copyBlock;
        this.copyBlock.initMaskMap();
        syncCopyBlock();
    }

    //给客户端使用
    public BrushBase() {

    }



    public ShapeBase getShapeBase() {
        return shapeBase;
    }

    public void setShapeBase(ShapeBase shapeBase) {
        this.shapeBase = shapeBase;
    }

    public Map<BlockState, Boolean> getMaskMap() {
        return maskMap;
    }

    public void putMaskMap(BlockState blockState) {
        this.maskMap.put(blockState, false);
    }

    public void initMaskMap() {
        this.maskMap = new HashMap<>();
    }

    public void syncShape(){
        this.shape = shapeBase.getShape();
        this.maskFlag = shapeBase.isMaskFlag();
        this.maskMap = shapeBase.getMaskMap();

    }
    public void syncCopyBlock(){
        this.shape = "paste";
        this.maskFlag = copyBlock.isMaskFlag();
        this.maskMap = copyBlock.getMaskMap();

    }

    public CopyBlock getCopyBlock() {
        return copyBlock;
    }

    public void setCopyBlock(CopyBlock copyBlock) {
        this.copyBlock = copyBlock;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public boolean isMaskFlag() {
        return maskFlag;
    }

    public void setMaskFlag(boolean maskFlag) {
        this.maskFlag = maskFlag;
    }

    public void setMaskMap(Map<BlockState, Boolean> maskMap) {
        this.maskMap = maskMap;
    }
}
