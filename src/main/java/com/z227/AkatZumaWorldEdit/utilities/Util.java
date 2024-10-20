package com.z227.AkatZumaWorldEdit.utilities;

import com.mojang.blaze3d.platform.InputConstants;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Capability.BindInventoryPos;
import com.z227.AkatZumaWorldEdit.Capability.BindInventoryPosCapability;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedstorage.block.WoodStorageBlockBase;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public static Map<Integer,Integer> findBlockInContainer(Level level, BlockPos pos, String blockName){

        BlockEntity be =  level.getBlockEntity(pos);
        LazyOptional<IItemHandler> itemHandler = be.getCapability(ForgeCapabilities.ITEM_HANDLER);
        Map<Integer,Integer> temp = new HashMap<>();
        itemHandler.ifPresent(container -> {
            int size = container.getSlots();
            for(int i = 0; i < size; ++i) {
                ItemStack invItem = container.getStackInSlot(i);
                if (!invItem.isEmpty() ) {
                    String itemName = invItem.getDescriptionId().replace("block.","").replace("." ,":");
                    int count = invItem.getCount();
                    if (itemName.equals(blockName)){
                        temp.put(i, count);
                    }
                }
            }
        });

        return temp;
    }


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

    public static Map<Integer,Integer> findSopBackpack(String blockName, ItemStack itemStack){
        Map<Integer,Integer> sopBackpackMap = new HashMap<>();
        LazyOptional<IBackpackWrapper> lazyBackpackWrapper = itemStack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance());
        if(lazyBackpackWrapper.isPresent()){
            IBackpackWrapper backpackWrapper = lazyBackpackWrapper.orElse(null);
            InventoryHandler inventoryHandler = backpackWrapper.getInventoryHandler();
            int slots = inventoryHandler.getSlots();
            for(int i = 0; i < slots; i++){
                ItemStack invItemStack = inventoryHandler.getSlotStack(i);
                if (!invItemStack.isEmpty() ) {
                    String itemName = invItemStack.getDescriptionId().replace("block.","").replace("." ,":");
                    int count = invItemStack.getCount();
                    if (itemName.equals(blockName)){
                        sopBackpackMap.put(i, count);
                    }
                }
            }
        }
        return sopBackpackMap;
    }

    public static Map<Integer,Integer> findBlockInSopBackpack(Player player, String blockName){
        Map<Integer,Integer> temp = new HashMap<>();
        if(!Util.isLoadSopBackpacks()) return temp;

        PlayerMapData PMD = Util.getPMD(player);

        if(Util.isLoadCurios()){//查找饰品栏第一个背包栏
            LazyOptional<ICuriosItemHandler> curiosHandler = player.getCapability(CuriosCapability.INVENTORY);
            if(curiosHandler.isPresent()){
                ICuriosItemHandler curios = curiosHandler.orElse(null);
//                Optional<SlotResult> optionalSlotResult = curios.findCurio("back",0); // mc1.20.1
                Optional<ICurioStacksHandler> optionalSlotResult = curios.getStacksHandler("back");


                if(optionalSlotResult.isPresent()){
                    IDynamicStackHandler dynamicStackHandler= optionalSlotResult.get().getCosmeticStacks();
                    int slots = optionalSlotResult.get().getSlots();
                    if(slots>0){
                        ItemStack itemStack = dynamicStackHandler.getStackInSlot(0);


//                    ItemStack itemStack = optionalSlotResult.get().stack();

                        if(itemStack.getItem() instanceof BackpackItem){
                            temp = findSopBackpack(blockName, itemStack);
                            PMD.setSopBackpackFlag((byte) 1);
                            return temp;
                        }
                    }
                }
            }
        }
        //查找玩家胸甲和背包第一个格子
        Inventory playerInv = player.getInventory();

        List<ItemStack> armorList =playerInv.armor;
        if(armorList.size() > 2){//查找玩家胸甲栏
            ItemStack itemStack =  armorList.get(2);
            if(itemStack.getItem() instanceof BackpackItem){
                temp = findSopBackpack(blockName, itemStack);
                PMD.setSopBackpackFlag((byte) 2);
                return temp;
            }
        }
        //查找玩家背包第9个格子
        int slots = playerInv.getContainerSize();
        if(slots > 9){
            ItemStack itemStack = playerInv.getItem(9);
            if(itemStack.getItem() instanceof BackpackItem){
                temp = findSopBackpack(blockName, itemStack);
                PMD.setSopBackpackFlag((byte) 3);
                return temp;
            }
        }


        return temp;
    }

    //遍历绑定的箱子
    public static Map<Integer,Integer> findBlockInBindInv(Player player, String blockName){
        Map<Integer,Integer> temp = new HashMap<>();

        LazyOptional<BindInventoryPos> bindPos = player.getCapability(BindInventoryPosCapability.BIND_INV_POS_CAP);
        if (bindPos.isPresent()){
            BindInventoryPos bp = bindPos.orElse(null);
            CompoundTag tag =  bp.getCompoundNBT();

            int[] intPos = tag.getIntArray("pos");
            if(intPos.length == 3){
                BlockPos pos = new BlockPos(intPos[0], intPos[1], intPos[2]);
                Level level = player.getLevel();
                //区块未加载
                if (!level.hasChunkAt(pos)) {
                    return temp;
                }
                Util.getPMD(player).setInvPos(pos);

                BlockState blockState = level.getBlockState(pos);
                Block block = blockState.getBlock();
                if(block instanceof ChestBlock || block instanceof BarrelBlock){
                    temp = findBlockInContainer(level, pos, blockName);
                    return temp;
                }

                if(Util.isLoadSopStorage()){
                    if(block instanceof WoodStorageBlockBase){
                        temp = findBlockInContainer(level, pos, blockName);
                        return temp;
                    }
                }


            }

        }

        return temp;
    }


    public static int sum(Map<Integer, Integer> map) {
        if(map.isEmpty()) return 0;
        return map.values().stream().mapToInt(Integer::intValue).sum();
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
        PlayerMapData PMD = getPMD(player);
        String pos1 = logString(String.valueOf(PMD.getPos1()));
        String pos2 = logString(String.valueOf(PMD.getPos2()));
        Util.logDebug(playerName+playerCommand+playerPos+logBlockName+pos1+pos2);
    }

    public static void recordPosLog(String str, Player player) {
        String playerName = logString(player.getName().getString());
        String playerPos = logString(player.getOnPos().toString());
        String playerCommand = logString(str);
        PlayerMapData PMD = getPMD(player);
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
    public static boolean isDownLAlt(){
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 342);
    }

    public static boolean isLoadSopBackpacks(){
        return AkatZumaWorldEdit.loadSopBackpacks;
    }

    public static boolean isLoadSopStorage(){
        return AkatZumaWorldEdit.loadSopStorage;
    }

    public static boolean isLoadCurios() {
        return AkatZumaWorldEdit.loadCurios;
    }

    public static void setLoadSop() {
        if(ModList.get().isLoaded("sophisticatedbackpacks")){
            AkatZumaWorldEdit.loadSopBackpacks = true;
        }
        if(ModList.get().isLoaded("sophisticatedstorage")){
            AkatZumaWorldEdit.loadSopStorage = true;
        }
        if(ModList.get().isLoaded("curios")){
            AkatZumaWorldEdit.loadCurios = true;
        }



    }

    public static String getPos(BlockPos pos ){
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
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



