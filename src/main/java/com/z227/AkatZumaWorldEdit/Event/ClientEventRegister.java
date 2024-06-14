package com.z227.AkatZumaWorldEdit.Event;

import com.mojang.blaze3d.platform.InputConstants;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Items.ProjectorItem;
import com.z227.AkatZumaWorldEdit.Items.QueryBlockStateItem;
import com.z227.AkatZumaWorldEdit.Items.WoodAxeItem;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.SendToServer;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


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




    //左键单击空气
    @SubscribeEvent
    public static void leftClickAir(PlayerInteractEvent.LeftClickEmpty event) {

        ItemStack itemStack =  event.getItemStack();
        Item item = itemStack.getItem();
        if(AkatZumaWorldEdit.USEITEM.get(item) == null)return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if(item instanceof WoodAxeItem ){

                SendCopyMessage.sendCommand("a pos1");

                return;
            }
            if(item instanceof ProjectorItem){
//                player.connection.sendCommand("a copy");
                SendCopyMessage.sendCommand("a copy");
                return;
            }
            //查询空气
            if(item instanceof QueryBlockStateItem){
                PlayerMapData PMD = Util.getPMD(player);
                NetworkingHandle.INSTANCE.sendToServer(new SendToServer(0));
                BlockState blockState = Blocks.AIR.defaultBlockState();
                PMD.setQueryBlockState(blockState);
                String blockStateStr = blockState.toString().replaceFirst("}", "")
                        .replaceFirst("^Block\\{", "");

                Component component = blockState.getBlock().getName().append(Component.literal(": "));
                Component copy = SendCopyMessage.send(blockStateStr);
                AkatZumaWorldEdit.sendAkatMessage(component, copy, player);
                return;
            }

        }
    }






    @SubscribeEvent
    public static void onKeyboardInput(InputEvent.Key event) {
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

        }

    }

//    @SubscribeEvent
//    public static void InputEventKey(InputEvent.MouseScrollingEvent event) {
//
//        LocalPlayer player = Minecraft.getInstance().player;
//        if (player == null)return;
//
//        ItemStack itemstack = player.getMainHandItem();
//        if(itemstack.getItem() != AkatZumaWorldEdit.BindInventory.get())return;
//
//        if(Util.isDownCtrl()){
//            PlayerMapData PMD = Util.getPMD(player);
//            if(event.getScrollDelta() == -1.0){
//                PMD.addInvPosIndex();
//            }else{
//                PMD.deInvPosIndex();
//            }
//            event.setCanceled(true);
////            System.out.println("ctrl");
//        }
//    }





}
