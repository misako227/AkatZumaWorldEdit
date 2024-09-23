package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.LineBase;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.LineItemEvent;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.lineItemPacket.C2SPlaceCurvePacket;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.player.LocalPlayer;
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

public class LineItem extends Item {
    public LineItem(Item.Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
//        pTooltipComponents.add( Component.translatable("item.line_item.desc1"));


        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    }

    //左键方块
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return true;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if(pLevel.isClientSide()){
            PlayerMapData PMD = Util.getPMD(pPlayer);
            LineBase lineBase = PMD.getLineBase();
            if(Util.isDownCtrl() && Util.isDownLAlt()){
                lineBase.delAllPos();
                return super.use(pLevel, pPlayer, pUsedHand);
            }



            if(Util.isDownLAlt()){
                lineBase.delPos();
                return super.use(pLevel, pPlayer, pUsedHand);
            }
            if(Util.isDownCtrl()){
                List<BlockPos> posList = lineBase.getPosList();
                if(posList.size() > 2){
                    NetworkingHandle.INSTANCE.sendToServer(new C2SPlaceCurvePacket(PMD.getQueryBlockState(), posList));
                }else{
                    Component component = Component.translatable("chat.akatzuma.error.line_pos_inadequate");
                    AkatZumaWorldEdit.sendAkatMessage(component, pPlayer);
                }

                return super.use(pLevel, pPlayer, pUsedHand);
            }

            LineItemEvent.onItemRightAir((LocalPlayer) pPlayer, lineBase);
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }


    //右键方块
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level pLevel = context.getLevel();

        if(pLevel.isClientSide()){
            Player pPlayer = context.getPlayer();
            PlayerMapData PMD = Util.getPMD(pPlayer);
            LineBase lineBase = PMD.getLineBase();
            if(Util.isDownCtrl()){
                lineBase.delPos();
                return InteractionResult.SUCCESS;
            }

            LineItemEvent.onItemRightAir((LocalPlayer) pPlayer, lineBase);

//            lineBase.get123();
        }

        return InteractionResult.SUCCESS;
    }
}
