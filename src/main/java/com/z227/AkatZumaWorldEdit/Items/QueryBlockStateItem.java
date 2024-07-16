package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import com.z227.AkatZumaWorldEdit.utilities.Util;
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

public class QueryBlockStateItem extends Item{

    public QueryBlockStateItem(Item.Properties pProperties) {
        super(pProperties);
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add( Component.translatable("item.query_block_state.desc1"));
        pTooltipComponents.add( Component.translatable("item.query_block_state.desc2"));
        pTooltipComponents.add( Component.translatable("item.query_block_state.desc3"));
        pTooltipComponents.add( Component.translatable("item.query_block_state.desc4"));
        pTooltipComponents.add( Component.translatable("item.query_block_state.desc5"));
        pTooltipComponents.add( Component.translatable("item.query_block_state.desc6"));

//        if(pLevel!=null && pLevel.isClientSide) Util.addBlockStateText(pTooltipComponents);

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }




    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pLevel.isClientSide){
            if(pPlayer.getCooldowns().isOnCooldown(this)){
                return super.use(pLevel, pPlayer, pUsedHand);
            }
            PlayerMapData PMD = Util.getPMD(pPlayer);
            pPlayer.getCooldowns().addCooldown(this, 5);
            if(Util.isDownCtrl()){

//                Component component;
//                if (PMD.getQueryBlockState() == null) {
//                    component = Component.translatable("chat.item.query_block_state.null");
//                    AkatZumaWorldEdit.sendAkatMessage(component, pPlayer);
//                    return super.use(pLevel, pPlayer, pUsedHand);
//                }

                String blockName = BlockStateString.getStateName(PMD.getQueryBlockState());
                if(PMD.getQueryFlag() == 1){
                    SendCopyMessage.sendCommand("a set "+ blockName);
                }else{
                    String blockName2 = BlockStateString.getStateName(PMD.getReplaceBlockState());
                    SendCopyMessage.sendCommand("a replace "+ blockName + " " + blockName2);
                }
                return  super.use(pLevel, pPlayer, pUsedHand);
            }


        }
        return  super.use(pLevel, pPlayer, pUsedHand);
    }

    //左键
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {

        return true;
    }

    //这在使用item时，在激活block之前调用。
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {


        Level world =  context.getLevel();
        Player player = context.getPlayer();


//        Component component;

        if(world.isClientSide){

            if(player.getCooldowns().isOnCooldown(this)){
                return InteractionResult.SUCCESS;
            }

            BlockPos placePos = context.getClickedPos().relative(context.getClickedFace());
            PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());

            player.getCooldowns().addCooldown(this, 5);
            if(Util.isDownCtrl()){


//                if (PMD.getQueryBlockState() == null) {
//                    component = Component.translatable("chat.item.query_block_state.null");
//                    AkatZumaWorldEdit.sendAkatMessage(component, player);
//                    return InteractionResult.SUCCESS;
//                }

                String blockName = BlockStateString.getStateName(PMD.getQueryBlockState());
                if(PMD.getQueryFlag() == 1){
                    SendCopyMessage.sendCommand("a set "+ blockName);
                }else{
                    String blockName2 = BlockStateString.getStateName(PMD.getReplaceBlockState());
                    SendCopyMessage.sendCommand("a replace "+ blockName + " " + blockName2);
                }

                return InteractionResult.SUCCESS;
            }else{
                String blockName;
                BlockPos blockPos;
                if(PMD.getQueryFlag() == 1){
                    blockPos = placePos;
                    blockName = BlockStateString.getStateName(PMD.getQueryBlockState());
                }else{
                    blockPos = context.getClickedPos();
                    blockName = BlockStateString.getStateName(PMD.getReplaceBlockState());
                }
                SendCopyMessage.sendCommand("a other set " + PMD.getQueryFlag() +" " + blockName + " " + Util.getPos(blockPos));
            }


            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }


}
