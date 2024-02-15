package com.z227.AkatZumaWorldEdit.Event;

import com.mojang.blaze3d.platform.InputConstants;
import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Items.ProjectorItem;
import com.z227.AkatZumaWorldEdit.Items.WoodAxeItem;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void leftClickAir(PlayerInteractEvent.LeftClickEmpty event) {

        ItemStack itemStack =  event.getItemStack();
        Item item = itemStack.getItem();
        if(AkatZumaWorldEdit.USEITEM.get(item) == null)return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            if(item instanceof WoodAxeItem ){
                player.connection.sendCommand("a pos1");
            }
            if(item instanceof ProjectorItem){
                player.connection.sendCommand("a copy");
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
//    public static void InputEventKey(PlayerInteractEvent.RightClickEmpty event) {
//        Minecraft mc = Minecraft.getInstance();
//        LocalPlayer player = mc.player;
//        if(player==null)return;
//        Item item = event.getEntity().getMainHandItem().getItem();
//        if(!AkatZumaWorldEdit.USEITEM.containsKey(item))return;
//
//
////        player.connection.sendCommand("a line " + state);
//        if(InputEventClient.isDownKey(341)){
//            if(item instanceof LineItem){
//                setLine(player);
//                return;
//            }
//        }
//
//    }
//
//    public static void setLine(LocalPlayer player){
//        PlayerMapData PMD = Util.getPMD(player);
//        BlockState state =  PMD.getQueryBlockState();
//        if (PMD.getQueryBlockState() == null) {
//            Component component = Component.translatable("chat.item.query_block_state.null");
//            AkatZumaWorldEdit.sendAkatMessage(component, player);
//            return;
//        }
//        String blockName = BlockStateString.getStateName(state);
//        player.connection.sendCommand("a line " + blockName);
//    }



}
