package com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape;

import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.MySetBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.RotateBlock;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapeBase {
    BlockPos pos1,pos2;
    Level world;
    Player player;
    PlayerMapData PMD;
    BlockState blockState;
    int radius,radiusZ;
    boolean permissionLevel,hollow,teleport,isMask,maskFlag;
    int height,blockNum;
    String shape;
    BlockPos playerPos; //生成时玩家位置
//    UndoData undoMap;
    int xOrigin;
    int yOrigin;
    int zOrigin;
    float xAngle,yAngle,zAngle;
    //客户端渲染使用
    Map<BlockState, Boolean> maskMap;
    //每个形状先存到列表中，然后根据列表生成
    List<BlockPos> posList;





    public ShapeBase(PlayerMapData PMD, Level level, Player player, BlockState blockState, int radius, int height, boolean hollow, String shape) {
//        this.pos1 = PMD.getPos1();
//        this.pos2 = PMD.getPos2();
        this.world = level;
        this.player = player;
        this.PMD = PMD;
        this.blockState = blockState;
        this.radius = radius;
        this.height = height;
        this.hollow = hollow;
        this.shape = shape;//要生成的类型
        this.playerPos = new BlockPos(player.getBlockX(),player.getBlockY(),player.getBlockZ());//使用笔刷的时候会修改为笔刷位置
        this.permissionLevel = player.hasPermissions(2);
        this.blockNum = 0;
        this.xOrigin = this.playerPos.getX();
        this.yOrigin = this.playerPos.getY();
        this.zOrigin = this.playerPos.getZ();
        this.teleport = true;
    }
    public ShapeBase(PlayerMapData PMD, Level level, Player player,BlockState blockState, int radius, int radiusZ,int height, boolean hollow,String shape) {
        this(PMD,level,player,blockState,radius,height,hollow,shape);
        this.radiusZ = radiusZ;
    }
    public ShapeBase(PlayerMapData PMD, ServerLevel level, Player player,BlockState blockState, int radius, int height, boolean hollow,String shape,float xAng,float yAng,float zAng) {
        this(PMD,level,player,blockState,radius,height,hollow,shape);
        this.xAngle = xAng;
        this.yAngle = yAng;
        this.zAngle = zAng;
    }
    public ShapeBase(PlayerMapData PMD, ServerLevel level, Player player,BlockPos playerPos,BlockState blockState, int radius, int radiusZ,int height, boolean hollow,String shape) {
        this(PMD,level,player,blockState,radius,height,hollow,shape);
        this.playerPos = playerPos;
    }

    public boolean init(){
        if(checkPos(this.player,this.PMD)){

            if(PlaceBlock.canPlaceBlock(this.pos1,this.pos2,this.world,this.player,this.blockState,this.blockNum, this.permissionLevel)) {
//                this.undoMap  = new UndoData(this.world);
//                PMD.getUndoDataMap().push(undoMap);
//                this.world.setBlock(this.pos1, Blocks.COBBLESTONE.defaultBlockState(), 2);
//                this.world.setBlock(this.pos2, Blocks.COBBLESTONE.defaultBlockState(), 2);
                posList = new ArrayList<>();
                return true;
            }


        }
        return false;
    }

    public void placeBlocks(){
        MySetBlock.setShapeFromList(this.posList, (ServerLevel) this.world, this.player, this.blockState, this.isMask, this.maskFlag, this.maskMap);
        if(teleport){
            player.teleportTo( this.playerPos.getX(),this.playerPos.getY()+this.height,this.playerPos.getZ());
        }
        Util.recordBrushLog(shape, this.player, this.pos1, this.pos2);//记录日志
    }

    public boolean place(){
        if(init()){
            switch (shape){
                case "sphere" -> {
                    sphere();
                    placeBlocks();
                }
                case "cyl" -> {
                    cyl();
                    placeBlocks();
                }
                case "ellipse" -> {
                    ellipse();
                    placeBlocks();
                }

            }
            return true;
        }
        return false;
    }


    public int getRadiusZ() {
        return radiusZ;
    }

    public void setRadiusZ(int radiusZ) {
        this.radiusZ = radiusZ;
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

    public boolean isMaskFlag() {
        return maskFlag;
    }

    public void setMask(boolean flag) {
        this.isMask = true;
        this.maskFlag = flag;
    }

    public boolean isTeleport() {
        return teleport;
    }

    public void setTeleport(boolean teleport) {
        this.teleport = teleport;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    public void setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public BlockPos getPlayerPos() {
        return playerPos;
    }

    public void setPlayerPos(BlockPos playerPos) {
        this.playerPos = playerPos;
    }

    public boolean checkPos(Player player, PlayerMapData PMD){
        if(!PlaceBlock.cheakFlag(PMD,player)){
            return false;
        }
        // 设置标志位
        PMD.setFlag(false);

        calcCylCubePos();
        //检查生成时候的高度
        return PlaceBlock.checkLowHeight(pos1, pos2, player);
    }

    //圆柱体
    public void cyl(){
        int xOrigin = this.playerPos.getX();
        int yOrigin = this.playerPos.getY();
        int zOrigin = this.playerPos.getZ();

//        boolean updateBlock = PlayerUtil.isSetUpdateBlock(player);
        double step = 180.0 / (Math.PI * radius);
        for (int i = 0; i < this.height; i++) {
            if (this.radius > 50 && !hollow) { //半径大于50的实心圆
                for (int x = -radius; x <= radius; x += 1) {
                    for (int z = -radius; z <= radius; z += 1) {
                        if (x * x + z * z <= radius * radius) {
                            BlockPos pos = new BlockPos(x + xOrigin, yOrigin+i, z + zOrigin);
                            pos = RotateBlock.rotateCyl(xAngle, yAngle, zAngle,pos, xOrigin,yOrigin ,zOrigin);
//                            MySetBlock.shapeSetBlock(this.world, pos, this.blockState, this.isMask, this.maskFlag, updateBlock, this.maskMap, this.undoMap);
                            this.posList.add(pos);
                        }
                    }
                }
//                if(teleport){
//                    player.teleportTo(xOrigin,yOrigin+this.height+1,zOrigin);
//                }

            } else { //空心圆
                for (double x = 0; x < 360; x += step) {
                    calcCylPos(xOrigin, yOrigin+i, zOrigin, x, this.radius);
                    if (!hollow) {
                        // 设置圆心到当前点之间的连线上的点，以形成实心圆
                        for (double j = 0; j <= radius; j+=0.5) {
                            calcCylPos(xOrigin, yOrigin+i, zOrigin, x, j);
                        }
//                        if(teleport){
//                            player.teleportTo(xOrigin,yOrigin+this.height+1,zOrigin);
//                        }

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

//        int vx = clyX -  xOrigin;
//        int vz = clyZ -  zOrigin;
//
//        Matrix4f translationMatrix =  new Matrix4f().rotate(45, 1, 0, 0);
//        Vector4f position = new Vector4f(vx,1,vz,1);
//        position = translationMatrix.transform(position);
//        int tranX = (int)position.x + xOrigin;
//        int tranY = (int)position.y + yOrigin;
//        int tranZ = (int)position.z + zOrigin;

//        boolean updateBlock = PlayerUtil.isSetUpdateBlock(player);
        BlockPos blockPos = new BlockPos(clyX, yOrigin, clyZ);
//        BlockPos bp = new BlockPos(tranX,tranY,tranZ);
        blockPos = RotateBlock.rotateCyl(xAngle,yAngle,zAngle,blockPos, xOrigin,yOrigin ,zOrigin);
        this.posList.add(blockPos);
//        MySetBlock.shapeSetBlock(this.world, bp, this.blockState, this.isMask,this.maskFlag,updateBlock,this.maskMap, this.undoMap);
     }

    //计算圆柱体所在方形的坐标
    public void calcCylCubePos(){
        switch (this.shape){
            case "cyl" -> {
                BlockPos p1 = this.playerPos.offset(this.radius,0,this.radius);
                BlockPos p2 = this.playerPos.offset(-this.radius,this.height-1,-this.radius);
                this.pos1 = RotateBlock.rotateCyl(xAngle,yAngle,zAngle,p1,xOrigin,yOrigin,zOrigin);
                this.pos2 = RotateBlock.rotateCyl(xAngle,yAngle,zAngle,p2,xOrigin,yOrigin,zOrigin);
            }
            case "sphere"->{
                this.pos1 = this.playerPos.offset(this.radius-1,-this.radius+1,this.radius-1);
                this.pos2 = this.playerPos.offset(-this.radius+1,this.radius-1,-this.radius+1);
            }
            case "ellipse"->{
                this.pos1 = this.playerPos.offset(this.radius,-this.height,this.radiusZ);
                this.pos2 = this.playerPos.offset(-this.radius,this.height,-this.radiusZ);
            }
        }

        if(this.hollow){
            switch (this.shape){
                case "cyl" -> this.blockNum = (int) (Math.PI * this.radius * 2);
                case "sphere" -> this.blockNum = (int) (4 * Math.PI * this.radius * this.radius);
                //椭圆体的表面积
                case "ellipse" -> this.blockNum = (int)((2 * Math.PI * this.radius * this.radiusZ)+ (Math.PI * this.height * this.height));
            }
        }else{
            this.blockNum = -1;
        }
    }


    //生成球体
    public void sphere(){
        int centerX = this.playerPos.getX();
        int centerY = this.playerPos.getY();
        int centerZ = this.playerPos.getZ();
//        boolean updateBlock = PlayerUtil.isSetUpdateBlock(player);
            for (int x = centerX - radius; x <= centerX + radius; x++) {
                for (int y = centerY - radius; y <= centerY + radius; y++) {
                    for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) + Math.pow(z - centerZ, 2));
                        if(hollow)
                        {//空心球
                            if (distance < radius && distance >= radius - 1){
                                BlockPos pos = new BlockPos(x, y, z);
                                this.posList.add(pos);
//                                MySetBlock.shapeSetBlock(this.world, pos, this.blockState, this.isMask,this.maskFlag,updateBlock,this.maskMap, this.undoMap);
                            }
                        }else{//实心球
                            if (distance < radius){
                                BlockPos pos = new BlockPos(x, y, z);
                                this.posList.add(pos);
//                                MySetBlock.shapeSetBlock(this.world, pos, this.blockState, this.isMask,this.maskFlag,updateBlock,this.maskMap, this.undoMap);
                            }
                        }
                    }
                }
            }
//        if(teleport){
//            player.teleportTo(centerX,centerY+this.radius,centerZ);
//        }

    }

    //生成投影
    public void sphereProjection(Map<BlockPos, Byte> shapePosMap){
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
                            shapePosMap.put(pos, (byte) 1);
//                            MySetBlock.shapeSetBlock(this.world, this.player, pos, this.blockState, 2, this.undoMap);
                        }
                    }else{//实心球
                        if (distance < radius){
                            BlockPos pos = new BlockPos(x, y, z);
                            shapePosMap.put(pos, (byte) 1);
//                            MySetBlock.shapeSetBlock(this.world, this.player, pos, this.blockState, 2, this.undoMap);
                        }
                    }
                }
            }
        }


    }




    //生成椭圆
    public void ellipse(){
        int centerX = this.playerPos.getX();
        int centerY = this.playerPos.getY();
        int centerZ = this.playerPos.getZ();
        int radiusX = this.radius;
        int radiusY = this.height;
        int radiusZ = this.radiusZ;



        double density;
        if(radiusX<10||radiusY<10||radiusZ<10)density = 0.5;  // 设置密度
        else density = 1;


        for (double x = centerX - radiusX; x <= centerX + radiusX; x += density) {
            for (double y = centerY - radiusY; y <= centerY + radiusY; y += density) {
                for (double z = centerZ - radiusZ; z <= centerZ + radiusZ; z += density) {
                    if (isPointInsideEllipsoid(x, y, z, centerX, centerY, centerZ, radiusX, radiusY, radiusZ)) {
                        BlockPos blockPos = new BlockPos((int)x,(int)y, (int)z);
//                        MySetBlock.shapeSetBlock(this.world, blockPos, this.blockState, this.isMask,this.maskFlag,this.maskMap, this.undoMap);
                        this.posList.add(blockPos);
                    }
                }
            }
        }


    }

    private boolean isPointInsideEllipsoid(int x, int y, int z, int xCenter, int yCenter, int zCenter,
                                                  int semiMajorAxis, int verticalRadius, int semiMinorAxis) {
        double normalizedX = (x - xCenter) / (double) semiMajorAxis;
        double normalizedY = (y - yCenter) / (double) verticalRadius;
        double normalizedZ = (z - zCenter) / (double) semiMinorAxis;

        double ellipsoidSurface = Math.pow(normalizedX, 2) + Math.pow(normalizedY, 2) + Math.pow(normalizedZ, 2);
        if(this.hollow)return ellipsoidSurface >= 0.9 && ellipsoidSurface <= 1.1; // 调整阈值以控制表面的宽度
        return ellipsoidSurface < 1;
    }

    private boolean isPointInsideEllipsoid(double x, double y, double z, int xCenter, int yCenter, int zCenter,
                                                  int semiMajorAxis, int verticalRadius, int semiMinorAxis) {
        double normalizedX = (x - xCenter) / (double) semiMajorAxis;
        double normalizedY = (y - yCenter) / (double) verticalRadius;
        double normalizedZ = (z - zCenter) / (double) semiMinorAxis;

//
        double ellipsoidSurface = Math.pow(normalizedX, 2) + Math.pow(normalizedY, 2) + Math.pow(normalizedZ, 2);
        if(this.hollow)return ellipsoidSurface >= 0.9 && ellipsoidSurface <= 1.1; // 调整阈值以控制表面的宽度
        return ellipsoidSurface < 1;

    }

    public String getShape() {
        return shape;
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


