package com.z227.AkatZumaWorldEdit.Commands.brush;

import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.ShapeBase;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrushBase {
    ShapeBase shapeBase;
    List shapeCenterPointList;
    Map<BlockPos,Byte> shapePosMap;



    public BrushBase(ShapeBase shapeBase) {
        this.shapeBase = shapeBase;
        this.shapeCenterPointList = new ArrayList<>();
        this.shapePosMap = new HashMap();
    }

    public ShapeBase getShapeBase() {
        return shapeBase;
    }

    public void setShapeBase(ShapeBase shapeBase) {
        this.shapeBase = shapeBase;
    }

    public List getShapeCenterPointList() {
        return shapeCenterPointList;
    }

    public void setShapeCenterPointList(List shapeCenterPointList) {
        this.shapeCenterPointList = shapeCenterPointList;
    }

    public Map<BlockPos, Byte> getShapePosMap() {
        return shapePosMap;
    }

    public void setShapePosMap(Map<BlockPos, Byte> shapePosMap) {
        this.shapePosMap = shapePosMap;
    }
}
