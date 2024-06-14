package com.z227.AkatZumaWorldEdit.Core.modifyBlock;


import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;
import org.joml.Vector4f;


public class RotateBlock {

    public static BlockPos rotateCyl(float xAngle,float yAngle,float zAngle, BlockPos pos, int xOrigin,int yOrigin,int zOrigin){
        if(xAngle == 0 && zAngle == 0)return pos;
        int vx = pos.getX() -  xOrigin;
//        int vy = pos.getY() -  yOrigin;
        int vz = pos.getZ() -  zOrigin;


        //todo 2024年6月14日 20:33:24  下面不能用
        Matrix4f translationMatrix =  new Matrix4f().rotate(xAngle, 1, 0, 0);
//        if(yAngle != 0)translationMatrix.rotate(yAngle, 0, 1, 0);
        if(zAngle != 0)translationMatrix.rotate(zAngle, 0, 0, 1);

//        Vector4f position = new Vector4f(vx,vy,vz,1);
        Vector4f position = new Vector4f(vx,1,vz,1);
        position = translationMatrix.transform(position);
        int tranX = (int)position.x + xOrigin;
        int tranY = (int)position.y + pos.getY();
//        int tranY = (int)position.y + yOrigin;
        int tranZ = (int)position.z + zOrigin;
        return new BlockPos(tranX, tranY, tranZ);
    }



//    public static BlockPos rotateCopyMapY(BlockPos playerCopyPos, BlockPos pos, BlockPos pos1,BlockPos pos2){
//        int playerCopyX = playerCopyPos.getX();
//        int playerCopyY = playerCopyPos.getY();
//        int playerCopyZ = playerCopyPos.getZ();
//
//        float centerX = (pos1.getX() + pos2.getX()) / 2f;
//        float centerY = (pos1.getY() + pos2.getY()) / 2f;
//        float centerZ = (pos1.getZ() + pos2.getZ()) / 2f;
//
//        float newX = pos.getX() + playerCopyX - centerX,
//              newY = pos.getY() + playerCopyY - centerY,
//              newZ = pos.getZ() + playerCopyZ - centerZ;
//
//
//        return new BlockPos((int)(-newZ+centerX-playerCopyX), (int)(newY+centerY-playerCopyY), (int)(newX+centerZ-playerCopyZ));
//
//    }
//
//    public static BlockPos rotateCopyMapZ(BlockPos playerCopyPos, BlockPos pos, BlockPos pos1,BlockPos pos2){
//        int playerCopyX = playerCopyPos.getX();
//        int playerCopyY = playerCopyPos.getY();
//        int playerCopyZ = playerCopyPos.getZ();
//
//        float centerX = (pos1.getX() + pos2.getX()) / 2f;
//        float centerY = (pos1.getY() + pos2.getY()) / 2f;
//        float centerZ = (pos1.getZ() + pos2.getZ()) / 2f;
//
//        float newX = pos.getX() + playerCopyX - centerX,
//                newY = pos.getY() + playerCopyY - centerY,
//                newZ = pos.getZ() + playerCopyZ - centerZ;
//
//
//        return new BlockPos((int)(-newY+centerX-playerCopyX), (int)(newX+centerY-playerCopyY), (int)(newZ+centerZ-playerCopyZ));
//
//    }

}
