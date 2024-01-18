package com.z227.AkatZumaWorldEdit.Core;


import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.level.BlockEvent;

import java.util.Map;

public class PlaceBlock {

    //判断能否放置方块
    public static Boolean isPlaceBlock(Level world, Player player, BlockPos blockPos, BlockState blockState){

        BlockSnapshot snapshot = BlockSnapshot.create(world.dimension(), world, blockPos);
        BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(snapshot, blockState, player);
        MinecraftForge.EVENT_BUS.post(placeEvent);
        return !placeEvent.isCanceled();
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
                    undoMap.put(v3,world.getBlockState(v3));
                    world.setBlock(v3,blockState, 2);

                }
            }
        }
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

    public static boolean checkBlackList(Player player, Integer n){
        //检查黑名单
        if(n < 0){
            MutableComponent component =Component.translatable("chat.akatzuma.error.black_list");
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
        String prefixName = splitName[0];String suffixName = splitName[1];
        if(blackWhiteMap.containsKey(prefixName+":*"))
            return blackWhiteMap.get(prefixName+":*");
        return 1;

    }

    //检查背包是否足够，返回一个map为物品的槽位和数量，返回null则背包为空或者数量不够
    //@param blockName 扣除的物品名 minecraft:block
    //@param n 扣除的比例
    //@param volume 扣除的总数量
    //@param player 玩家
    //@param deBlockName 扣除物品的显示名
    public static Map<Integer, Integer> checkInv(String blockName, int n, int volume, Player player, MutableComponent deBlockName) {
        Map<Integer, Integer> blockInInvMap = Util.findBlockInPlayerInv(player, blockName);

            int sum = blockInInvMap.values().stream().mapToInt(Integer::intValue).sum();
            //要扣除的数量
            int num = (int) Math.ceil((double) volume / n);
            MutableComponent component = Component.translatable("chat.akatzuma.error.inventory_not_enough")
                    .append(deBlockName).withStyle(ChatFormatting.GREEN).append(":"+num);
            if (blockInInvMap.isEmpty()) {
                AkatZumaWorldEdit.sendAkatMessage(component, player);
                return null;
            }
            //背包如果不够
            if (sum < num) {
                AkatZumaWorldEdit.sendAkatMessage(component, player);
                return null;
            }


        return blockInInvMap;
    }

    public static void removeItemInPlayerInv(Map<Integer, Integer> blockInInvMap, int n, int volume, Player player) {
        if(n==0||volume==0)return;
        //要扣除的数量
        int num = (int) Math.ceil((double) volume / n);
        //遍历
        for (Map.Entry<Integer, Integer> entry : blockInInvMap.entrySet()) {
            int slot = entry.getKey();
            int num1 = entry.getValue();

            if (num1 >= num) {
                player.getInventory().removeItem(slot, num);
                break;
            } else {
                player.getInventory().removeItem(slot, num1);
                num = num - num1;
            }
            if (num <= 0) break;
        }


    }


    //#################################################################################################


    public static boolean canSetBlock(BlockPos pos1, BlockPos pos2, Level world, Player player, BlockState blockState, boolean permissionLevel, PlayerMapData PMD) {
        // 判断上次操作是否完成
        if (!PMD.isFlag()) {
            Component component = Component.translatable("chat.akatzuma.error.wait");
            AkatZumaWorldEdit.sendAkatMessage(component, player);
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


        //如果不是管理员
        if (!permissionLevel) {
            int areaValue = Config.DEFAULTValue.get();      //选区大小
            boolean checkInventory = Config.CHECKInventory.get();    //是否检查背包
            Map<String, Integer> blackWhiteMap = AkatZumaWorldEdit.defaultBlockMap;    //黑白名单方块
            if (PMD.isVip()) {
                areaValue = Config.VIPValue.get();      //选区大小
                checkInventory = Config.VIPCHECKInventory.get();    //是否检查背包
                blackWhiteMap = AkatZumaWorldEdit.VipBlockMap;    //黑白名单方块
            }

            // 选区大小
            Vec3i vec3 = calculateCubeDimensions(pos1, pos2);
            int volume =  vec3.getX() * vec3.getY()* vec3.getZ();

            // 选区大小
            if (!checkArea(pos1, pos2, player, areaValue, volume)) {
                return false;
            }



            //区块是否加载
            if (!world.hasChunkAt(pos1) || !world.hasChunkAt(pos2)) {
                MutableComponent component = Component.translatable("chat.akatzuma.error.chunk_not_loaded");
                AkatZumaWorldEdit.sendAkatMessage(component, player);
                return false;
            }


            String blockName = BlockStateString.getBlockName(blockState);
            int n = getLimit(blockName, blackWhiteMap);  //比例值
            Map<Integer, Integer> blockInInvMap = null;


            //检查黑名单
            if (!checkBlackList(player, n)) {
                return false;
            }


            //检查背包 && 是否无限制放置
            if(checkInventory && n > 0){
                MutableComponent deBlockName = blockState.getBlock().getName();
                //返回一个map为物品的槽位和数量，返回null则背包为空或者数量不够
                blockInInvMap = checkInv(blockName,n,volume,player,deBlockName);
                if(blockInInvMap==null)return false;
            }

            //检查是否有放置权限
            //todo


            //扣除背包
            if(blockInInvMap!=null){
                removeItemInPlayerInv(blockInInvMap, n, volume, player);
            }

        }
        return true;
    }



}//class
