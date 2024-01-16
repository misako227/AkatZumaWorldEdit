package com.z227.AkatZumaWorldEdit.utilities;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    //查找这个物品在玩家背包中的位置
    //返回null表示没有找到，有则返回在背包中的位置,return Map<位置, 数量>,位置是str，需要转换一下
//    public static Map<String, Map<Integer,Integer>> findBlockFromPlayerInv(BlockState blockState, Player player){
//        String[] resourceName = blockState.getBlock().toString().replace("Block{","").replace("}" ,"").split(":");
////        if (resourceName.length < 2){
////            return null;
////        }
//        RegistryObject<Item> tempItem = RegistryObject.create(new ResourceLocation(resourceName[0],resourceName[1]), ForgeRegistries.ITEMS);
//        ItemStack it = tempItem.get().getDefaultInstance();  //wood_axe  ：后面的名字
//        int invSize = player.getInventory().getContainerSize();
//        System.out.println("invSize: " + invSize);
//
//        Map<String, Map<Integer,Integer>> temp = new HashMap<>();
////        temp.put(new HashMap<>());
//        for(int i = 0; i < 36; ++i) {
//            ItemStack invItem = player.getInventory().getItem(i);
//            if (!invItem.isEmpty() && ItemStack.isSameItemSameTags(it, invItem)) {
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
////        int sum = temp.values().stream().mapToInt(Integer::intValue).sum();
////        temp.put("count", sum);
//
//        return temp;
//    }



    //遍历玩家背包，返回物品的槽位和数量，并返回一个map，key为物品名称，value为槽位和数量
    public static Map<String, Map<Integer,Integer>> findPlayerInv(Player player){
        Map<String, Map<Integer,Integer>> temp = new HashMap<>();
        for(int i = 0; i < 36; ++i) {
            ItemStack invItem = player.getInventory().getItem(i);
            if (!invItem.isEmpty() ) {
                String itemName = invItem.getDescriptionId().replace("block.","").replace("." ,":");
                int count = invItem.getCount();
                if (!temp.containsKey(itemName)) {
                    temp.put(itemName, new HashMap<>());
                }
                temp.get(itemName).put(i, count);


            }
        }
        return temp;
    }

    public static boolean checkVip(String playerName, List VIPPlayerList){
//        String name = player.getName().toString();
        return VIPPlayerList.contains(playerName);
    }
}
