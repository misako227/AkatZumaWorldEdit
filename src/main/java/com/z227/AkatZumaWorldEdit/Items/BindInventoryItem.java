package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
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
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.p3pp3rf1y.sophisticatedstorage.block.WoodStorageBlockBase;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BindInventoryItem extends Item {

    public BindInventoryItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("hud.akatzuma.right"));
        pTooltipComponents.add(Component.translatable("hud.akatzuma.ctrl_right"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pUsedHand) {
        if(pLevel.isClientSide){
            if(player.getCooldowns().isOnCooldown(this)){
                return super.use(pLevel, player, pUsedHand);
            }
            if(Util.isDownCtrl()) {
                PlayerMapData PMD = Util.getPMD(player);
                player.getCooldowns().addCooldown(this, 40);
                SendCopyMessage.sendCommand("a other tp");
                PMD.updateInvPosMap(player);
            }
        }

        return super.use(pLevel, player, pUsedHand);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        Player player = context.getPlayer();

        if(context.getLevel().isClientSide) {

            if(player.getCooldowns().isOnCooldown(this)){
                return InteractionResult.SUCCESS;
            }

            BlockPos blockPos = context.getClickedPos();
            BlockState blockState = context.getLevel().getBlockState(blockPos);
            Block block = blockState.getBlock();
            PlayerMapData PMD = Util.getPMD(player);
            if(Util.isDownCtrl()){
                //ctrl+右键
                player.getCooldowns().addCooldown(this, 40);
                SendCopyMessage.sendCommand("a other tp");
                PMD.updateInvPosMap(player);
                return InteractionResult.SUCCESS;
            }else{
                //右键 绑定箱子
                player.getCooldowns().addCooldown(this, 20);

                if(block instanceof ChestBlock || block instanceof BarrelBlock){
                    String command = "a other bind " + getPos(blockPos);

                    SendCopyMessage.sendCommand(command);
                    PMD.setInvPosMap(blockPos,player);
                    return InteractionResult.SUCCESS;
                }else if(Util.isLoadSopStorage()){
                    if(block instanceof WoodStorageBlockBase){
                        String command = "a other bind "+ getPos(blockPos);
                        SendCopyMessage.sendCommand(command);

                        PMD.setInvPosMap(blockPos,player);
                        return InteractionResult.SUCCESS;
                    }

                }

                Component component = Component.translatable("chat.akatzuma.error.bind_pos");
                AkatZumaWorldEdit.sendAkatMessage(component,player);

                return InteractionResult.SUCCESS;

            }

        }
        return InteractionResult.SUCCESS;
    }

    public static String getPos(BlockPos pos ){
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }

}
