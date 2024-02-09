package com.z227.AkatZumaWorldEdit.Core.modifyBlock;

import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class RotateBlock {

    public static BlockPos rotateCyl(float xAngle,float zAngle, BlockPos pos, int xOrigin,int yOrigin,int zOrigin){
        int vx = pos.getX() -  xOrigin;
        int vz = pos.getZ() -  zOrigin;

        Matrix4f translationMatrix =  new Matrix4f().rotate(xAngle, 1, 0, 0);
        if(zAngle != 0)translationMatrix.rotate(zAngle, 0, 0, 1);

        Vector4f position = new Vector4f(vx,1,vz,1);
        position = translationMatrix.transform(position);
        int tranX = (int)position.x + xOrigin;
        int tranY = (int)position.y + pos.getY();
        int tranZ = (int)position.z + zOrigin;
        return new BlockPos(tranX, tranY, tranZ);
    }

//    public static BlockPos flipY(BlockPos pos){
//        Matrix4f translationMatrix =  new Matrix4f().scale(1.0f, -1.0f, 1.0f);;
////        matrixStack.scale(-1.0f, 1.0f, 1.0f); // 翻转x轴
////        matrixStack.scale(1.0f, -1.0f, 1.0f); // 翻转y轴
////        matrixStack.scale(1.0f, 1.0f, -1.0f); // 翻转z轴
//
//        Vector4f position = new Vector4f((Vector3fc) pos,1);
//        position = translationMatrix.transform(position);
//
//        return new BlockPos((int)position.x, (int)position.y, (int)position.z);
//    }

}
