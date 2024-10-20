package com.z227.AkatZumaWorldEdit.Event;

import com.mojang.blaze3d.platform.InputConstants;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Commands.brush.BrushBase;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Core.modifyBlock.shape.LineItemEvent;
import com.z227.AkatZumaWorldEdit.Items.LineItem;
import com.z227.AkatZumaWorldEdit.Items.ProjectorItem;
import com.z227.AkatZumaWorldEdit.Items.QueryBlockStateItem;
import com.z227.AkatZumaWorldEdit.Items.WoodAxeItem;
import com.z227.AkatZumaWorldEdit.Render.RenderLineBox;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.brushPacket.C2SUseBrush;
import com.z227.AkatZumaWorldEdit.utilities.PlayerUtil;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Map;


@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
@OnlyIn(Dist.CLIENT)
public class ClientEventRegister {
    static long lastTime = -1;

    //client 命令
//    @SubscribeEvent
//    public static void onServerStarting(RegisterClientCommandsEvent event) {
//        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
//        CommandBuildContext commandbuildcontext = Commands.createValidationContext(VanillaRegistries.createLookup());
//
//        PosClientCommand.register(dispatcher,commandbuildcontext);
//
//    }

    public static final KeyMapping UNDO_KEY = new KeyMapping("key.category.undo",
            KeyConflictContext.IN_GAME,
            KeyModifier.CONTROL,
            InputConstants.Type.KEYSYM,
            85,
            "key.akatzuma");


//    @SubscribeEvent
//    public static void leftClickAir(TickEvent.ClientTickEvent event) {
//        System.out.println(Minecraft.getInstance().options.keyAttack.isDown());
//    }


