package com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape;

import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.MySetBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapeBase {
    BlockPos pos1,pos2;
    ServerLevel world;
    Player player;
    PlayerMapData PMD;
    BlockState blockState;
    int radius;
    boolean permissionLevel,hollow;
    int height,blockNum;
    String shape;
    List<BlockPos> volList;
    BlockPos playerPos;
    Map<BlockPos, BlockState> undoMap;




    public ShapeBase(PlayerMapData PMD, ServerLevel level, Player player,BlockState blockState, int radius, int height, boolean hollow,String shape) {
        this.pos1 = PMD.getPos1();
        this.pos2 = PMD.getPos2();
        this.world = level;
        this.player = player;
        this.PMD = PMD;
        this.blockState = blockState;
        this.radius = radius;
        this.height = height;
        this.hollow = hollow;
        this.shape = shape;
        this.volList = new ArrayList<>();
        this.playerPos = new BlockPos(player.getBlockX(),player.getBlockY(),player.getBlockZ());
        this.permissionLevel = player.hasPermissions(2);

        this.blockNum = 0;


    }

    public boolean init(){
        if(checkPos(this.player,this.PMD)){

            if(PlaceBlock.canPlaceBlock(this.pos1,this.pos2,this.world,this.player,this.blockState,this.blockNum, this.permissionLevel,PMD)) {
                this.undoMap  = new HashMap<>();
                PMD.getUndoDataMap().push(undoMap);
                this.world.setBlock(this.pos1, Blocks.COBBLESTONE.defaultBlockState(), 2);
                this.world.setBlock(this.pos2, Blocks.COBBLESTONE.defaultBlockState(), 2);
                return true;
            }


        }
        return false;
    }


    public boolean checkPos(Player player, PlayerMapData PMD){
        if(!PlaceBlock.cheakFlag(PMD,player)){
            return false;
        }
        // 设置标志位
        PMD.setFlag(false);

        calcCylCubePos();

        return PlaceBlock.checkLowHeight(pos1, pos2, player);
    }

    public void cyl(){
        int xOrigin = this.playerPos.getX();
        int yOrigin = this.playerPos.getY();
        int zOrigin = this.playerPos.getZ();

        double step = 180.0 / (Math.PI * radius);
        for (int i = 0; i < this.height; i++) {
            if (this.radius > 50 && !hollow) {
                for (int x = -radius; x <= radius; x += 1) {
                    for (int z = -radius; z <= radius; z += 1) {
                        if (x * x + z * z <= radius * radius) {
                            BlockPos pos = new BlockPos(x + xOrigin, yOrigin+i, z + zOrigin);
                            MySetBlock.shapeSetBlock(this.world, this.player, pos, this.blockState, 2, this.undoMap);
                        }
                    }
                }
                player.teleportTo(xOrigin,yOrigin+this.height,zOrigin);
            } else {
                for (double x = 0; x < 360; x += step) {
                    calcCylPos(xOrigin, yOrigin+i, zOrigin, x, this.radius);
                    if (!hollow) {
                        // 设置圆心到当前点之间的连线上的点，以形成实心圆
                        for (double j = 0; j <= radius; j+=0.5) {
                            calcCylPos(xOrigin, yOrigin+i, zOrigin, x, j);
                        }
                        player.teleportTo(xOrigin,yOrigin+this.height,zOrigin);
                    }
                }
            }

        }


    }

    public void calcCylPos(int xOrigin, int yOrigin, int zOrigin, double x, int radius) {
        calcCyl(xOrigin, yOrigin, zOrigin, x, radius);
    }

    public void calcCylPos(int xOrigin, int yOrigin, int zOrigin, double x, double radius) {
        calcCyl(xOrigin, yOrigin, zOrigin, x, radius);
    }

    public void calcCyl(int xOrigin, int yOrigin, int zOrigin, double x, double radius) {
        double tx = xOrigin + radius * Math.cos(Math.toRadians(x)); // 计算x坐标
        double tz = zOrigin + radius * Math.sin(Math.toRadians(x)); // 计算y坐标

        int clyX = Math.toIntExact(Math.round(tx));
        int clyZ = Math.toIntExact(Math.round(tz));

        BlockPos bp = new BlockPos(clyX, yOrigin, clyZ);
//        BlockPos bp = new BlockPos(tranX,tranY,tranZ);
        MySetBlock.shapeSetBlock(this.world, this.player, bp, this.blockState, 2, this.undoMap);
    }

    //计算圆柱体所在方形的坐标
    public void calcCylCubePos(){
        switch (this.shape){
            case "cyl" -> {
                this.pos1 = this.playerPos.offset(this.radius,0,this.radius);
                this.pos2 = this.playerPos.offset(-this.radius,this.height-1,-this.radius);
            }
            case "sphere"->{
                this.pos1 = this.playerPos.offset(this.radius-1,-this.radius+1,this.radius-1);
                this.pos2 = this.playerPos.offset(-this.radius+1,this.radius-1,-this.radius+1);
            }
        }

        if(this.hollow){
            switch (this.shape){
                case "cyl" -> this.blockNum = (int) (Math.PI * this.radius * 2);
                case "sphere" -> this.blockNum = (int) (4 * Math.PI * this.radius * this.radius);
            }
        }else{
            this.blockNum = -1;
        }
    }

    public void sphere(){
        int centerX = this.playerPos.getX();
        int centerY = this.playerPos.getY();
        int centerZ = this.playerPos.getZ();
            for (int x = centerX - radius; x <= centerX + radius; x++) {
                for (int y = centerY - radius; y <= centerY + radius; y++) {
                    for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) + Math.pow(z - centerZ, 2));
                        if(hollow)
                        {//空心球
                            if (distance < radius && distance >= radius - 1){
                                BlockPos pos = new BlockPos(x, y, z);
                                MySetBlock.shapeSetBlock(this.world, this.player, pos, this.blockState, 2, this.undoMap); // 使用指定方块来代表球体
                            }
                        }else{//实心球
                            if (distance < radius){
                                BlockPos pos = new BlockPos(x, y, z);
                                MySetBlock.shapeSetBlock(this.world, this.player, pos, this.blockState, 2, this.undoMap); // 使用指定方块来代表球体
                            }
                        }
                    }
                }
            }
        player.teleportTo(centerX,centerY+this.radius,centerZ);

    }

//    public  void rotate(){
//        int tX = clyX - xOrigin;
//        int tY = 1;
//        int tZ = clyZ - zOrigin;
//
//        Matrix4f translationMatrix =  new Matrix4f().rotate(45,1,0,0);
//        Vector4f position = new Vector4f(tX,tY,tZ,1);
//        position = translationMatrix.transform(position);
//        int tranX = (int)position.x + xOrigin;
//        int tranY = (int)position.y + yOrigin;
//        int tranZ = (int)position.z + zOrigin;
//    }
}


