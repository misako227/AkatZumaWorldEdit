package com.z227.AkatZumaWorldEdit;

import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Items.QueryBlockStateItem;
import com.z227.AkatZumaWorldEdit.Items.WoodAxeItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Mod(AkatZumaWorldEdit.MODID)
public class AkatZumaWorldEdit{
    public static final String MODID = "akatzumaworldedit";
    public static final String MODNAME = "AkatZumaWorldEdit";

    public static Component Akat = Component.literal("AkatZuma").withStyle(ChatFormatting.GOLD)
            .append( Component.literal("]:").withStyle(ChatFormatting.WHITE));


    public static Map<UUID, PlayerMapData> PlayerWEMap = new HashMap<>();

    public AkatZumaWorldEdit() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(CreativeModeTab::buildContents);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> WOOD_AXE = ITEMS.register("wood_axe", () -> new WoodAxeItem(new Item.Properties()));
    public static final RegistryObject<Item> Query_Item = ITEMS.register("query_blockstate_item", () -> new QueryBlockStateItem(new Item.Properties()));


    //发送消息
    public static void sendAkatMessage(String message, Player player){
        player.sendSystemMessage(Component.literal("[").append(Akat).append(message));
    }

    public static void sendAkatMessage(String message, Component component, Player player){
        player.sendSystemMessage(Component.literal("[").append(Akat).append(message).append(component));
    }


}
