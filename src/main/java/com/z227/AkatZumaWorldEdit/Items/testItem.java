package com.z227.AkatZumaWorldEdit.Items;

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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.level.BlockEvent;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class testItem extends Item {

    public testItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add( Component.translatable("item.test_item.desc1"));
        pTooltipComponents.add( Component.translatable("item.test_item.desc2"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pLevel.isClientSide)return super.use(pLevel, pPlayer, pUsedHand);

        Vec3 vec3 = pPlayer.getViewVector(5);
        pPlayer.sendSystemMessage(Component.literal("当前位置:"+ pPlayer.getPosition(0).toString()));
        pPlayer.sendSystemMessage(Component.literal("vec3位置:"+ vec3.toString() ));
        pPlayer.setDeltaMovement(2 * vec3.x, 2* vec3.y, 2* vec3.z);

        //2*pPlayer.getViewVector(5).x,

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level world = pContext.getLevel();
        Player player = pContext.getPlayer();
        if(world.isClientSide || player == null) return InteractionResult.FAIL;

        BlockPos blockPos = pContext.getClickedPos();
        BlockState airBlock = Blocks.AIR.defaultBlockState();
        player.sendSystemMessage(Component.literal("位置1:"+ blockPos.toString() + "已替换"));

        BlockPos bp = blockPos;
        for ( var i=0; i<5; i++){
            bp = bp.offset(1, 0, 0);
            if(!isPlaceBlock(world,player,bp,airBlock)){
                player.sendSystemMessage(Component.literal("位置:"+ bp.toString() + "替换失败！！！"));
                return InteractionResult.FAIL;
            }
        }
//        player.sendSystemMessage(Component.literal("位置2:"+ blockPos.toString() + "已替换"));
        BlockPos bp2 = blockPos;
        player.sendSystemMessage(Component.literal("位置2:"+ bp2.toString() + "已替换"));
        for ( var i=0; i<5; i++){
            bp2 = bp2.offset(1, 0, 0);
            player.sendSystemMessage(Component.literal("位置:"+ bp2.toString() + "已替换"));
            world.setBlockAndUpdate(bp2, airBlock);

        }



        return InteractionResult.SUCCESS;
    }

    public Boolean isPlaceBlock(Level world, Player player, BlockPos blockPos, BlockState blockState){

        BlockSnapshot snapshot = BlockSnapshot.create(world.dimension(), world, blockPos);
        BlockEvent.EntityPlaceEvent placeEvent = new BlockEvent.EntityPlaceEvent(snapshot, blockState, player);
        MinecraftForge.EVENT_BUS.post(placeEvent);
        if(placeEvent.isCanceled()) {
            return false;
        }
        return true;
    }


}
