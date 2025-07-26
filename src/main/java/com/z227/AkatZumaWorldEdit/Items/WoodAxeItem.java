package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.PlaceBlock;
import com.z227.AkatZumaWorldEdit.Render.renderLine.RenderLineBox;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
        pTooltipComponents.add( Component.translatable("item.wood_axe.desc1"));
        pTooltipComponents.add( Component.translatable("item.wood_axe.desc2"));
        pTooltipComponents.add( Component.translatable("item.wood_axe.desc3"));
        pTooltipComponents.add( Component.translatable("item.wood_axe.desc4"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    }


    //右键空气
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//        if(pLevel.isClientSide)return super.use(pLevel, pPlayer, pUsedHand);

        BlockPos blockPos2 =BlockPos.containing(pPlayer.getEyePosition());
        clickPos(pLevel,blockPos2, pPlayer,false );
        if(pLevel.isClientSide()){
            RenderLineBox.updateVertexBuffer();
        }
        return super.use(pLevel, pPlayer, pUsedHand);

    }

    //@param bool true左键，false右键
    public static boolean clickPos(Level leve, BlockPos pos,Player player, boolean bool) {
        //超出最低高度
        if(!player.hasPermissions(2) && pos.getY() < Config.LOWHeight.get()){
            if(player.isLocalPlayer()){return false;}
                Component component = Component.translatable("chat.item.wood_axe.left_error");
                Component msg = Component.literal(Config.LOWHeight.get().toString()).withStyle(ChatFormatting.RED);
                AkatZumaWorldEdit.sendAkatMessage(component,msg, player);

            return false;
        }
        //禁止选中基岩
        Block block = leve.getBlockState(pos).getBlock();
        if(block == Blocks.BEDROCK){
            Component component = Component.literal(" ")
                    .append(block.getName()).withStyle(ChatFormatting.GREEN)
                    .append(Component.translatable(("chat.akatzuma.error.black_list")));
            AkatZumaWorldEdit.sendAkatMessage(component, player);
            return false;
        }

        Map<UUID, PlayerMapData> AWE =  AkatZumaWorldEdit.PlayerWEMap;
        //isFlag
        if(!AWE.get(player.getUUID()).isFlag()){
            return false;
        }

        PlayerMapData pwm = AWE.get(player.getUUID());
        if(pwm == null){
            AWE.put(player.getUUID(), new PlayerMapData());
        }

        if(bool)pwm.setPos1(pos);
        else pwm.setPos2(pos);


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
        msg = msg.replaceFirst("^BlockPos", "§5");


        if(player.isLocalPlayer())return false;
        Util.logInfo(player,component.getString()+msg);

            if(pos2!=null){
                Vec3i vec3 = PlaceBlock.calculateCubeDimensions(pos, pos2);
                size =  vec3.getX()*vec3.getY()*vec3.getZ();
                msg = String.format("%s §5(%s)", msg, size);
            }

            AkatZumaWorldEdit.sendAkatMessage(component, msg, player);


        return true;
    }

    //左键
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
//        clickPos(pos, player,true );

        return true;
    }

    //右键方块
    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();

        BlockPos blockPos2 = context.getClickedPos();
//        NetworkingHandle.INSTANCE.sendToServer(new C2SPos2(blockPos2));
        clickPos(level,blockPos2,context.getPlayer(), false );
        if(level.isClientSide()){
            RenderLineBox.updateVertexBuffer();
        }

        return InteractionResult.SUCCESS;
    }

}
