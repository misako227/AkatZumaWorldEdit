package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.utilities.Util;
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
import java.util.Map;

public class BindInventoryItem extends Item {

    public BindInventoryItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add( Component.translatable("item.projector_item.desc1"));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        Player player = context.getPlayer();

        if(context.getLevel().isClientSide) {
            Map<int[], BlockState> invPosMap = Util.getPMD(player).getInvPosMap();
            if(Util.isDownCtrl()){
//                LocalPlayer lplayer = Minecraft.getInstance().player;

                System.out.println(invPosMap);
                Util.getPMD(player).addInvPosIndex();
            }
//            StorageBlockEntity storageBlockEntity = (StorageBlockEntity) context.getLevel().getBlockEntity(context.getClickedPos());
//
//            StorageWrapper sw = storageBlockEntity.getStorageWrapper();
////            int slotsize = sw.getInventoryHandler().getBaseSlotLimit(); //堆叠数量
////            System.out.println(slotsize);
//            System.out.println(sw.getInventoryHandler().getSlots());//槽位
//
//            ItemStack is = sw.getInventoryHandler().getSlotStack(0);
//
//            ItemStack is2 = sw.getInventoryHandler().getSlotStack(10);
//            System.out.println();
//            System.out.println(is.getHoverName().getString()+ "数量："+is.getCount());
//            System.out.println(is2.getHoverName().getString()+ "数量："+is2.getCount());


//            LazyOptional<BindInventoryPos> bindPos = lplayer.getCapability(BindInventoryPosCapability.BIND_INV_POS_CAP);
//            bindPos.ifPresent(bp -> {
//                System.out.println(bp);
//                System.out.println(bp.getCompoundNBT());
//            });

        }
        return InteractionResult.SUCCESS;
    }
}
