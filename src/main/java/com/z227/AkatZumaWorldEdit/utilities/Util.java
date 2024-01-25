package com.z227.AkatZumaWorldEdit.utilities;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {



    //遍历玩家背包，返回物品的槽位和数量，并返回一个map，key为物品名称，value为槽位和数量
//    public static Map<String, Map<Integer,Integer>> findPlayerInv(Player player){
//        Map<String, Map<Integer,Integer>> temp = new HashMap<>();
//        int size = player.getInventory().getContainerSize();
//        for(int i = 0; i < size; ++i) {
//            ItemStack invItem = player.getInventory().getItem(i);
//            if (!invItem.isEmpty() ) {
//                String itemName = invItem.getDescriptionId().replace("block.","").replace("." ,":");
//                int count = invItem.getCount();
//                if (!temp.containsKey(itemName)) {
//                    temp.put(itemName, new HashMap<>());
//                }
//                temp.get(itemName).put(i, count);
//
//
//            }
//        }
//        return temp;
//    }


    //遍历玩家背包，返回一个map为物品的槽位和数量
    public static Map<Integer,Integer> findBlockInPlayerInv(Player player, String blockName){
        Map<Integer,Integer> temp = new HashMap<>();
        int size = player.getInventory().getContainerSize();
        for(int i = 0; i < size; ++i) {
            ItemStack invItem = player.getInventory().getItem(i);
            if (!invItem.isEmpty() ) {
                String itemName = invItem.getDescriptionId().replace("block.","").replace("." ,":");
                int count = invItem.getCount();
                if (itemName.equals(blockName)){
                    temp.put(i, count);
                }
            }
        }
        return temp;
    }

    public static boolean checkVip(String playerName, List VIPPlayerList){
//        String name = player.getName().toString();
        return VIPPlayerList.contains(playerName);
    }


//    //检测背包是否有指定物品，没有或者数量不够返回null，有则返回槽位：数量的Map
//    public static Map<Integer, Integer> getSlotInInv(String blockName,int multiple,int size, Player player, Map<String, Map<Integer,Integer>> playerInv){
//        if (!playerInv.containsKey(blockName))return null;
//        int sum = playerInv.get(blockName).values().stream().mapToInt(Integer::intValue).sum() * multiple;
//        if(sum < size){
//            MutableComponent component = Component.translatable("chat.akatzuma.error.inventory_not_enough");
//            AkatZumaWorldEdit.sendAkatMessage(component, player);
//            return null;
//        }
//        return playerInv.get(blockName);
//
////        return  null ;
//    }

//    public static boolean checkOreTags(ITag<Block> Tags, BlockState state, Player player){
//        //判断矿词
//        if(Tags.contains(state.getBlock())){
//            Component component = Component.literal(" ")
//                    .append(state.getBlock().getName()).withStyle(ChatFormatting.GREEN)
//                    .append(Component.translatable(("chat.akatzuma.error.black_list")));
//            AkatZumaWorldEdit.sendAkatMessage(component,player);
//            return false;
//        }
//        return true;
//    }
}



