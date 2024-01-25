package com.z227.AkatZumaWorldEdit.Event;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.utilities.BlockStateString;
import com.z227.AkatZumaWorldEdit.utilities.Util;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
        AkatZumaWorldEdit.PlayerWEMap.get(player.getUUID()).setVip(Util.checkVip(playerName, Config.VIPPlayerList.get()));

    }

    @SubscribeEvent
    public static void Logout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        AkatZumaWorldEdit.PlayerWEMap.remove(player.getUUID());
//        System.out.println("退出："+ player.getName().getString());


    }

    //玩家加入服务器，仅在客户端触发
    @SubscribeEvent
    public static void Logout(ClientPlayerNetworkEvent.LoggingIn event) {
        LocalPlayer player = event.getPlayer();
        String playerName = player.getName().getString();
//        System.out.println("登录："+ playerName);
        AkatZumaWorldEdit.PlayerWEMap.put(player.getUUID(), new PlayerMapData(playerName));
    }

    @SubscribeEvent
    public static void ServerStarted(ServerStartedEvent event){
        addTagsToMap(Config.BLACKListTags.get(), AkatZumaWorldEdit.defaultBlockMap);
        addTagsToMap(Config.VIPBLACKListBlock.get(), AkatZumaWorldEdit.VipBlockMap);
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




}
