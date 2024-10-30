package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.LineBase;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.LineItemEvent;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.lineItemPacket.C2SPlaceCurvePacket;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
        pTooltipComponents.add( new TranslatableComponent("item.line_item.desc1"));
        pTooltipComponents.add( new TranslatableComponent("item.line_item.desc2"));
        pTooltipComponents.add( new TranslatableComponent("item.line_item.desc3"));
        pTooltipComponents.add( new TranslatableComponent("item.line_item.desc4"));
        pTooltipComponents.add( new TranslatableComponent("item.line_item.desc5"));
        pTooltipComponents.add( new TranslatableComponent("item.line_item.desc6"));

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
            right(pPlayer);
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }


    //右键方块
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level pLevel = context.getLevel();

        if(pLevel.isClientSide()){
            Player pPlayer = context.getPlayer();
//            PlayerMapData PMD = Util.getPMD(pPlayer);
//            LineBase lineBase = PMD.getLineBase();
            right(pPlayer);
        }

        return InteractionResult.SUCCESS;
    }

    public static void right(Player pPlayer){
        PlayerMapData PMD = Util.getPMD(pPlayer);
        LineBase lineBase = PMD.getLineBase();

        //删除所有点
        if(Util.isDownCtrl() && Util.isDownLAlt()){
            lineBase.delAllPos();
            AkatZumaWorldEdit.sendAkatMessage(new TranslatableComponent("chat.akatzuma.line.delall"), pPlayer);
            return;
        }

        //alt + 右键，删除选中的点位
        if(Util.isDownLAlt()){
            lineBase.delPos();
            return;
        }

        //ctrl + 右键，放置曲线
        if(Util.isDownCtrl()){
            List<BlockPos> posList = lineBase.getPosList();
            if(posList.size() > 2){
                NetworkingHandle.INSTANCE.sendToServer(new C2SPlaceCurvePacket(PMD.getQueryBlockState(), posList));
            }else{
                Component component = new TranslatableComponent("chat.akatzuma.error.line_pos_inadequate");
                AkatZumaWorldEdit.sendAkatMessage(component, pPlayer);
            }
            return;
        }

        //右键选中一个点位
        LineItemEvent.onItemRightAir(pPlayer, lineBase);
    }
}
