package com.z227.AkatZumaWorldEdit.utilities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    //查找这个物品在玩家背包中的位置
    //返回null表示没有找到，有则返回在背包中的位置,return Map<位置, 数量>,位置是str，需要转换一下
    public static Map<String, Integer> findBlockFromPlayerInv(BlockState blockState, Player player){
        String[] resourceName = blockState.getBlock().toString().replace("Block{","").replace("}" ,"").split(":");
        if (resourceName.length < 2){
            return null;
        }
        RegistryObject<Item> tempItem = RegistryObject.create(new ResourceLocation(resourceName[0],resourceName[1]), ForgeRegistries.ITEMS);
        ItemStack it = tempItem.get().getDefaultInstance();  //wood_axe  ：后面的名字
        int invSize = player.getInventory().getContainerSize();

        Map<String, Integer> temp = new HashMap<>();

        for(int i = 0; i < invSize; ++i) {
            if (!player.getInventory().getItem(i).isEmpty() && ItemStack.isSameItemSameTags(it, player.getInventory().getItem(i))) {
                int count = player.getInventory().getItem(i).getCount();
                temp.put(String.valueOf(i), count);
            }
        }
        int sum = temp.values().stream().mapToInt(Integer::intValue).sum();
        temp.put("count", sum);

        return temp;
    }


    public static boolean checkVip(String playerName, List VIPPlayerList){
//        String name = player.getName().toString();
        return VIPPlayerList.contains(playerName);
    }
}
