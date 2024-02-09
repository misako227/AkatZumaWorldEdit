package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ProjectorItem extends Item
{
    public ProjectorItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add( Component.translatable("item.projector_item.desc1"));
        pTooltipComponents.add( Component.translatable("item.projector_item.desc2"));
        pTooltipComponents.add( Component.translatable("item.projector_item.desc3"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    }

    //左键
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if(pLevel.isClientSide){
            SendCopyMessage.sendCommand("a paste");
        }



        return  super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        if(context.getLevel().isClientSide){
            SendCopyMessage.sendCommand("a paste");
        }

        return InteractionResult.SUCCESS;
    }


}
