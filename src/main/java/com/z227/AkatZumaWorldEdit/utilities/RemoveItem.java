package com.z227.AkatZumaWorldEdit.utilities;

import net.minecraft.core.BlockPos;
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
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedstorage.block.WoodStorageBlockEntity;

import java.util.Map;

public class RemoveItem {

    public static int removePlayerItem(Map<Integer, Integer> map, Player player, int sum){
//        if(map.isEmpty()) return sum;
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

public static int removeItem(Map<Integer, Integer> map, int sum, WoodStorageBlockEntity be){

    InventoryHandler inventoryHandler = be.getStorageWrapper().getInventoryHandler();
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

    public static int removeBindPosItem(Map<Integer, Integer> map, Player player, int sum){
//        if(map.isEmpty()) return sum;

        BlockPos pos = Util.getPMD(player).getInvPos();
        Level level = player.level();
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
        }

        //精妙存储
        if(Util.isLoadSopStorage()) {
            if(be instanceof WoodStorageBlockEntity blockEntity){
                sum = removeItem(map, sum, blockEntity);
            }
            return sum;
        }

        return sum;
    }
}
