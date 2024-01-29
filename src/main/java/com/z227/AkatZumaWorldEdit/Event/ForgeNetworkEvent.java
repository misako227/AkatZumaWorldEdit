package com.z227.AkatZumaWorldEdit.Event;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Items.QueryBlockStateItem;
import com.z227.AkatZumaWorldEdit.Items.WoodAxeItem;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import com.z227.AkatZumaWorldEdit.utilities.SendCopyMessage;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
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
        String playerName = player.getName().getString();
//        System.out.println("登录："+ playerName);
        AkatZumaWorldEdit.PlayerWEMap.put(player.getUUID(), new PlayerMapData(playerName));
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
    public static void LogginIn(ClientPlayerNetworkEvent.LoggingIn event) {
        LocalPlayer player = event.getPlayer();
        String playerName = player.getName().getString();
//        System.out.println("登录："+ playerName);
        AkatZumaWorldEdit.PlayerWEMap.put(player.getUUID(), new PlayerMapData(playerName));
    }

    @SubscribeEvent
    public static void ServerStarted(ServerStartedEvent event){
        addTagsToMap(Config.BLACKListTags.get(), AkatZumaWorldEdit.defaultBlockMap);
        addTagsToMap(Config.VIPBLACKListBlock.get(), AkatZumaWorldEdit.VipBlockMap);
        AkatZumaWorldEdit.LOGGER.info("加载黑名单标签成功");
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

    @SubscribeEvent
    public static void leftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        ItemStack itemStack =  event.getItemStack();
        Item item = itemStack.getItem();
        if(AkatZumaWorldEdit.USEITEM.get(item) == null)return;
        if(event.getAction()!= PlayerInteractEvent.LeftClickBlock.Action.START)return;

        BlockPos pos =  event.getPos();
        Level world = event.getLevel();
        PlayerMapData PMD = AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID());
        if(item instanceof QueryBlockStateItem){
            BlockState blockState = world.getBlockState(pos);

            PMD.setQueryBlockState(blockState);
            if(player.isLocalPlayer()){
                String blockStateStr = blockState.toString().replaceFirst("}", "")
                        .replaceFirst("^Block\\{", "");

                Component component = blockState.getBlock().getName().append(Component.literal(": "));
                Component copy = SendCopyMessage.send(blockStateStr);
                AkatZumaWorldEdit.sendAkatMessage(component, copy, player);
            }
            return;

        }
        if(item instanceof WoodAxeItem){
            WoodAxeItem.clickPos(world,pos, player,true );

        }
    }





}
