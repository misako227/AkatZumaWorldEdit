//package com.z227.AkatZumaWorldEdit.Core.modifyBlock;
//
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
//import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Vec3i;
//import net.minecraft.network.chat.Component;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.state.BlockState;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MoveBlock {
//    BlockPos pos1,pos2;
//    BlockPos endPos1,endPos2, maxPos1, maxPos2;
//    ServerLevel world;
//    Player player;
//    PlayerMapData PMD;
//    Vec3i stackVec3, cube;
//    int moveNum;
//    boolean permissionLevel;
//    Map<BlockPos, BlockState> moveMap;
//
//
//    public MoveBlock(PlayerMapData PMD, ServerLevel level, Player player, int num, int direction) {
//        this.player = player;
//        this.PMD = PMD;
//        this.moveNum =  num;
//        this.permissionLevel = player.hasPermissions(2);;
//        this.world = level;
//        this.pos1 = this.PMD.getPos1();
//        this.pos2 = this.PMD.getPos2();
//        this.moveMap = new HashMap<>();
//        switch (direction) {
//            case 0 -> this.stackVec3 = player.getDirection().getNormal();
//            case 2 -> this.stackVec3 = new Vec3i(0, 1, 0);
//            case 4 -> this.stackVec3 = new Vec3i(0, -1, 0);
//        }
//    }
//
//
//    public boolean init(){
//
//        if(PlaceBlock.checkPos(this.pos1,this.pos2,this.player,this.PMD)){
//            this.cube = PlaceBlock.calculateCubeDimensions(this.pos1, this.pos2);
//            this.endPos1 = getEndPos(this.pos1);
//            this.endPos2 = getEndPos(this.pos2);
//            if(!PlaceBlock.checkLowHeight(this.endPos1,this.endPos2, this.player))return false;
//            if(!checkMoveLimit())return false;
////            return PlaceBlock.canPlaceBlock(this.maxPos1, this.maxPos2, this.world, this.player, this.invBlockState, this.permissionLevel, this.PMD);
//        }
////
//        return false;
//    }
//    public boolean checkMoveLimit(){
//        if(this.moveNum > Config.MOVELimit.get()){
//            AkatZumaWorldEdit.sendAkatMessage(new TranslatableComponent("chat.akatzuma.error.move_limit").append(String.valueOf(Config.MOVELimit.get())), this.player);
//            return false;
//        } return true;
//    }
//
//
//
//    public BlockPos getEndPos(BlockPos pos){
//        int x = this.stackVec3.getX(),
//            y = this.stackVec3.getY(),
//            z = this.stackVec3.getZ();
//        int length = this.cube.getX(),
//            height = this.cube.getY(),
//            width = this.cube.getZ();
//        int endX = pos.getX() + x * length * this.moveNum;
//        int endY = pos.getY() + y * height * this.moveNum;
//        int endZ = pos.getZ() + z * width * this.moveNum;
//
//        return  new BlockPos(endX, endY, endZ);
//    }
//    public boolean move(Map<BlockPos, BlockState> undoMap){
//        if (!this.permissionLevel) {
//            int areaValue = Config.DEFAULTValue.get();      //选区大小
//
//            if (PMD.isVip()) {
//                areaValue = Config.VIPValue.get();      //选区大小
////                blackWhiteMap = AkatZumaWorldEdit.VipBlockMap;    //黑白名单方块
//            }
//            // 选区大小
//            Vec3i vec3 = PlaceBlock.calculateCubeDimensions(pos1, pos2);
//            int volume =  vec3.getX() * vec3.getY()* vec3.getZ();
//            // 选区大小
//            if (!PlaceBlock.checkArea(this.maxPos1, this.maxPos2, player, areaValue, volume)) {
//                return false;
//            }
//        }
//        int length = this.cube.getX(),
//            height = this.cube.getY(),
//            width = this.cube.getZ();
//
//        for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++) {
//            for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++) {
//                for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++) {
//                    BlockPos pos = new BlockPos(x, y, z);
//                    BlockState state = this.world.getBlockState(pos);
//
//                    BlockPos newPos = pos.offset(this.stackVec3.getX()*length,this.stackVec3.getY()*height, this.stackVec3.getZ()*width);
//
//                    undoMap.put(pos, state);
//                    undoMap.put(newPos, world.getBlockState(newPos));
//                    world.setBlock(newPos, state, 2);
//                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
//                }
//            }
//        }
//        AkatZumaWorldEdit.sendAkatMessage(new TranslatableComponent("chat.akatzuma.success.stack"),this.player);
//
//        return true;
//    }
//}
