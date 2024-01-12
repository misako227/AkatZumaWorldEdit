package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
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
//        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
////        VertexConsumer builder = buffer.getBuffer(OurRenderTypes.lines());
//        VertexConsumer lineBuilder = buffer.getBuffer(RenderType.LINES);
//        float colorR = 0, colorG = 1, colorB = 0;
//
//        AABB aabb = new AABB(pos);
//        LevelRenderer.renderLineBox(new PoseStack(),lineBuilder, aabb, colorR, colorG, colorB, 0.5f);
//
        return true;
    }

    //这在使用item时，在激活block之前调用。
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        BlockPos blockPos =  context.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);
        if(player.isLocalPlayer()){
            String msg = "value=" + Config.VALUE.get().toString();
            AkatZumaWorldEdit.sendAkatMessage(msg,player);
            String msg2 = blockState.toString();
//            blockState.g
            AkatZumaWorldEdit.sendAkatMessage(msg2 ,player);
//            Config.whiteListBlock.get().


        }




        return InteractionResult.SUCCESS;
    }
}
