package com.z227.AkatZumaWorldEdit.utilities;

import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.api.CapabilityBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedstorage.block.WoodStorageBlockEntity;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RemoveItem {

    public static int removePlayerItem(Map<Integer, Integer> map, Player player, int sum){
        if(map.isEmpty()) return sum;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int slot = entry.getKey();
            int num1 = entry.getValue();

            if (num1 >= sum) {
                player.getInventory().removeItem(slot, sum);
                sum = 0;
                break;
            } else {
                player.getInventory().removeItem(slot, num1);
                sum = sum - num1;
            }
//            if (sum <= 0) break;
        }
        return sum;
    }

//    public static int removeItem(Map<Integer, Integer> map, Player player, int sum, ItemStack sopBackpackItemStack){
//        LazyOptional<IBackpackWrapper> lazyBackpackWrapper = sopBackpackItemStack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance());
//        if(lazyBackpackWrapper.isPresent()){
//            IBackpackWrapper backpackWrapper = lazyBackpackWrapper.orElse(null);
//            InventoryHandler inventoryHandler = backpackWrapper.getInventoryHandler();
//            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
//                int slot = entry.getKey();
//                int num1 = entry.getValue();
//
//                if (num1 >= sum) {
//                    ItemStack itemStack = inventoryHandler.getSlotStack(slot);
//                    itemStack.setCount(num1-sum);
////            InventoryHandler.setSlotStack(slot, itemStack);
//                    sum = 0;
//                    break;
//                } else {
//                    inventoryHandler.setStackInSlot(slot,ItemStack.EMPTY);
//                    sum = sum - num1;
//                }
//            }
//        }
//        return sum;
//    }

    public static int removeSopBackpackItem(Map<Integer, Integer> map, Player player, int sum){
        if(map.isEmpty()) return sum;
        PlayerMapData PMD = Util.getPMD(player);
        byte flag = PMD.getSopBackpackFlag();
        switch (flag){
            case(1):{
                LazyOptional<ICuriosItemHandler> curiosHandler = player.getCapability(CuriosCapability.INVENTORY);
                if(curiosHandler.isPresent()){
                    ICuriosItemHandler curios = curiosHandler.orElse(null);

//                    Optional<SlotResult> optionalSlotResult = curios.findCurio("back",0);

                    Optional<ICurioStacksHandler> optionalSlotResult = curios.getStacksHandler("back");
                    if(optionalSlotResult.isPresent()) {
                        IDynamicStackHandler dynamicStackHandler = optionalSlotResult.get().getCosmeticStacks();
                        ItemStack sopBackpackItemStack = dynamicStackHandler.getStackInSlot(0);
//                    ItemStack sopBackpackItemStack = optionalSlotResult.get().stack();
                        LazyOptional<IBackpackWrapper> lazyBackpackWrapper = sopBackpackItemStack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance());
                        if (lazyBackpackWrapper.isPresent()) {
                            IBackpackWrapper backpackWrapper = lazyBackpackWrapper.orElse(null);
                            InventoryHandler inventoryHandler = backpackWrapper.getInventoryHandler();
                            sum = removeItem(map, sum, inventoryHandler);
                            break;
                        }
                    }
                }
            }
            case(2):{
                Inventory playerInv = player.getInventory();
                List<ItemStack> armorList =playerInv.armor;
                //查找玩家胸甲栏
                ItemStack itemStack =  armorList.get(2);
                LazyOptional<IBackpackWrapper> lazyBackpackWrapper = itemStack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance());
                if(lazyBackpackWrapper.isPresent()) {
                    IBackpackWrapper backpackWrapper = lazyBackpackWrapper.orElse(null);
                    InventoryHandler inventoryHandler = backpackWrapper.getInventoryHandler();
                    sum = removeItem(map, sum, inventoryHandler);
                }

                break;
            }
            case(3):{
                //查找玩家背包第9个格子
                Inventory playerInv = player.getInventory();
                ItemStack itemStack = playerInv.getItem(9);
                LazyOptional<IBackpackWrapper> lazyBackpackWrapper = itemStack.getCapability(CapabilityBackpackWrapper.getCapabilityInstance());
                if(lazyBackpackWrapper.isPresent()) {
                    IBackpackWrapper backpackWrapper = lazyBackpackWrapper.orElse(null);
                    InventoryHandler inventoryHandler = backpackWrapper.getInventoryHandler();
                    sum = removeItem(map, sum, inventoryHandler);
                }

                break;
            }
        }
        return sum;

    }

    public static int removeItem(Map<Integer, Integer> map, int sum, InventoryHandler inventoryHandler){

//        InventoryHandler inventoryHandler = be.getStorageWrapper().getInventoryHandler();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            int slot = entry.getKey();
            int num1 = entry.getValue();

            if (num1 >= sum) {
                ItemStack itemStack = inventoryHandler.getSlotStack(slot);
                itemStack.setCount(num1-sum);
//            InventoryHandler.setSlotStack(slot, itemStack);
                sum = 0;
                break;
            } else {
                inventoryHandler.setStackInSlot(slot,ItemStack.EMPTY);
                sum = sum - num1;
            }
        }
        return sum;
    }

//public static int removeItem(Map<Integer, Integer> map, int sum, WoodStorageBlockEntity be){
//
//    InventoryHandler inventoryHandler = be.getStorageWrapper().getInventoryHandler();
//    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
//        int slot = entry.getKey();
//        int num1 = entry.getValue();
//
//        if (num1 >= sum) {
//            ItemStack itemStack = inventoryHandler.getSlotStack(slot);
//            itemStack.setCount(num1-sum);
////            InventoryHandler.setSlotStack(slot, itemStack);
//            sum = 0;
//            break;
//        } else {
//            inventoryHandler.setStackInSlot(slot,ItemStack.EMPTY);
//            sum = sum - num1;
//        }
//    }
//    return sum;
//}

    public static int removeBindPosItem(Map<Integer, Integer> map, Player player, int sum){
        if(map.isEmpty()) return sum;

        BlockPos pos = Util.getPMD(player).getInvPos();
        Level level = player.getLevel();
        if(pos==null) return sum;

        BlockEntity be =  level.getBlockEntity(pos);
        Block block =  level.getBlockState(pos).getBlock();
        // 原版箱子、木桶
        if(block instanceof ChestBlock || block instanceof BarrelBlock){
            LazyOptional<IItemHandler> lazyItemHandler = be.getCapability(ForgeCapabilities.ITEM_HANDLER);
            if(lazyItemHandler.isPresent()) {
                IItemHandler itemHandler1 = lazyItemHandler.orElse(null);
                for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                    int slot = entry.getKey();
                    int num1 = entry.getValue();

                    if (num1 >= sum) {
                        itemHandler1.extractItem(slot, sum, false);
                        sum = 0;
                        break;
                    } else {
                        itemHandler1.extractItem(slot, num1, false);
                        sum = sum - num1;
                    }
                }
            }
            return sum;
        }else if(Util.isLoadSopStorage()) {//精妙存储
            if(be instanceof WoodStorageBlockEntity blockEntity){
                InventoryHandler inventoryHandler = blockEntity.getStorageWrapper().getInventoryHandler();
                sum = removeItem(map, sum, inventoryHandler);
            }
            return sum;
        }

        return sum;
    }
}
