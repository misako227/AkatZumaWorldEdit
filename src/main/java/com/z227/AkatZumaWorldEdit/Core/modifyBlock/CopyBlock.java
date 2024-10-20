package com.z227.AkatZumaWorldEdit.Core.modifyBlock;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.PosDirection;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CopyBlock {
    BlockPos playerCopyPos,playerPastePos, copyPos1,copyPos2;
    Map<BlockPos, BlockState> copyMap;

    //客户端渲染使用
    Map<BlockPos, BlockState> clientCopyMap;
//    List<RenderBlockDirectionBase> clientCopyList;
    Direction faceThePlayer;
    Direction playerClockWise;

//    Map<String, BlockPos> pastePosMap;//记录翻转后的位置，给tempPastePosMap使用
//    Map<String, BlockPos> tempPastePosMap;//粘帖时候的start和end坐标，用来判断是否有权限放置
    BlockPos pastePos1, pastePos2; //复制时的坐标
    BlockPos tempPastePos1, tempPastePos2;  //粘帖时候的start和end坐标，用来判断是否有权限放置

    Player player;
//    ServerLevel serverLevel;
    PlayerMapData PMD;
    Vec3i copyVec3, pasteVec3;
    boolean permissionLevel;
    BlockState invBlockState =  AkatZumaWorldEdit.Building_Consumable_Block.get().defaultBlockState();
    boolean air;
    boolean isMask,maskFlag;
    //客户端渲染使用
    Map<BlockState, Boolean> maskMap;



    public CopyBlock(PlayerMapData PMD, Player player) {
        this.playerCopyPos = player.getOnPos();
        this.copyPos1 = PMD.getPos1();
        this.copyPos2 = PMD.getPos2();
        this.player = player;
//        this.serverLevel =  serverLevel;
        this.copyMap = new HashMap<>();
        this.clientCopyMap = new HashMap<>();
//        this.clientCopyList = new ArrayList<>();
        this.faceThePlayer = player.getDirection().getOpposite(); //方块朝向玩家的一面
        this.playerClockWise = player.getDirection().getClockWise();
//        this.pastePosMap = new HashMap<>();
        this.PMD = PMD;
        this.permissionLevel = player.hasPermissions(2);

        this.copyVec3 = player.getDirection().getNormal();
    }

    public boolean init(ServerLevel serverlevel){
        if(canCopyBlock(copyPos1, copyPos2 ,player, PMD)){
            if(checkPosAddCopyMap(serverlevel)){
                return true;
            }
        }
        return false;
    }



    public boolean checkPosAddCopyMap(Level serverlevel){
        BlockPos pos1 = this.copyPos1,
                 pos2 = this.copyPos2;

        Map<String, Integer> blackWhiteMap = AkatZumaWorldEdit.defaultBlockMap;    //黑白名单方块
        if (!this.permissionLevel && !serverlevel.isClientSide) {
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
        pastePos1 = this.copyPos1.offset(-cx,-cy, -cz);
        pastePos2 = this.copyPos2.offset(-cx,-cy, -cz);
//        pastePosMap.put("startPos", );
//        pastePosMap.put("endPos", );

//        Component component;
        for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++) {
            for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++) {
                for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++) {
                    BlockPos pos = new BlockPos(x,y,z);
                    BlockState state = serverlevel.getBlockState(pos);
                    BlockPos transfPos = new BlockPos(x-cx, y-cy, z-cz);
                    //判断有没有黑名单
                    if (!this.permissionLevel && !serverlevel.isClientSide){
//                        if(state==Blocks.AIR.defaultBlockState())continue;
                        String blockName = BlockStateString.getBlockName(state);
                        int n = PlaceBlock.getLimit(blockName, blackWhiteMap);  //比例值
                        //检查黑名单
                        MutableComponent deBlockName = state.getBlock().getName();
                        if (!PlaceBlock.checkBlackList(player, n, deBlockName)) {
                            this.copyMap.clear();
                            return false;
                        }


                    }
                    //添加到copyMap
                    this.copyMap.put(transfPos, state);

                    if(serverlevel.isClientSide){
                        if(state.getBlock().equals(Blocks.AIR)) continue;
//                        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();

                        BlockPos newPos = pos.relative(faceThePlayer);

                        // 朝向玩家的面
                        if(Block.shouldRenderFace(state, serverlevel, pos, faceThePlayer, newPos)){
                            this.clientCopyMap.put(transfPos, state);
                            continue;
                        }

                        // c
                        Direction d = PosDirection.calcNextPosDirection(player.getOnPos(), pos);
                        newPos = pos.relative(d);
                        if(Block.shouldRenderFace(state, serverlevel, pos, d, newPos)){
                            this.clientCopyMap.put(transfPos, state);
                            continue;
                        }

                        Direction face = Direction.UP;
                        if(pos.getY() > playerCopyPos.getY()){
                            face = Direction.DOWN;
                        }
                        newPos = pos.relative(face);
                        if(Block.shouldRenderFace(state, serverlevel, pos, face, newPos)){
                            this.clientCopyMap.put(transfPos, state);
                        }

                    }


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



    public void flip(boolean Y,boolean client){

        Vec3i flipVec3 = new Vec3i(1,1,1);
//        Rotation rotation = Rotation.NONE;
        if(this.copyVec3.getX()==0){
            flipVec3 =   new Vec3i(-1,1,1);
//            rotation = Rotation.CLOCKWISE_180;
        }else{
            flipVec3 =   new Vec3i(1,1,-1);

        }
        if(Y)flipVec3 =   new Vec3i(1,-1,1);
        //修改Paste起始坐标
        setPastePosMapFlip(flipVec3);


        Map<BlockPos, BlockState> BlockMap;
        if(!client)BlockMap = this.copyMap;
        else BlockMap = this.clientCopyMap;

        // 翻转copyMap
        Map<BlockPos, BlockState> flippedCopyMap = new HashMap<>();
        for (Map.Entry<BlockPos, BlockState> entry : BlockMap.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();

            state = isFlipZFace(state,Y);



            int x = pos.getX()*flipVec3.getX(),
                y = pos.getY()*flipVec3.getY(),
                z = pos.getZ()*flipVec3.getZ();
            BlockPos transfPos = new BlockPos(x, y, z);
            flippedCopyMap.put(transfPos, state);
        }
        if(!client)this.copyMap = flippedCopyMap;
        else this.clientCopyMap = flippedCopyMap;


        if(!player.isLocalPlayer())AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.success.flip"), this.player);
    }

    public BlockState isFlipZFace(BlockState state,boolean Y){
        BlockState tempState = state;
        Optional<Direction> directionFace = state.getOptionalValue(HorizontalDirectionalBlock.FACING);
        //判断楼梯方向
        if(!directionFace.equals(Optional.empty())) {
            Optional<StairsShape> stairsShape = state.getOptionalValue(BlockStateProperties.STAIRS_SHAPE);
            if (this.copyVec3.getZ() == 0) {// X=1 or -1
                if (directionFace.get().getAxis() == Direction.Axis.X ) {
                    if(stairsShape.isPresent()){
                        tempState = setStrairsShapeZ(stairsShape.get(),state,false);
                    }else{
                        tempState = state;
                    }

                } else {
                    if(stairsShape.isPresent()){
                        tempState = setStrairsShapeZ(stairsShape.get(),state,true);
                        tempState = setDirectionFaceMirror(directionFace,tempState);
                    }else{
                        tempState = state.rotate(Rotation.CLOCKWISE_180);
                    }

                }

            } else {//Z=1 or -1
                if (directionFace.get().getAxis() == Direction.Axis.Z) {
                    if(stairsShape.isPresent()){
                        tempState = setStrairsShapeZ(stairsShape.get(),state,false);
                    }else{
                        tempState = state;
                    }
                } else {
                    if(stairsShape.isPresent()){
                        tempState = setStrairsShapeZ(stairsShape.get(),state,true);
                        tempState = setDirectionFaceMirror(directionFace,tempState);
                    }else{
                        tempState = state.rotate(Rotation.CLOCKWISE_180);
                    }
                }
            }
        }


        //判断楼梯上下翻转
        if(Y) {
            Optional<Half> directionHalf = state.getOptionalValue(BlockStateProperties.HALF);
            if (!directionHalf.equals(Optional.empty())) {
                if (directionHalf.get() == Half.TOP) {
                    tempState = state.setValue(BlockStateProperties.HALF, Half.BOTTOM);
                }else{
                    tempState = state.setValue(BlockStateProperties.HALF, Half.TOP);
                }
            }
        }
        return tempState;
    }

    public  BlockState setDirectionFaceMirror(Optional<Direction> face, BlockState state){
        Vec3i vec3i = face.get().getNormal();
        Direction  newDire = fromDelta(-vec3i.getX(),vec3i.getY(),-vec3i.getZ());
        return state.setValue(HorizontalDirectionalBlock.FACING,newDire);
    }

    public static Direction fromDelta(int pX, int pY, int pZ) {
        if (pX == 0) {
            if (pY == 0) {
                if (pZ > 0) {
                    return Direction.SOUTH;
                }

                if (pZ < 0) {
                    return Direction.NORTH;
                }
            } else if (pZ == 0) {
                if (pY > 0) {
                    return Direction.UP;
                }

                return Direction.DOWN;
            }
        } else if (pY == 0 && pZ == 0) {
            if (pX > 0) {
                return Direction.EAST;
            }

            return Direction.WEST;
        }

        return null;
    }

    public  BlockState setStrairsShapeZ(StairsShape pProperty, BlockState state, boolean z){
        switch (pProperty.getSerializedName()){
            case "inner_left" -> {
                return state.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.INNER_RIGHT);
            }
            case "inner_right" -> {
                return state.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.INNER_LEFT);
            }
            case "outer_left" -> {
                return state.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.OUTER_RIGHT);
            }
            case "outer_right" -> {
                return state.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.OUTER_LEFT);
            }
            default -> {
                if(z)return state.rotate(Rotation.CLOCKWISE_180);
                return state;
            }
        }
    }



