package com.z227.AkatZumaWorldEdit.Core;


import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import com.z227.AkatZumaWorldEdit.utilities.Util;
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
    public Boolean isPlaceBlock(Level world, Player player, BlockPos blockPos, BlockState blockState){

        BlockSnapshot snapshot = BlockSnapshot.create(world.dimension(), world, blockPos);
        BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(snapshot, blockState, player);
        MinecraftForge.EVENT_BUS.post(placeEvent);
        if(placeEvent.isCanceled()) {
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

                    BlockPos v3 = new BlockPos(x,y,z);
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

    //检测背包是否有指定物品，没有或者数量不够返回null，有则返回槽位：数量的Map
    public static Map getSlotInInv(String blockName,int multiple,int size, Player player, Map<String, Map<Integer,Integer>> playerInv){
        if (playerInv.containsKey(blockName)){
            int sum = playerInv.get(blockName).values().stream().mapToInt(Integer::intValue).sum() * multiple;
            if(sum < size){
                MutableComponent component = Component.translatable("chat.akatzuma.error.inventory_not_enough");
                AkatZumaWorldEdit.sendAkatMessage(component, player);
                return null;
            }
        }
        return  playerInv.get(blockName);
    }

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
            String blockName = BlockStateString.getBlockName(blockState);
            Integer n = blackWhiteMap.get(blockName);  //比例值
            if (n == null) n = 1;
            // 选区大小
            Vec3i vec3 = calculateCubeDimensions(pos1, pos2);
            int volume =  vec3.getX() * vec3.getY()* vec3.getZ();

            // 选区大小
            if (!checkArea(pos1, pos2, player, areaValue, volume)) {
                return false;
            }


            //检查黑名单
            if (!checkBlackList(player, n)) {
                return false;
            }

            //区块是否加载
            if (!world.hasChunkAt(pos1) || !world.hasChunkAt(pos2)) {
                MutableComponent component = Component.translatable("chat.akatzuma.error.chunk_not_loaded");
                AkatZumaWorldEdit.sendAkatMessage(component, player);
                return false;
            }

            Map<String, Map<Integer, Integer>> blockInInvMap;
            Map<Integer, Integer> itemSlot = null;
            if (checkInventory) {
                n = getLimit(blockName, blackWhiteMap);
                //检查是否无限制放置
                if (n > 0) {
                    //检查背包
                    blockInInvMap = Util.findPlayerInv(player);
                    if (blockInInvMap.size() > 0) {
                        itemSlot = getSlotInInv(blockName, n, volume, player, blockInInvMap);
                        if (itemSlot == null) return false;
                    }
                    //背包数量够则扣除
                    int num = (int) Math.ceil((double)volume / n);
                    //遍历
                    for (Map.Entry<Integer, Integer> entry : itemSlot.entrySet()) {
                        int slot = entry.getKey();
                        int num1 = entry.getValue();
//                        ItemStack invItem = player.getInventory().getItem(slot);
                        if (num1 >= num) {
                            player.getInventory().removeItem(slot, num);
                            break;
                        } else {
                            player.getInventory().removeItem(slot, num1);
                            num = num - num1;
                        }
                        if (num<=0)break;
                    }


                    //检查放置权限
                }


            }

        }
        return true;
    }



}//class
