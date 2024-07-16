package com.z227.AkatZumaWorldEdit.Event;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.Capability.BindInventoryPos;
import com.z227.AkatZumaWorldEdit.Capability.BindInventoryPosCapability;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Items.ProjectorItem;
import com.z227.AkatZumaWorldEdit.Items.QueryBlockStateItem;
import com.z227.AkatZumaWorldEdit.Items.WoodAxeItem;
import com.z227.AkatZumaWorldEdit.network.NetworkingHandle;
import com.z227.AkatZumaWorldEdit.network.SendToClientCompoundTag;
import com.z227.AkatZumaWorldEdit.network.posPacket.C2SPos1;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


@Mod.EventBusSubscriber(modid = AkatZumaWorldEdit.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeNetworkEvent {

    @SubscribeEvent
    public static void Login(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
//        System.out.println("登录："+ playerName);
        AkatZumaWorldEdit.PlayerWEMap.put(player.getUUID(), new PlayerMapData());

        LazyOptional<BindInventoryPos> bindPos = player.getCapability(BindInventoryPosCapability.BIND_INV_POS_CAP);
        bindPos.ifPresent(bp -> {
            NetworkingHandle.sendToClient(new SendToClientCompoundTag(bp.getCompoundNBT()), (ServerPlayer) player);
            Util.getPMD(player).setInvPosNBT(bp.getCompoundNBT(),player);
        });
//        AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID()).setVip(Util.checkVip(playerName, Config.VIPPlayerList.get()));

    }

    @SubscribeEvent
    public static void Logout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        AkatZumaWorldEdit.PlayerWEMap.remove(player.getUUID());
//        System.out.println("退出："+ player.getName().getString());


    }

    //玩家加入服务器，仅在客户端触发
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientLoginIn(ClientPlayerNetworkEvent.LoggingIn event) {
        LocalPlayer player = event.getPlayer();

//        System.out.println("登录："+ playerName);
        AkatZumaWorldEdit.PlayerWEMap.put(player.getUUID(), new PlayerMapData());
        Util.setLoadSop();
    }

    @SubscribeEvent
    public static void ServerStarted(ServerStartedEvent event){
        addTagsToMap(Config.BLACKListTags.get(), AkatZumaWorldEdit.defaultBlockMap);
        addTagsToMap(Config.VIPBLACKListTags.get(), AkatZumaWorldEdit.VipBlockMap);
        AkatZumaWorldEdit.LOGGER.info("加载黑名单标签完成");

        int defaultSize = AkatZumaWorldEdit.defaultBlockMap.size();
        int vipSize = AkatZumaWorldEdit.VipBlockMap.size();
        Util.logDebug("普通黑白名单："+AkatZumaWorldEdit.defaultBlockMap);
        Util.logDebug("vip黑白名单："+AkatZumaWorldEdit.VipBlockMap);
        AkatZumaWorldEdit.LOGGER.info("普通黑白名单总数：" + defaultSize + "，vip黑白名单总数："+ vipSize);
        Util.setLoadSop();
    }

    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player) {
            event.addCapability(new ResourceLocation(AkatZumaWorldEdit.MODID), new BindInventoryPosCapability());
        }
    }

    //玩家死亡后复制cap
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            LazyOptional<BindInventoryPos> oldSpeedCap = event.getOriginal().getCapability(BindInventoryPosCapability.BIND_INV_POS_CAP);
            LazyOptional<BindInventoryPos> newSpeedCap = event.getEntity().getCapability(BindInventoryPosCapability.BIND_INV_POS_CAP);
            if (oldSpeedCap.isPresent() && newSpeedCap.isPresent()) {
                newSpeedCap.ifPresent((newCap) -> {
                    oldSpeedCap.ifPresent((oldCap) -> {
                        newCap.deserializeNBT(oldCap.serializeNBT());
                    });
                });
            }
        }
    }


    //取消玩家在空中挖掘时的减速
    @SubscribeEvent (priority = EventPriority.LOW)
    public static void handleBreakSpeedEvent(PlayerEvent.BreakSpeed event) {

        if (event.isCanceled()) {
            return;
        }
        Player player = event.getEntity();
        if (!player.onGround()) {
            event.setNewSpeed(Math.max(event.getNewSpeed(), event.getOriginalSpeed() * 5.0F));
        }
    }




    public static void addTagsToMap(List<? extends String> input, Map output) {
        input.forEach(k ->{
            Matcher tagName = BlockStateString.findBlackBlockName(k);
            if(tagName == null) return;

            ITag<Block> tempTag =  ForgeRegistries.BLOCKS.tags().getTag(BlockTags.create(new ResourceLocation(tagName.group(1), tagName.group(2))));
            if(tempTag.size()==0) return;
            tempTag.forEach(block -> {output.put(BlockStateString.getBlockName(block), -1);});
        });

    }

    //左键点击方块
    @SubscribeEvent
    public static void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        BlockPos pos =  event.getPos();
        Level world = event.getLevel();
        if(!world.isClientSide) return;

        Player player = event.getEntity();
//        if(player==null)return;
        Item item = event.getItemStack().getItem();
        if(AkatZumaWorldEdit.USEITEM.get(item) == null)return;
        if(event.getAction()!= PlayerInteractEvent.LeftClickBlock.Action.START)return;


        PlayerMapData PMD = Util.getPMD(player);


        if(item instanceof QueryBlockStateItem){
            if(player.getCooldowns().isOnCooldown(item))return;
            player.getCooldowns().addCooldown(item, 10);
            queryBlock(world, pos, player, PMD);

            return;
        }
        if(item instanceof WoodAxeItem){
            NetworkingHandle.INSTANCE.sendToServer(new C2SPos1(pos));
            WoodAxeItem.clickPos(world,pos,player, true );
            return;
        }
        if(item instanceof ProjectorItem){

            if(player.getCooldowns().isOnCooldown(item))return;
            player.getCooldowns().addCooldown(item, 10);

            SendCopyMessage.sendCommand("a copy");

            return;

        }
    }


    public static void queryBlock(Level world, BlockPos pos, Player player, PlayerMapData PMD){
        BlockState blockState = world.getBlockState(pos);

        if(Util.isDownCtrl()){
            PMD.setReplaceBlockState(blockState);
        }else{
            PMD.setQueryBlockState(blockState);
        }
        if(player.isLocalPlayer()){
            String blockStateStr = blockState.toString().replaceFirst("}", "")
                    .replaceFirst("^Block\\{", "");

            Component component = blockState.getBlock().getName().append(Component.literal(": "));
            Component copy = SendCopyMessage.send(blockStateStr);
            AkatZumaWorldEdit.sendAkatMessage(component, copy, player);
        }
    }

//    @SubscribeEvent
//    public static void onRightClickAir(PlayerInteractEvent.RightClickEmpty event) {
//
//        System.out.println(event.getEntity().getMainHandItem());
//        System.out.println(event.getSide());
//    }




}