//
//
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

//    public void rotate(Level serverlevel){
//        Component component;
//
//
//        int cx = this.playerCopyPos.getX(),
//            cy = this.playerCopyPos.getY(),
//            cz = this.playerCopyPos.getZ();
//
//        Map<BlockPos, BlockState> rotateCopyMap = new HashMap<>();
//        for (Map.Entry<BlockPos, BlockState> entry : this.copyMap.entrySet()) {
//            BlockPos pos = entry.getKey();
//
//
//            BlockState state = entry.getValue();
//            rotateCopyMap.put(RotateBlock.rotateCopyMapZ(this.playerCopyPos,pos,this.copyPos1,this.copyPos2), state);
//        }
//        component = Component.translatable("chat.akatzuma.success.rotate");
//        this.copyMap = rotateCopyMap;
//        AkatZumaWorldEdit.sendAkatMessage(component, this.player);
//    }


    public boolean pasteBlock(ServerLevel serverlevel, Map<BlockPos, BlockState> undoMap, boolean air) {
//        boolean isLowHeight = false;
        //计算玩家朝向旋转的角度
        Rotation rotation = PosDirection.calcDirection(this.copyVec3,this.pasteVec3);

        //修改pos1、2为粘贴时的坐标系
        getPastePosAndRotation(rotation);
        //粘帖前权限检查
//        BlockPos pos1 = pastePos1,pos2= pastePos2;
        if (!PlaceBlock.canPlaceBlock( tempPastePos1, tempPastePos2,serverlevel, this.player, this.invBlockState ,-1, this.permissionLevel, this.PMD)){
            return false;
        }
        this.PMD.getUndoDataMap().push(undoMap);

        for (Map.Entry<BlockPos, BlockState> entry : this.copyMap.entrySet()) {

            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue().rotate(rotation);
            //根据玩家朝向旋转复制内容
            pos = pos.rotate(rotation);
            //转到粘帖时的坐标系
            BlockPos transfPos = pos.offset(this.playerPastePos);
            //判断放置的高度是否超过最低高度，超过则忽略
            if(transfPos.getY()< Config.LOWHeight.get()){
//                isLowHeight=true;
                continue;
            }
            if(!air && state== Blocks.AIR.defaultBlockState()){
                continue;
            }
            BlockState old = serverlevel.getBlockState(transfPos);

            undoMap.put(transfPos,old);
            MySetBlock.shapeSetBlock(serverlevel, transfPos, state, isMask,maskFlag,maskMap,undoMap);
//            serverlevel.setBlock(transfPos, state, 2);
        }
        return true;

    }


    public void setPastePosMapFlip(Vec3i vec3i){
        int x1 = this.pastePos1.getX()*vec3i.getX();
        int y1 = this.pastePos1.getY()*vec3i.getY();
        int z1 = this.pastePos1.getZ()*vec3i.getZ();
        this.tempPastePos1 = new BlockPos(x1,y1,z1);
        int x2 = this.pastePos2.getX()*vec3i.getX();
        int y2 = this.pastePos2.getY()*vec3i.getY();
        int z2 = this.pastePos2.getZ()*vec3i.getZ();
        this.tempPastePos2 = new BlockPos(x2,y2,z2);


    }
