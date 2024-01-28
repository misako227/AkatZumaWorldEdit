package com.z227.AkatZumaWorldEdit.Items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BuildingConsumable extends Block {


    public BuildingConsumable(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add( Component.translatable("item.akatzumaworldedit.building_consumable.desc"));
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
}
