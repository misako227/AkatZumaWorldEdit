package com.z227.AkatZumaWorldEdit.Core.modifyBlock;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import com.z227.AkatZumaWorldEdit.utilities.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class StackBlock {
    BlockPos pos1,pos2;
    BlockPos endPos1,endPos2, maxPos1, maxPos2;
    ServerLevel world;
    Player player;
    PlayerMapData PMD;
    Vec3i stackVec3, cube;
    int stackNum;
    boolean permissionLevel;
    Map<BlockPos, BlockState> stackMap;
    BlockState invBlockState = AkatZumaWorldEdit.Building_Consumable_Block.get().defaultBlockState();

    public StackBlock(PlayerMapData PMD, ServerLevel level, Player player, int num, int direction) {
        this.player = player;
        this.PMD = PMD;
        this.stackNum =  num;
        this.permissionLevel = player.hasPermissions(2);;
        this.world = level;
        this.pos1 = this.PMD.getPos1();
        this.pos2 = this.PMD.getPos2();
        this.stackMap = new HashMap<>();
        switch (direction) {
            case 0 -> this.stackVec3 = player.getDirection().getNormal();
            case 2 -> this.stackVec3 = new Vec3i(0, 1, 0);
            case 4 -> this.stackVec3 = new Vec3i(0, -1, 0);
        }

//        this.endPos1 = pos1
    }
    public boolean init(){

        if(PlaceBlock.checkPos(this.pos1,this.pos2,this.player,this.PMD)){
            this.cube = PlaceBlock.calculateCubeDimensions(this.pos1, this.pos2);
            this.endPos1 = getEndPos(this.pos1);
            this.endPos2 = getEndPos(this.pos2);
            calcMaxPos();
            if(!addStackMap())return false;
            //放置前权限检查
            return PlaceBlock.canPlaceBlock(this.maxPos1, this.maxPos2, this.world, this.player, this.invBlockState,-1, this.permissionLevel);
        }
//
        return false;
    }

    public BlockPos getEndPos(BlockPos pos){
        int x = this.stackVec3.getX(),
            y = this.stackVec3.getY(),
            z = this.stackVec3.getZ();
        int length = this.cube.getX(),
            height = this.cube.getY(),
            width = this.cube.getZ();
        int endX = pos.getX() + x * length * this.stackNum;
        int endY = pos.getY() + y * height * this.stackNum;
        int endZ = pos.getZ() + z * width * this.stackNum;

        return  new BlockPos(endX, endY, endZ);
    }

    public boolean addStackMap(){
        Map<String, Integer> blackWhiteMap = AkatZumaWorldEdit.defaultBlockMap;    //黑白名单方块
        if (!this.permissionLevel) {
            int areaValue = Config.DEFAULTValue.get();      //选区大小

            if (PlaceBlock.checkVip(this.player)) {
                areaValue = Config.VIPValue.get();      //选区大小
                blackWhiteMap = AkatZumaWorldEdit.VipBlockMap;    //黑白名单方块
            }
            // 选区大小
            Vec3i vec3 = PlaceBlock.calculateCubeDimensions(pos1, pos2);
            int volume =  vec3.getX() * vec3.getY()* vec3.getZ();
            // 选区大小
            if (!PlaceBlock.checkArea(this.maxPos1, this.maxPos2, player, areaValue, volume)) {
                return false;
            }
        }
        for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++) {
            for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++) {
                for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = this.world.getBlockState(pos);
                    if (!this.permissionLevel){
                        String blockName = BlockStateString.getBlockName(state);
                        int n = PlaceBlock.getLimit(blockName, blackWhiteMap);  //比例值
                        //检查黑名单
                        MutableComponent deBlockName = state.getBlock().getName();
                        if (!PlaceBlock.checkBlackList(player, n, deBlockName)) {
                            return false;
                        }
                    }
                    stackMap.put(pos, state);
                }
            }
        }
        return true;
    }

    public void stack(UndoData undoMap){
        int x = this.stackVec3.getX(),
            y = this.stackVec3.getY(),
            z = this.stackVec3.getZ();
        int length = this.cube.getX(),
            height = this.cube.getY(),
            width = this.cube.getZ();
        boolean isLowHeight = false;
        boolean flag = PlayerUtil.isSetUpdateBlock(player);
        for (int i = 1; i <= stackNum; i++) {
            for (Map.Entry<BlockPos, BlockState> entry : stackMap.entrySet()) {
                BlockPos pos = entry.getKey();
                BlockState state = entry.getValue();
                BlockPos newPos = pos.offset(x*i*length,y*i*height, z*i*width);
                if(newPos.getY()< Config.LOWHeight.get()){
                    isLowHeight=true;
                    continue;
                }
//                undoMap.put(newPos, world.getBlockState(newPos));
//                world.setBlock(newPos, state, 2);
                MySetBlock.setBlockAddUndo(world,newPos,state,flag,undoMap);
            }
        }
        AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.success.stack"),this.player);
        if(isLowHeight)AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.error.low_hight"),this.player);


    }


    public double calculateDistance(BlockPos point1, BlockPos point2) {
        double dx = point1.getX() -  point2.getX();
        double dy = point1.getY() -  point2.getY();
        double dz = point1.getZ() -  point2.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public void calcMaxPos(){
        double distance1 = calculateDistance(this.pos1, this.endPos2);
        double distance2 = calculateDistance(this.pos2, this.endPos1);
        if (distance1 > distance2) {
            this.maxPos1 = this.pos1;
            this.maxPos2 = this.endPos2;
        } else {
            this.maxPos1 = this.pos2;
            this.maxPos2 = this.endPos1;
        }
    }


    public BlockPos getMaxPos1() {
        return maxPos1;
    }

    public BlockPos getMaxPos2() {
        return maxPos2;
    }
}
