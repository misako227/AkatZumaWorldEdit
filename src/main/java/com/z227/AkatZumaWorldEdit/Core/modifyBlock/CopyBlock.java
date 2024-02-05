package com.z227.AkatZumaWorldEdit.Core.modifyBlock;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.PosDirection;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class CopyBlock {
    BlockPos playerCopyPos,playerPastePos, copyPos1,copyPos2;
    Map<BlockPos, BlockState> copyMap;
    Map<String, BlockPos> pastePosMap;//记录翻转后的位置，给tempPastePosMap使用
    Map<String, BlockPos> tempPastePosMap;//粘帖时候的start和end坐标，用来判断是否有权限放置
    Player player;
//    ServerLevel serverLevel;
    PlayerMapData PMD;
    Vec3i copyVec3, pasteVec3;
    boolean permissionLevel;
    BlockState invBlockState =  AkatZumaWorldEdit.Building_Consumable_Block.get().defaultBlockState();


    public CopyBlock(PlayerMapData PMD, Player player) {
        this.playerCopyPos = player.getOnPos();
        this.copyPos1 = PMD.getPos1();
        this.copyPos2 = PMD.getPos2();
        this.player = player;
//        this.serverLevel =  serverLevel;
        this.copyMap = new HashMap<>();
        this.pastePosMap = new HashMap<>();
        this.PMD = PMD;
        this.permissionLevel = player.hasPermissions(2);

        this.copyVec3 = player.getDirection().getNormal();
    }

    public boolean init(ServerLevel serverlevel){
        if(canCopyBlock(copyPos1, copyPos2 ,player, PMD)){
            if(checkPosAddCopyMap(serverlevel, PMD)){
                return true;
            }
        }
        return false;
    }



    public boolean checkPosAddCopyMap(ServerLevel serverlevel, PlayerMapData PMD){
        BlockPos pos1 = this.copyPos1,
                 pos2 = this.copyPos2;

        Map<String, Integer> blackWhiteMap = AkatZumaWorldEdit.defaultBlockMap;    //黑白名单方块
        if (!this.permissionLevel) {
            if(!PlaceBlock.cheakLevel(serverlevel,player))return false; //世界黑名单
            int areaValue = Config.DEFAULTValue.get();      //选区大小

            if (PlaceBlock.checkVip(this.player)) {
                areaValue = Config.VIPValue.get();      //选区大小
                blackWhiteMap = AkatZumaWorldEdit.VipBlockMap;    //黑白名单方块
            }

            // 选区大小
            Vec3i vec3 = PlaceBlock.calculateCubeDimensions(pos1, pos2);
            int volume =  vec3.getX() * vec3.getY()* vec3.getZ();
            // 选区大小
            if (!PlaceBlock.checkArea(pos1, pos2, player, areaValue, volume)) {
                return false;
            }

        }

        int cx = this.playerCopyPos.getX(),
            cy = this.playerCopyPos.getY(),
            cz = this.playerCopyPos.getZ();
        pastePosMap.put("startPos", this.copyPos1.offset(-cx,-cy, -cz));
        pastePosMap.put("endPos", this.copyPos2.offset(-cx,-cy, -cz));

//        Component component;
        for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++) {
            for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++) {
                for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++) {
                    BlockPos pos = new BlockPos(x,y,z);
                    BlockState state = serverlevel.getBlockState(pos);
                    BlockPos transfPos = new BlockPos(x-cx, y-cy, z-cz);
                    //判断有没有黑名单
                    if (!this.permissionLevel){
//                        if(state==Blocks.AIR.defaultBlockState())continue;
                        String blockName = BlockStateString.getBlockName(state);
                        int n = PlaceBlock.getLimit(blockName, blackWhiteMap);  //比例值
                        //检查黑名单
                        MutableComponent deBlockName = state.getBlock().getName();
                        if (!PlaceBlock.checkBlackList(player, n, deBlockName)) {
                            this.copyMap=null;
                            return false;
                        }
                    }
                    //添加到copyMap
                    this.copyMap.put(transfPos, state);
                }
            }
        }


        return true;
    }
    public boolean canCopyBlock(BlockPos pos1, BlockPos pos2, Player player, PlayerMapData PMD){
        if(!PlaceBlock.cheakFlag(PMD,player)){
            return false;
        }
        // 设置标志位
        PMD.setFlag(false);

        // 判断坐标是否为null
        if (pos1 == null || pos2 == null) {
            MutableComponent component = Component.translatable("chat.akatzuma.error.invalid_pos");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return false;
        }

        return true;
    }

    public void flip(boolean Y){
        Component component;

        Vec3i flipVec3 = new Vec3i(1,1,1);
        Rotation rotation = Rotation.NONE;
        if(this.copyVec3.getX()==0){
            flipVec3 =   new Vec3i(-1,1,1);
            rotation = Rotation.CLOCKWISE_180;
        }else{
            flipVec3 =   new Vec3i(1,1,-1);

        }
        if(Y)flipVec3 =   new Vec3i(1,-1,1);
        //修改Paste起始坐标
        setPastePosMapFlip(flipVec3);

        // 翻转copyMap
        Map<BlockPos, BlockState> flippedCopyMap = new HashMap<>();
        for (Map.Entry<BlockPos, BlockState> entry : this.copyMap.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue().rotate(rotation);

            int x = pos.getX()*flipVec3.getX(),
                y = pos.getY()*flipVec3.getY(),
                z = pos.getZ()*flipVec3.getZ();
            BlockPos transfPos = new BlockPos(x, y, z);
            flippedCopyMap.put(transfPos, state);
        }
        component = Component.translatable("chat.akatzuma.success.flip");
        this.copyMap = flippedCopyMap;
        AkatZumaWorldEdit.sendAkatMessage(component, this.player);
    }

