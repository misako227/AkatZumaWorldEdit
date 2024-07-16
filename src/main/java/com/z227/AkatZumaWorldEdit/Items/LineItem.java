//package com.z227.AkatZumaWorldEdit.Items;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.TooltipFlag;
//import net.minecraft.world.item.context.UseOnContext;
//import net.minecraft.world.level.Level;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.List;
//
//public class LineItem extends Item {
//    public LineItem(Item.Properties pProperties)
//    {
//        super(pProperties);
//    }
//
//    @Override
//    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
////        pTooltipComponents.add( Component.translatable("item.line_item.desc1"));
//
//
//        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
//
//    }
//
//    //左键
//    @Override
//    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
//        return true;
//    }
//
//
//
//
//    @Override
//    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
//
//
//
//        return InteractionResult.SUCCESS;
//    }
//}