//计算粘帖时的pos1，pos2
    public void getPastePosAndRotation(Rotation rotation){
//        BlockPos start = this.pastePosMap.get("startPos");
//        BlockPos end = this.pastePosMap.get("endPos");
        BlockPos startPos = new BlockPos(pastePos1.rotate(rotation)).offset(this.playerPastePos);
        BlockPos endPos = new BlockPos(pastePos2.rotate(rotation)).offset(this.playerPastePos);
//        HashMap<String,BlockPos> map = new HashMap<>();
//        map.put("startPos",startPos);
//        map.put("endPos",endPos);
        this.tempPastePos1 = startPos;
        this.tempPastePos2 = endPos;
//        return map;

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

    public Map<BlockPos, BlockState> getClientCopyMap() {
        return clientCopyMap;
    }

    public void setClientCopyMap(Map<BlockPos, BlockState> clientCopyMap) {
        this.clientCopyMap = clientCopyMap;
    }

    public BlockPos getPlayerCopyPos() {
        return playerCopyPos;
    }

    public void setPlayerCopyPos(BlockPos playerCopyPos) {
        this.playerCopyPos = playerCopyPos;
    }

    public BlockPos getPlayerPastePos() {
        return playerPastePos;
    }

    //==========================================粘帖笔刷===================================================================

    public void modifyPlayerCopyPos(){

        int cx = (this.copyPos1.getX() + this.copyPos2.getX()) / 2;
        int cy = (this.copyPos1.getY() + this.copyPos2.getY()) / 2;
        int cz = (this.copyPos1.getZ() + this.copyPos2.getZ()) / 2;

//        this.playerCopyPos = new BlockPos(cx, cy, cz);


        Map<BlockPos, BlockState> tempCopyMap = new HashMap<>();

        //遍历copyMap
        for (Map.Entry<BlockPos, BlockState> entry : this.copyMap.entrySet()) {
            BlockPos pos = entry.getKey();
            BlockState state = entry.getValue();
            BlockPos newPos = pos.offset(playerCopyPos).offset(-cx, -cy, -cz);
            tempCopyMap.put(newPos, state);
        }
        this.copyMap = tempCopyMap;

        pastePos1 = this.copyPos1.offset(-cx,-cy, -cz);
        pastePos2 = this.copyPos2.offset(-cx,-cy, -cz);

    }

    public boolean isAir() {
        return air;
    }

    public void setAir(boolean air) {
        this.air = air;
    }


    public boolean isMask() {
        return isMask;
    }

    public boolean isMaskFlag() {
        return maskFlag;
    }

    public void setMask(boolean flag) {
        this.isMask = true;
        this.maskFlag = flag;
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


}
