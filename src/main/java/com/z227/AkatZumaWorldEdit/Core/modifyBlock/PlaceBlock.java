package com.z227.AkatZumaWorldEdit.Core.modifyBlock;


import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import com.z227.AkatZumaWorldEdit.utilities.RemoveItem;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.level.BlockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaceBlock {

    //判断能否放置方块 by ConstructionWand
    public static Boolean isPlaceBlock(Level world, Player player, BlockPos blockPos, BlockState blockState){

        BlockSnapshot blockSnapshot = BlockSnapshot.create(world.dimension(), world, blockPos);
        BlockEvent.EntityPlaceEvent entityPlaceEvent = new BlockEvent.EntityPlaceEvent(blockSnapshot, blockState, player);
        MinecraftForge.EVENT_BUS.post(entityPlaceEvent);
        return !entityPlaceEvent.isCanceled();
    }

    public static Boolean isPlaceBlock(Level world, Player player, BlockPos pos1,BlockPos pos2, BlockState blockState){
        int maxY = Math.max(pos1.getY(), pos2.getY());
//        int minY = Math.min(pos1.getY(), pos2.getY());
//        int y = maxY/2;
        int y = maxY;

        int maxX =  Math.max(pos1.getX(), pos2.getX());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());
        int minX = Math.min(pos1.getX(), pos2.getX());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());

        for (int x = minX; x <=maxX; x+=15) {
            for (int z = minZ; z <=maxZ; z+=15)  {
//                world.setBlock(new BlockPos(x, y, z), blockState, 2);
                if(!isPlaceBlock(world, player, new BlockPos(x, y, z), blockState))return false;
            }
        }

        for (int z = minZ; z <=maxZ; z+=15)  {
//            world.setBlock(new BlockPos(maxX, y, z), blockState, 2);
            if(!isPlaceBlock(world, player, new BlockPos(maxX, y, z), blockState))return false;
        }

        for (int x = minX; x <=maxX; x+=15) {
//            world.setBlock(new BlockPos(x, y, maxZ), blockState, 2);
            if(!isPlaceBlock(world, player, new BlockPos(x, y, maxZ), blockState))return false;
        }
