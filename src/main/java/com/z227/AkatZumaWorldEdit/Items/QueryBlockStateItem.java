package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
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
        pTooltipComponents.add( Component.translatable("item.QueryBlockStateItem.desc2"));
        pTooltipComponents.add( Component.translatable("item.QueryBlockStateItem.desc3"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    }




    //左键
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
//        player.getViewVector()


        if(player.isLocalPlayer()){
            Level world = player.level();
//            BlockPos blockPos =  pos;
            BlockState blockState = world.getBlockState(pos);
            String blockStateStr = blockState.toString().replaceFirst("}", "")
                    .replaceFirst("^Block\\{", "");

            Component component = blockState.getBlock().getName().append(Component.literal(": "));
            Component copy = SendCopyMessage.send(blockStateStr);
//            Component component = Component.translatable("chat.item.query_block_state.right");
            AkatZumaWorldEdit.sendAkatMessage(component, copy, player);


        }
        return true;
    }

    //这在使用item时，在激活block之前调用。
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {


        Vec3i vec3i = context.getClickedFace().getNormal();     //Vec3i{x=0, y=0, z=-1}
//        Direction.Axis axis = context.getClickedFace().getAxis();       //axis= z  or  axis= x
//        int  StepZ = context.getClickedFace().getStepZ();       //StepZ= -1  or StepZ= 1
        System.out.println("vec3i= "+ vec3i);
//        System.out.println("axis= "+ axis);
//        System.out.println("StepZ= "+ StepZ);
        Level world =  context.getLevel();
//        BlockState blockState = world.getBlockState(context.getClickedPos());
        if(world.isClientSide){
            String it = stack.getItem().toString();
            String des = stack.getItem().getDescriptionId();
            String item = stack.getItem().getName(stack).toString();
            String did =  stack.getDescriptionId();
            String hn = stack.getHoverName().toString();

//        String tag = stack.getTag().toString();
            System.out.printf("""
                    did= %s
                    hn= %s
                    it= %s
                    des = %s
                    item= %s
                                
                    %n""", did, hn, it,des, item);
        }


        return InteractionResult.SUCCESS;
    }
}
