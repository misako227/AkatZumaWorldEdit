package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.PlaceBlock;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WoodAxeItem extends Item {

    public WoodAxeItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add( Component.translatable("item.test_item.desc1"));
        pTooltipComponents.add( Component.translatable("item.test_item.desc2"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    }


    //右键空气
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//        if(pLevel.isClientSide)return super.use(pLevel, pPlayer, pUsedHand);
//
//        return super.use(pLevel, pPlayer, pUsedHand);
//
//    }

    //@param bool true左键，false右键
    public void clickPos(BlockPos pos,Player player, boolean bool) {
        //超出最低高度
        if(pos.getY() < Config.LOWHight.get()){
            if(player.isLocalPlayer()){
                Component component = Component.translatable("chat.item.wood_axe.left_error");
                Component msg = Component.literal(Config.LOWHight.get().toString()).withStyle(ChatFormatting.RED);
                AkatZumaWorldEdit.sendAkatMessage(component,msg, player);
            }
            return;
        }

        Map<UUID, PlayerMapData> AWE =  AkatZumaWorldEdit.PlayerWEMap;
        //isFlag
        if(!AWE.get(player.getUUID()).isFlag()){
            return;
        }

        if(AWE.get(player.getUUID()) == null){
            AWE.put(player.getUUID(), new PlayerMapData(player.getName().getString()));
        }

        PlayerMapData pwm = AWE.get(player.getUUID());
        if(bool)pwm.setPos1(pos);
        else pwm.setPos2(pos);

        //判断是客户端
        if(player.isLocalPlayer()){
            BlockPos pos2 = null; int size;
            Component component;
            //判断pos2存不存在
            if(bool){
                component = Component.translatable("chat.item.wood_axe.left");
                if (pwm.getPos2() !=null) pos2 = pwm.getPos2();
            }else{
                component = Component.translatable("chat.item.wood_axe.right");
                if (pwm.getPos1() !=null) pos2 = pwm.getPos1();
            }



            String msg = pos.toString().replaceFirst("^MutableBlockPos", "§5");

            if(pos2!=null){
                Vec3i vec3 = PlaceBlock.calculateCubeDimensions(pos, pos2);
                size =  vec3.getX()*vec3.getY()*vec3.getZ();
                msg = String.format("%s §5(%s)", msg, size);
            }
            Minecraft minecraft = Minecraft.getInstance();
            AkatZumaWorldEdit.sendAkatMessage(component, msg, player);

        }
    }

    //左键
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        clickPos(pos, player,true );
        return true;
    }

    //这在使用item时，在激活block之前调用。
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {

        BlockPos blockPos2 = context.getClickedPos();
        Player player = context.getPlayer();
//        Level world = context.getLevel();
        clickPos(blockPos2, player,false );



        return InteractionResult.SUCCESS;
    }

}
