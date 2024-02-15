//package com.z227.AkatZumaWorldEdit.Items;
//
//import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
//import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
//import com.z227.AkatZumaWorldEdit.utilities.Util;
//import net.minecraft.ChatFormatting;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.player.LocalPlayer;
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
//        if(pLevel.isClientSide){
//            LocalPlayer player =  Minecraft.getInstance().player;
//            if(player!=null) {
//                PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
//    //            if(PMD == null) return;
//                int currentType = PMD.getLineBase().getType();
//                pTooltipComponents.add(Component.translatable("item.line_item.desc_type")
//                        .append(PMD.getLineBase().getLineMap().get(currentType))
//                        .withStyle(ChatFormatting.GOLD));
//
//                Util.addBlockStateText(pTooltipComponents);
//            }
//        }
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
//        BlockPos blockPos2 = context.getClickedPos();
//        Player player = context.getPlayer();
//        Level world = context.getLevel();
//        WoodAxeItem.clickPos(world,blockPos2, player,false );
//
//
//
//        return InteractionResult.SUCCESS;
//    }
//}