//    public void isFlipZ(BlockState state){
////        if(state.getOptionalValue(Property))
//    }



//    public void rotate(float xAngle,float yAngle,float zAngle){
//        Component component;
//
//        Matrix4f translationMatrix =  new Matrix4f().rotate(90, 0, 1, 0);
////        if(xAngle != 0 ){
////            translationMatrix.rotate(xAngle, 1, 0, 0);
////        }
////        if(zAngle != 0 ){
////            translationMatrix.rotate(zAngle, 0, 0, 1);
////        }
//
//        // 旋转copyMap
//        Map<BlockPos, BlockState> rotateCopyMap = new HashMap<>();
//        for (Map.Entry<BlockPos, BlockState> entry : this.copyMap.entrySet()) {
//            BlockPos pos = entry.getKey();
//            int x = pos.getX();
//            int y = pos.getY();
//            int z = pos.getZ();
//
//            BlockState state = entry.getValue();
//            Vector4f position = new Vector4f(x,y,z,1);
//            position = translationMatrix.transform(position);
//            rotateCopyMap.put(new BlockPos(Math.toIntExact(Math.round(position.x)), Math.toIntExact(Math.round(position.y)), Math.toIntExact(Math.round(position.z))), state);
//        }
//
//        component = Component.translatable("chat.akatzuma.success.rotate");
//        this.copyMap = rotateCopyMap;
//        AkatZumaWorldEdit.sendAkatMessage(component, this.player);
//    }


    public void pasteBlock(ServerLevel serverlevel, Map<BlockPos, BlockState> undoMap) {
        boolean isLowHeight = false;
        //计算玩家朝向旋转的角度
        Rotation rotation = PosDirection.calcDirection(this.copyVec3,this.pasteVec3);

        this.tempPastePosMap = getPastePosMap(rotation);
        //粘帖前权限检查
        BlockPos pos1 = tempPastePosMap.get("startPos"),pos2= tempPastePosMap.get("endPos");
        if (!PlaceBlock.canPlaceBlock( pos1, pos2,serverlevel, this.player, this.invBlockState ,-1, this.permissionLevel, this.PMD)){
            return;
        }

        for (Map.Entry<BlockPos, BlockState> entry : this.copyMap.entrySet()) {

            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue().rotate(rotation);
            //根据玩家朝向旋转复制内容
            pos = pos.rotate(rotation);
            //转到粘帖时的坐标系
            BlockPos transfPos = pos.offset(this.playerPastePos);
            //判断放置的高度是否超过最低高度，超过则忽略
            if(transfPos.getY()< Config.LOWHeight.get()){
                isLowHeight=true;
                continue;
            }
            BlockState old = serverlevel.getBlockState(transfPos);

            undoMap.put(transfPos,old);
            MySetBlock.setBlockNotUpdate(serverlevel, transfPos, old, state);
//            serverlevel.setBlock(transfPos, state, 2);
        }
        AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.success.paste"),this.player);
        if(isLowHeight)AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.error.ignore_low_hight"),this.player);
    }


    public void setPastePosMapFlip(Vec3i vec3i){
        BlockPos start = this.pastePosMap.get("startPos");
        BlockPos end = this.pastePosMap.get("endPos");
        this.pastePosMap.put("startPos",new BlockPos(
                start.getX()*vec3i.getX(),
                start.getY()*vec3i.getY(),
                start.getZ()*vec3i.getZ()
        ));
        this.pastePosMap.put("endPos",new BlockPos(
                end.getX()*vec3i.getX(),
                end.getY()*vec3i.getY(),
                end.getZ()*vec3i.getZ()
        ));
    }
//计算粘帖时的pos1，pos2
    public HashMap<String,BlockPos> getPastePosMap(Rotation rotation){
        BlockPos start = this.pastePosMap.get("startPos");
        BlockPos end = this.pastePosMap.get("endPos");
        BlockPos startPos = new BlockPos(start.rotate(rotation)).offset(this.playerPastePos);
        BlockPos endPos = new BlockPos(end.rotate(rotation)).offset(this.playerPastePos);
        HashMap<String,BlockPos> map = new HashMap<>();
        map.put("startPos",startPos);
        map.put("endPos",endPos);
        return map;
    }

//    public Map<String, BlockPos> getTempPastePosMap() {
//        return tempPastePosMap;
//    }

    public Map<BlockPos, BlockState> getCopyMap() {
        return copyMap;
    }

    public void setPlayerPastePos(BlockPos playerPastePos) {
        this.playerPastePos = playerPastePos;
    }

    public void setPasteVec3(Vec3i pasteVec3) {
        this.pasteVec3 = pasteVec3;
    }

    public Vec3i getCopyVec3() {
        return copyVec3;
    }

    public Vec3i getPasteVec3() {
        return pasteVec3;
    }

    //    public BlockPos getCopyPos1() {
//        return copyPos1;
//    }
//
//    public BlockPos getCopyPos2() {
//        return copyPos2;
//    }
}
