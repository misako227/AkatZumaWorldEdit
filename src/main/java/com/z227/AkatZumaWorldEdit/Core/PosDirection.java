package com.z227.AkatZumaWorldEdit.Core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Rotation;

public class PosDirection {

    //根据方向计算角度
    public static double angle(Vec3i vector1, Vec3i vector2){
        // 计算点积
        int dotProduct = vector1.getX() * vector2.getX() +
                vector1.getY() * vector2.getY() +
                vector1.getZ() * vector2.getZ();

        double result = Math.acos(dotProduct); // 计算反余弦值
        double degrees = Math.toDegrees(result); // 转换为角度

        return degrees;
    }

    //计算是同方向还是背后
    public static Rotation calcRotationFromAngle(Vec3i copyVec3, Vec3i pasteVec3){
        double angle =  angle(copyVec3, pasteVec3);
        if ((int) angle == 180) {
            return Rotation.CLOCKWISE_180;
        }
        return Rotation.NONE;

    }

    //根据叉乘计算旋转的角度
    public static Rotation calcDirection(Vec3i copyVec3, Vec3i pasteVec3){
        Vec3i v3= copyVec3.cross(pasteVec3);//-1 right, 1 left
        switch (v3.getY()){
            case -1:
                return Rotation.CLOCKWISE_90;
            case  1:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return calcRotationFromAngle(copyVec3, pasteVec3);

        }
    }

    //计算第二个坐标在第一个坐标的方向
    public static BlockPos calcPosDirection(BlockPos currentPos, BlockPos nextPos){
        BlockPos result ;

        double yaw = calcPosYaw(currentPos, nextPos);

        if(yaw > -45 && yaw < 45){
            result = new BlockPos(1,0,0);

        }else if(yaw > 45 && yaw < 135){
            result = new BlockPos(0,0,1);

        }else if(yaw > -135 && yaw < -45){
            result = new BlockPos(0,0,-1);

        }else {
            result = new BlockPos(-1,0,0);

        }

        return  result;
    }

    public static Direction calcNextPosDirection(BlockPos currentPos, BlockPos nextPos){

        double yaw = calcPosYaw(currentPos, nextPos);
//        if(yaw > -45 && yaw < 45){
//            return Direction.EAST;      //south
//
//        }else if(yaw > 45 && yaw < 135){
//            return Direction.SOUTH;     //west
//
//        }else if(yaw > -135 && yaw < -45){
//            return Direction.NORTH;     //east
//
//        }

        if(yaw > -45 && yaw < 45){
            return Direction.SOUTH;      //south

        }else if(yaw > 45 && yaw < 135){
            return Direction.WEST;     //west

        }else if(yaw > -135 && yaw < -45){
            return Direction.EAST;     //east

        }



        return Direction.NORTH;      //north

    }

    //计算从当前位置到下一个位置的偏航角（Yaw）
    public static double calcPosYaw(BlockPos currentPos, BlockPos nextPos){
        BlockPos vector = new BlockPos(nextPos.getX() - currentPos.getX(),
                nextPos.getY() - currentPos.getY(),
                nextPos.getZ() - currentPos.getZ());

        return  Math.atan2(vector.getZ(), vector.getX()) * (180F /  Math.PI);

    }
}
