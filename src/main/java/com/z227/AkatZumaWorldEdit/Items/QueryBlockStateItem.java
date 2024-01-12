package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class QueryBlockStateItem extends Item{

    public QueryBlockStateItem(Item.Properties pProperties) {
        super(pProperties);
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add( Component.translatable("item.QueryBlockStateItem.desc1"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    }




    //左键
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        UUID uuid = player.getUUID();
        PlayerMapData pwm = AkatZumaWorldEdit.PlayerWEMap.get(uuid);


//
        return false;
    }

    //这在使用item时，在激活block之前调用。
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();


        if(player.isLocalPlayer()){
            BlockPos blockPos =  context.getClickedPos();
            BlockState blockState = world.getBlockState(blockPos);
            String blockStateStr = blockState.toString().replaceFirst("}", "")
                    .replaceFirst("^Block\\{", "§a");

            Component component = Component.translatable("chat.item.query_block_state.right");
            AkatZumaWorldEdit.sendAkatMessage(component, blockStateStr, player);


        }




        return InteractionResult.SUCCESS;
    }
}
