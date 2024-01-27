package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class QueryBlockStateItem extends Item{

    public QueryBlockStateItem(Item.Properties pProperties) {
        super(pProperties);
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add( Component.translatable("item.QueryBlockStateItem.desc1"));
        pTooltipComponents.add( Component.translatable("item.QueryBlockStateItem.desc2"));
        pTooltipComponents.add( Component.translatable("item.QueryBlockStateItem.desc3"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    //左键
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {


//        Level world = player.level();
//        BlockState blockState = world.getBlockState(pos);
//        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
//        PMD.setQueryBlockState(blockState);
//        if(player.isLocalPlayer()){
//            String blockStateStr = blockState.toString().replaceFirst("}", "")
//                    .replaceFirst("^Block\\{", "");
//
//            Component component = blockState.getBlock().getName().append(Component.literal(": "));
//            Component copy = SendCopyMessage.send(blockStateStr);
////            Component component = Component.translatable("chat.item.query_block_state.right");
//            AkatZumaWorldEdit.sendAkatMessage(component, copy, player);
//
//
//        }
//        player.getCooldowns().addCooldown(this, 10);
        return true;
    }

    //这在使用item时，在激活block之前调用。
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        BlockPos placePos = context.getClickedPos().relative(context.getClickedFace());
        Level world =  context.getLevel();
        Player player = context.getPlayer();

        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
        Component component;
        if(!world.isClientSide) {
            if (PMD.getQueryBlockState() == null) {
                component = Component.translatable("chat.item.query_block_state.null");
                AkatZumaWorldEdit.sendAkatMessage(component, player);
                return InteractionResult.SUCCESS;
            }
            //检查要放置的位置是不是空气
            if(!world.getBlockState(placePos).is(Blocks.AIR)){
                component = Component.translatable("chat.item.query_block_state.not_air");
                AkatZumaWorldEdit.sendAkatMessage(component, player);
                return InteractionResult.SUCCESS;
            }
            if (!player.hasPermissions(2)) {
                Map<String, Integer> blackWhiteMap = AkatZumaWorldEdit.defaultBlockMap;    //黑白名单方块
                MutableComponent descriptBlockName = PMD.getQueryBlockState().getBlock().getName();
                String blockName = BlockStateString.getBlockName(PMD.getQueryBlockState());
                int n = PlaceBlock.getLimit(blockName, blackWhiteMap);
                //检查黑名单
                if (!PlaceBlock.checkBlackList(player, n, descriptBlockName)) {
                    return InteractionResult.SUCCESS;
                }
                Map<Integer, Integer> blockInInvMap = PlaceBlock.checkInv(blockName, 1, 1, player, descriptBlockName);

                //检查放置权限
                if (!PlaceBlock.isPlaceBlock(world, player, placePos, PMD.getQueryBlockState())) {
                    component = Component.translatable("chat.akatzuma.error.not_permission_place");
                    AkatZumaWorldEdit.sendAkatMessage(component,player);
                    return InteractionResult.SUCCESS;
                }
                if (blockInInvMap == null) {
                    return InteractionResult.SUCCESS;
                }
                //扣除背包
                PlaceBlock.removeItemInPlayerInv(blockInInvMap, 1, 1, player);
                component = Component.translatable("chat.akatzuma.set.success")
                        .append(descriptBlockName.withStyle(ChatFormatting.GREEN));
                AkatZumaWorldEdit.sendAkatMessage(component, player);
             }

            world.setBlock(placePos, PMD.getQueryBlockState(), 16);
//            player.getCooldowns().addCooldown(this, 10);
        }
        return InteractionResult.SUCCESS;
    }
}