//        world.setBlock(new BlockPos(maxX, y, maxZ), blockState, 2);
        if(!isPlaceBlock(world, player, new BlockPos(maxX, y, maxZ), blockState))return false;
        return true;
    }

    public static Boolean isPlaceBlockFromPlane(Level world, Player player, BlockPos pos1,BlockPos pos2, BlockState blockState){
        if(!isPlaceBlock(world, player, pos1,pos2, blockState)){
            Component component = Component.translatable("chat.akatzuma.error.not_permission_place");
            AkatZumaWorldEdit.sendAkatMessage(component,player);
            return false;
        }
        return true;
    }


    //计算长宽高
    public static Vec3i calculateCubeDimensions(BlockPos pos1, BlockPos pos2) {
        int length = Math.abs(pos1.getX() - pos2.getX()) + 1;
        int width = Math.abs(pos1.getY() - pos2.getY()) + 1;
        int height = Math.abs(pos1.getZ() - pos2.getZ()) + 1;
        return new Vec3i(length,width,height);
    }



    //遍历两个坐标之间的每个点
    public static void traverseCube(BlockPos pos1, BlockPos pos2, Level world, Player player, BlockState blockState, Map<BlockPos,BlockState> undoMap) {
        for (int x = Math.min(pos1.getX(), pos2.getX()); x <= Math.max(pos1.getX(), pos2.getX()); x++) {
            for (int y = Math.min(pos1.getY(), pos2.getY()); y <= Math.max(pos1.getY(), pos2.getY()); y++) {
                for (int z = Math.min(pos1.getZ(), pos2.getZ()); z <= Math.max(pos1.getZ(), pos2.getZ()); z++) {

                    BlockPos v3 = new BlockPos(x, y, z);
                    BlockState old =  world.getBlockState(v3);
                    undoMap.put(v3,old);
//                    world.setBlock(v3,blockState, 16);
//                    world.sendBlockUpdated(v3, old,blockState,16);
                    MySetBlock.setBlockNotUpdate(world,v3,old,blockState);

                }
            }
        }
    }


    public static boolean cheakFlag(PlayerMapData PMD, Player player) {
        // 判断上次操作是否完成
        if (!PMD.isFlag()) {
            Component component = Component.translatable("chat.akatzuma.error.wait");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return false;
        }
        return true;
    }

    //检查世界
    public static boolean cheakLevel(Level level, Player player) {
        String worldName =  ((ServerLevelData)level.getLevelData()).getLevelName();
        String dimension = level.dimension().location().toString();
        String tempName = worldName + "/" + dimension;
        if(AkatZumaWorldEdit.BlackWorldMap.containsKey(tempName)){
            Component component = Component.translatable("chat.akatzuma.error.black_world");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return false;
        }
        return true;
    }


     public static boolean checkArea(BlockPos pos1, BlockPos pos2,Player player,int configVolume, int  volume){
         // 选区大小
         if(volume > configVolume){

             Component component = Component.translatable("chat.akatzuma.error.volume_too_long");
             AkatZumaWorldEdit.sendAkatMessage(component,String.valueOf(configVolume), player);
             return false;
         }
         return true;
     }

    public static boolean checkBlackList(Player player, Integer n, MutableComponent deBlockName){
        //检查黑名单
        if(n < 0){
            Component component = Component.literal(" ")
                    .append(deBlockName).withStyle(ChatFormatting.GREEN)
                    .append(Component.translatable(("chat.akatzuma.error.black_list")));
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return false;
        }

        return true;
    }

    //检查指定方块是否不限制
    public static int getLimit( String blockName, Map<String,Integer> blackWhiteMap){
        //检查白名单
        if(blackWhiteMap.containsKey(blockName))return blackWhiteMap.get(blockName);
        //检查*
        String[] splitName = blockName.split(":");
        String prefixName = splitName[0];
        //String suffixName = splitName[1];
        if(blackWhiteMap.containsKey(prefixName+":*"))
            return blackWhiteMap.get(prefixName+":*");
        return 1;

    }

    public static String isReplaceToBuildItem(Player player, String blockName, Map<String,Integer> blackWhiteMap){
        if(!AkatZumaWorldEdit.ReplaceBlockMap.containsKey(blockName))return blockName;
        BlockState buildBlockState = AkatZumaWorldEdit.Building_Consumable_Block.get().defaultBlockState();
        String buildItemName = BlockStateString.getBlockName(buildBlockState);
        int n = getLimit(blockName, blackWhiteMap);  //比例值
        if(!checkBlackList(player,n, buildBlockState.getBlock().getName())){
            return blockName;
        }

        return buildItemName;

    }


    //检查背包是否足够，返回一个map为物品的槽位和数量，返回null则背包为空或者数量不够
    //@param blockName 扣除的物品名 minecraft:block
    //@param n 扣除的比例
    //@param volume 扣除的总数量
    //@param player 玩家
    //@param deBlockName 扣除物品的显示名
    public static List<Map<Integer, Integer>> checkInv(String blockName, int n, int volume, Player player, MutableComponent deBlockName) {
        List<Map<Integer, Integer>> temp = new ArrayList<>();
        Map<Integer, Integer> blockInInvMap = Util.findBlockInPlayerInv(player, blockName);
        Map<Integer, Integer> chestMap;
        Map<Integer, Integer> sopBackpackMap;

        temp.add(blockInInvMap);

        int sum = Util.sum(blockInInvMap);
            //要扣除的数量
        int num = (int) Math.ceil((double) volume / n);
        MutableComponent component = Component.translatable("chat.akatzuma.error.inventory_not_enough")
                .append(deBlockName).withStyle(ChatFormatting.GREEN).append(":"+num)
                .append(Component.translatable("chat.akatzuma.error.current_num"))
                ;

        //背包如果不够
        if (sum < num) {
            //如果玩家背包不够，遍历精妙背包
            sopBackpackMap = Util.findBlockInSopBackpack(player, blockName);
            int sopBackpackSum = Util.sum(sopBackpackMap);
            temp.add(sopBackpackMap);

            sum = sum + sopBackpackSum;
            //如果(玩家背包+精妙背包)不够，遍历绑定的箱子到temp
            if(sum < num){
                //遍历绑定的箱子
                chestMap = Util.findBlockInBindInv(player, blockName);
                int chestSum = Util.sum(chestMap);

                sum = sum + chestSum;
                if(sum < num){
                    //如果玩家(背包+精妙背包+绑定的箱子)还不够，返回null
                    AkatZumaWorldEdit.sendAkatMessage(component.append(":"+sum), player);
                    return null;
                }
                temp.add(chestMap);
            }

        }

        return temp;
    }

    //@param n 扣除的比例
    //@param volume 扣除的总数量
    public static void removeItemInPlayerInv(List<Map<Integer, Integer>>blockInInvMap, int n, int volume, Player player) {
        if(n==0||volume==0)return;
        //要扣除的数量
        int sum = (int) Math.ceil((double) volume / n);
        //遍历
        for (int i = 0; i < blockInInvMap.size(); i++){
            switch (i){
                case 0://玩家背包
                    sum = RemoveItem.removePlayerItem(blockInInvMap.get(i), player, sum);
                    break;
                case 1://精妙背包
                    sum = RemoveItem.removeSopBackpackItem(blockInInvMap.get(i), player, sum);
                    break;
                case 2://绑定的箱子
                    sum = RemoveItem.removeBindPosItem(blockInInvMap.get(i), player, sum);
                    break;


            }


        }


    }


    //#################################################################################################

    //set
    public static boolean canSetBlock(BlockPos pos1, BlockPos pos2, Level world, Player player, BlockState blockState, boolean permissionLevel, PlayerMapData PMD) {

        if(!checkPos(pos1, pos2, player, PMD))return false;

        //如果不是管理员
        return canPlaceBlock(pos1, pos2, world, player, blockState, -1, permissionLevel, PMD);
    }

    public static boolean canSetBlock(BlockPos pos1, BlockPos pos2, Level world, Player player, BlockState blockState,int num, boolean permissionLevel, PlayerMapData PMD) {

        if(!checkPos(pos1, pos2, player, PMD))return false;

        //如果不是管理员
        return canPlaceBlock(pos1, pos2, world, player, blockState, num, permissionLevel, PMD);
    }


    public static boolean checkPos(BlockPos pos1, BlockPos pos2,Player player, PlayerMapData PMD){
        if(!cheakFlag(PMD,player)){
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

        if(!PlaceBlock.checkLowHeight(pos1,pos2,player))return false;
        return true;
    }

    //检查最低高度
    public static boolean checkLowHeight(BlockPos pos1,BlockPos pos2, Player player){
        if(!player.hasPermissions(2)){
            if(pos1.getY()<Config.LOWHeight.get() || pos2.getY()<Config.LOWHeight.get()){
                AkatZumaWorldEdit.sendAkatMessage(Component.translatable("chat.akatzuma.error.low_hight"),player);
                return false;
            }
        }
        return true;
    }
    public static boolean checkVip(Player player){
        return AkatZumaWorldEdit.VipPlayerMap.containsKey(player.getName().getString());
    }

    public static boolean canPlaceBlock(BlockPos pos1, BlockPos pos2, Level world, Player player, BlockState blockState,int deductNum,  boolean permissionLevel, PlayerMapData PMD) {

        //如果不是管理员
        if (!permissionLevel) {
            //检查世界黑名单
            if(!cheakLevel(world,player))return false;

            int areaValue = Config.DEFAULTValue.get();      //选区大小
            boolean checkInventory = Config.CHECKInventory.get();    //是否检查背包
            Map<String, Integer> blackWhiteMap = AkatZumaWorldEdit.defaultBlockMap;    //黑白名单方块
            if (checkVip(player)) {
                areaValue = Config.VIPValue.get();      //选区大小
                checkInventory = Config.VIPCHECKInventory.get();    //是否检查背包
                blackWhiteMap = AkatZumaWorldEdit.VipBlockMap;    //黑白名单方块
            }

            // 选区大小
            Vec3i vec3 = calculateCubeDimensions(pos1, pos2);
            int volume =  vec3.getX() * vec3.getY()* vec3.getZ();

            MutableComponent deBlockName = blockState.getBlock().getName();

            // 选区大小
            if (!checkArea(pos1, pos2, player, areaValue, volume)) {
                return false;
            }

            if(deductNum != -1){
                volume  = deductNum;
            }


            //区块是否加载
            if (!world.hasChunkAt(pos1) || !world.hasChunkAt(pos2)) {
                MutableComponent component = Component.translatable("chat.akatzuma.error.chunk_not_loaded");
                AkatZumaWorldEdit.sendAkatMessage(component, player);
                return false;
            }


            String blockName = BlockStateString.getBlockName(blockState);
            int n = getLimit(blockName, blackWhiteMap);  //比例值
            List<Map<Integer, Integer>> blockInInvMap = null;


            //检查黑名单
            if (!checkBlackList(player, n, deBlockName)) {
                return false;
            }

            //检查是否替换成本MOD的建筑耗材方块
            blockName = isReplaceToBuildItem(player,blockName,blackWhiteMap);
            if(blockName.equals("akatzumaworldedit:building_consumable") ){
                deBlockName = AkatZumaWorldEdit.Building_Consumable_Block.get().defaultBlockState().getBlock().getName();
            }



            //检查背包 && 是否无限制放置
//            if(checkInventory && n > 0 && !player.isCreative()){
            if(checkInventory && n > 0){
//
                //返回一个map为物品的槽位和数量，返回null则背包为空或者数量不够
                blockInInvMap = checkInv(blockName,n,volume,player,deBlockName);
                if(blockInInvMap==null)return false;
            }

            //检查是否有放置权限
            if(!isPlaceBlockFromPlane(world, player, pos1, pos2, blockState))return false;


            //扣除背包
            if(blockInInvMap!=null){
                removeItemInPlayerInv(blockInInvMap, n, volume, player);
            }
        }
        return true;
    }



}//class
