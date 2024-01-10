package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlaceBlock;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
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


    //右键空气
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pLevel.isClientSide)return super.use(pLevel, pPlayer, pUsedHand);


        return super.use(pLevel, pPlayer, pUsedHand);

    }

    //左键
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {

        AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID()).setPos1(pos);

        //判断是客户端
        if(player.isLocalPlayer()){
            player.sendSystemMessage(Component.literal("[")
                    .append(AkatZumaWorldEdit.Akat)
                    .append("左键选择了位置" + pos.toString())
            );
        }


        return true;
    }

    //这在使用item时，在激活block之前调用。
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        BlockPos blockPos2 = context.getClickedPos();

        PlayerMapData pwm = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());


        pwm.setPos2(blockPos2);

        if(world.isClientSide || player == null) {

            player.sendSystemMessage(Component.literal("[")
                    .append(AkatZumaWorldEdit.Akat)
                    .append("右键选择了位置" + blockPos2.toString())
            );

            Vec3i vec3 = PlaceBlock.calculateCubeDimensions(pwm.getPos1(),pwm.getPos2());
            System.out.println("长=" + vec3.getX() + " 高="+ vec3.getY() +" 宽="+ vec3.getZ());
            System.out.println("方块数量="+ vec3.getX()*vec3.getY()*vec3.getZ());
        }



        return InteractionResult.SUCCESS;
    }

//    @Override
//    public InteractionResult useOn(UseOnContext pContext) {
//        Level world = pContext.getLevel();
//        Player player = pContext.getPlayer();
//        if(world.isClientSide || player == null) return InteractionResult.FAIL;
//
//        id.setTestData();
//        player.sendSystemMessage(Component.literal("设置id="+ id.getTestData() ));
////
////        BlockPos blockPos = pContext.getClickedPos();
////        BlockState airBlock = Blocks.AIR.defaultBlockState();
//
////        BlockPos bp = blockPos;
////        for ( var i=0; i<5; i++){
////            bp = bp.offset(1, 0, 0);
////            if(!isPlaceBlock(world,player,bp,airBlock)){
////                player.sendSystemMessage(Component.literal("位置:"+ bp.toString() + "替换失败！！！"));
////                return InteractionResult.FAIL;
////            }
////        }
//////        player.sendSystemMessage(Component.literal("位置2:"+ blockPos.toString() + "已替换"));
////        BlockPos bp2 = blockPos;
////        player.sendSystemMessage(Component.literal("位置2:"+ bp2.toString() + "已替换"));
////        for ( var i=0; i<5; i++){
////            bp2 = bp2.offset(1, 0, 0);
////            player.sendSystemMessage(Component.literal("位置:"+ bp2.toString() + "已替换"));
////            world.setBlockAndUpdate(bp2, airBlock);
////
////        }
//
//
//
//        return InteractionResult.PASS;
//    }




}
