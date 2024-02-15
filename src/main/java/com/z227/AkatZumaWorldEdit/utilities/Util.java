package com.z227.AkatZumaWorldEdit.utilities;

import com.mojang.blaze3d.platform.InputConstants;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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

    public static void logDebug(Player player, Component component){
        AkatZumaWorldEdit.LOGGER.debug("["+ player.getName().getString() + "]"+  component.getString());
    }
    public static void logDebug(String str){
        AkatZumaWorldEdit.LOGGER.debug(str);
    }
    public static void logDebug(Player player, String str){
        AkatZumaWorldEdit.LOGGER.debug("["+ player.getName().getString() + "]"+  str);
    }
    public static String logString(String message) {
        return "[" + message + "]";
    }
    public static void recordPosLog(BlockState blockState, Player player,String command) {
        String playerCommand = logString(command);
        String playerName = logString(player.getName().getString());
        String playerPos = logString(player.getOnPos().toString());
        String logBlockName = logString(blockState.getBlock().getName().getString() + "=" + blockState);
        PlayerMapData PMD= Util.getPMD(player);
        String pos1 = logString(String.valueOf(PMD.getPos1()));
        String pos2 = logString(String.valueOf(PMD.getPos2()));
        Util.logDebug(playerName+playerCommand+playerPos+logBlockName+pos1+pos2);
    }

    public static void recordPosLog(String str, Player player) {
        String playerName = logString(player.getName().getString());
        String playerPos = logString(player.getOnPos().toString());
        String playerCommand = logString(str);
        PlayerMapData PMD= Util.getPMD(player);
        String pos1 = logString(String.valueOf(PMD.getPos1()));
        String pos2 = logString(String.valueOf(PMD.getPos2()));
        Util.logDebug(playerName+playerCommand+playerPos+pos1+pos2);
    }


    public static void logInfo(String str){
        AkatZumaWorldEdit.LOGGER.info(str);
    }

    public static PlayerMapData getPMD(Player player){
        return AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
    }

    //添加查询的方块名称
    public static void addBlockStateText(List<Component> pTooltipComponents){
        LocalPlayer player =  Minecraft.getInstance().player;
        if(player!=null){
            PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
            Component component;
            Block block;
            if(PMD.getQueryBlockState()!=null){
                block = PMD.getQueryBlockState().getBlock();
            }else{
                block = Blocks.AIR;
            }
            component = Component.translatable("item.query_block_state.desc_block")
                    .append(block.getName())
                    .withStyle(ChatFormatting.GREEN);
            pTooltipComponents.add(component);
        }
    }

    public static boolean isDownKey(int key){
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key);
    }

    public static boolean isDownCtrl(){
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 341);
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



