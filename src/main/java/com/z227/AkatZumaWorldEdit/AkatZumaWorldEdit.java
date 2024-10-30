package com.z227.AkatZumaWorldEdit;

import com.mojang.logging.LogUtils;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Items.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



@Mod(AkatZumaWorldEdit.MODID)
public class AkatZumaWorldEdit{
    public static final String MODID = "akatzumaworldedit";
//    public static final String MODNAME = "AkatZumaWorldEdit";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final AkatZumaCreativeModeTab CREATIVE_MODE_TAB = new AkatZumaCreativeModeTab();


    public static MutableComponent Akat = new TextComponent("AkatZuma").withStyle(ChatFormatting.GOLD)
            .append(new TextComponent("]:").withStyle(ChatFormatting.WHITE));
//    public static Component Akat = TranslatableComponent.literal("AkatZuma").withStyle(ChatFormatting.GOLD)
//            .append( new TextComponent("]:").withStyle(ChatFormatting.WHITE));

    public static Map<Item, Boolean> USEITEM = new HashMap<>();


    public static Map<UUID, PlayerMapData> PlayerWEMap = new HashMap<>();

    public static Map<String, Integer> defaultBlockMap = new HashMap<>();      //黑白名单方块
    public static Map<String, Integer> VipBlockMap = new HashMap<>();      //vip黑白名单方块
    public static Map<String, Boolean> VipPlayerMap = new HashMap<>();      //vip玩家
    public static Map<String, Boolean> BlackWorldMap = new HashMap<>();
    public static Map<String, Boolean> ReplaceBlockMap = new HashMap<>();

    public static boolean loadSopBackpacks = false;
    public static boolean loadSopStorage = false;
    public static boolean loadCurios = false;

    public static final DeferredRegister<Item> ITEMS;
    public static final RegistryObject<Item> WOOD_AXE;
    public static final RegistryObject<Item> Query_Item;
    public static final RegistryObject<Item> Projector_Item;
    public static final RegistryObject<Item> BindInventory;
    public static final RegistryObject<Item> Line_Item;


    public static final DeferredRegister<Block> BLOCKS;
    public static final RegistryObject<Block> Building_Consumable_Block;
    public static final RegistryObject<Item> Building_Consumable_Item;


    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        WOOD_AXE = ITEMS.register("wood_axe", () -> new WoodAxeItem(new Item.Properties().stacksTo(1).tab(CREATIVE_MODE_TAB)));
        Query_Item = ITEMS.register("query_blockstate_item", () ->  new QueryBlockStateItem(new Item.Properties().stacksTo(1).tab(CREATIVE_MODE_TAB)));
        Projector_Item = ITEMS.register("projector", () ->  new ProjectorItem(new Item.Properties().stacksTo(1).tab(CREATIVE_MODE_TAB)));
        BindInventory = ITEMS.register("bind_inventory", () ->  new BindInventoryItem(new Item.Properties().stacksTo(1).tab(CREATIVE_MODE_TAB)));
        Line_Item = ITEMS.register("line_item", () ->  new LineItem(new Item.Properties().stacksTo(1).tab(CREATIVE_MODE_TAB)));

        Building_Consumable_Block = BLOCKS.register("building_consumable", () -> new BuildingConsumable(BlockBehaviour.Properties.of(Material.DIRT).requiresCorrectToolForDrops().strength(0.2f, 1.5F)));
        Building_Consumable_Item = ITEMS.register("building_consumable",()-> new BlockItem(Building_Consumable_Block.get(), new Item.Properties().tab(CREATIVE_MODE_TAB)));

    }


    public AkatZumaWorldEdit() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        BLOCKS.register(bus);


//        FMLJavaModLoadingContext.get().getModEventBus().addListener(CreativeModeTab::buildContents);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
//        AkatZumaCreativeModeTab.TABS.register(bus);



    }








    //发送消息
    public static void sendAkatMessage(Component component, Player player){
        player.sendMessage(new TextComponent("[").append(Akat).append(component), player.getUUID());
//        player.sendSystemMessage(new TextComponent("[").append(Akat).append(component));
    }

    public static void sendAkatMessage(String message, Component component, Player player){
        player.sendMessage(new TextComponent("[").append(Akat).append(message).append(component), player.getUUID());
//        player.sendSystemMessage(new TextComponent("[").append(Akat).append(message).append(component));
    }

    public static void sendAkatMessage(Component component, String message,  Player player){
        player.sendMessage(new TextComponent("[").append(Akat).append(component).append(message), player.getUUID());
//        player.sendSystemMessage(new TextComponent("[").append(Akat).append(component).append(message));
    }

    public static void sendAkatMessage(Component message, Component component, Player player){
        player.sendMessage(new TextComponent("[").append(Akat).append(message).append(component), player.getUUID());
//        player.sendSystemMessage(new TextComponent("[").append(Akat).append(message).append(component));
    }
    public static void sendClientMessage(Component message, Component component, Player player){
        player.displayClientMessage(new TextComponent("[").append(Akat).append(message).append(component),true);
//        player.displayClientMessage(new TextComponent("[").append(Akat).append(message).append(component),true);
    }
    public static void sendClientMessage(Component component, Player player){
        player.displayClientMessage(new TextComponent("[").append(Akat).append(component),true);
//        player.displayClientMessage(new TextComponent("[").append(Akat).append(component),true);
    }


}