    //左键单击空气
    @SubscribeEvent
    public static void leftClickAir(PlayerInteractEvent.LeftClickEmpty event) {

        ItemStack itemStack =  event.getItemStack();
        Item item = itemStack.getItem();
        if(AkatZumaWorldEdit.USEITEM.get(item) == null)return;
        LocalPlayer player = (LocalPlayer) event.getEntity();

        if (player != null) {
            PlayerMapData PMD = Util.getPMD(player);
            if(item instanceof WoodAxeItem ){
                SendCopyMessage.sendCommand("a pos1");

                return;
            }
            if(item instanceof ProjectorItem){
                SendCopyMessage.sendCommand("a copy");
                return;
            }
            //查询空气
            if(item instanceof QueryBlockStateItem){

//                NetworkingHandle.INSTANCE.sendToServer(new SendToServer(0));
                BlockState blockState = Blocks.AIR.defaultBlockState();
                if(Util.isDownCtrl()){
                    PMD.setReplaceBlockState(blockState);
                }else{
                    PMD.setQueryBlockState(blockState);
                }

                String blockStateStr = blockState.toString().replaceFirst("}", "")
                        .replaceFirst("^Block\\{", "");

                Component component = blockState.getBlock().getName().append(Component.literal(": "));
                Component copy = SendCopyMessage.send(blockStateStr);
                AkatZumaWorldEdit.sendAkatMessage(component, copy, player);
                return;
            }

            if(item instanceof LineItem){
                if(Util.isDownCtrl()){
                    LineItemEvent.onItemCtrlLeftAir(player, PMD.getLineBase());
                    return;
                }

                LineItemEvent.onItemLeftAir(player, PMD.getLineBase());
                return;
            }



        }
    }






    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.Key event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null)return;

        if (UNDO_KEY.consumeClick()) {
            if(lastTime == -1){
                SendCopyMessage.sendCommand("a undo");
                lastTime=System.currentTimeMillis();
                return;
            }
            if(System.currentTimeMillis()-lastTime > 1000){
                SendCopyMessage.sendCommand("a undo");
                lastTime=System.currentTimeMillis();
            }else{
                LocalPlayer Lplayer = Minecraft.getInstance().player;
                if(Lplayer!=null){
                    Component component = Component.translatable("chat.akatzuma.error.too_fast");
                    AkatZumaWorldEdit.sendAkatMessage(component,Lplayer);
                }
            }
            return;
        }

        ItemStack itemstack = player.getMainHandItem();
        Item item = itemstack.getItem();
        if(item instanceof WoodAxeItem){
            sendScrollEndPos(player,event);
        }


    }



    @SubscribeEvent
    public static void InputEventKey(InputEvent.MouseScrollingEvent event) {

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null)return;

        ItemStack itemstack = player.getMainHandItem();
        Item item = itemstack.getItem();
        if(item instanceof QueryBlockStateItem){
            scrollQuery(player, event);
            return;
        }

        if(item instanceof WoodAxeItem){
            scrollAddPos(player, event);
        }
    }

    @SubscribeEvent
    public static void RightClickAir(PlayerInteractEvent.RightClickEmpty event) {
        Player player = event.getEntity();
//        ItemStack itemstack = player.getMainHandItem();
//        Item item = itemstack.getItem();
//        PlayerMapData PMD = Util.getPMD(player);
//
//        if(item instanceof LineItem){
//            LineItemEvent.onItemRightAir(event, (LocalPlayer) player, PMD);
//            return;
//        }

        brushEvent(player);
    }

    @SubscribeEvent
    public static void RightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if(event.getLevel().isClientSide()){
            brushEvent(event.getEntity());
        }
    }


    public static void brushEvent(Player player) {

//        Player player = event.getEntity();
//        if (player == null)return;

        ItemStack itemstack = player.getMainHandItem();
        Item item = itemstack.getItem();
        PlayerMapData PMD = Util.getPMD(player);
        Map<Item, BrushBase> brushMap = PMD.getBrushMap();
        if (brushMap.isEmpty()) return;
        if (brushMap.get(item) == null) return;

        BlockHitResult blockHitResult = PlayerUtil.getPlayerPOVHitResult(player,120);
        BlockPos blockPos = blockHitResult.getBlockPos();
        if(player.getLevel().getBlockState(blockPos).isAir()) return;
        NetworkingHandle.INSTANCE.sendToServer(new C2SUseBrush(blockPos));
}




    public static void sendScrollEndPos(LocalPlayer player, InputEvent.Key event) {
        PlayerMapData PMD = Util.getPMD(player);

        if(event.getKey() == GLFW.GLFW_KEY_LEFT_CONTROL){
            BlockPos pos1 = PMD.getPos1();
            if(event.getAction() == GLFW.GLFW_PRESS){
                PMD.setTempPos(pos1);
            }
            if(event.getAction() == GLFW.GLFW_RELEASE){
                if(pos1 == null || !PMD.isUpdatePos()) return;
                SendCopyMessage.sendCommand("a pos1 " + Util.getPos(pos1));
//                RenderLineBox.updateVertexBuffer();
                PMD.setUpdatePos(false);
                return;
            }

        }
        if(event.getKey() == GLFW.GLFW_KEY_LEFT_ALT){
            BlockPos pos2 = PMD.getPos2();
            if(event.getAction() == GLFW.GLFW_PRESS){
                PMD.setTempPos(pos2);
            }
            if(event.getAction() == GLFW.GLFW_RELEASE){
                if(pos2 == null || !PMD.isUpdatePos()) return;
                SendCopyMessage.sendCommand("a pos2 " + Util.getPos(pos2));
//                RenderLineBox.updateVertexBuffer();
                PMD.setUpdatePos(false);
                return;
            }

        }
    }

    public static void scrollQuery(LocalPlayer player, InputEvent.MouseScrollingEvent event){
        if(Util.isDownCtrl() && Util.isDownLAlt()){
            PlayerMapData PMD = Util.getPMD(player);
            BlockState blockState1 = PMD.getQueryBlockState();
            BlockState blockState2 = PMD.getReplaceBlockState();
            //互换
            PMD.setQueryBlockState(blockState2);
            PMD.setReplaceBlockState(blockState1);
            event.setCanceled(true);
            return;
        }

        //切换查询模式
        if(Util.isDownCtrl()){
            PlayerMapData PMD = Util.getPMD(player);
            PMD.setQueryFlag();
            event.setCanceled(true);
        }
    }

    public static void scrollAddPos(LocalPlayer player, InputEvent.MouseScrollingEvent event){
        PlayerMapData PMD = Util.getPMD(player);
        if(Util.isDownCtrl()){
            BlockPos pos1 = PMD.getPos1();
            if(pos1 == null) return;
            PMD.setPos1(addPos(pos1, event.getScrollDelta(), player));
            PMD.setUpdatePos(true);
            RenderLineBox.updateVertexBuffer();
            event.setCanceled(true);
            return;
        }
        if(Util.isDownLAlt()){
            BlockPos pos2 = PMD.getPos2();
            if(pos2 == null) return;
            PMD.setPos2(addPos(pos2, event.getScrollDelta(), player));
            PMD.setUpdatePos(true);
            RenderLineBox.updateVertexBuffer();
            event.setCanceled(true);
        }
    }


    public static BlockPos addPos(BlockPos pos, double scroll, LocalPlayer player) {
        Vec3i playerNormal = PlayerUtil.getPlayerNormal(player);
        if(scroll == 1){
            return pos.offset(playerNormal);
        }else{
            return pos.offset(-playerNormal.getX(), -playerNormal.getY(), -playerNormal.getZ());
        }

    }







}
