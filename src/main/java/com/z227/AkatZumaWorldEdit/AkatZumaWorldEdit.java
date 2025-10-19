package com.z227.AkatZumaWorldEdit;

import com.z227.AkatZumaWorldEdit.ConfigFile.AkatZumaLogger;
import com.z227.AkatZumaWorldEdit.ConfigFile.Config;
import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Items.*;
import com.z227.AkatZumaWorldEdit.utilities.ConfigFileUtil;
import com.z227.ImGuiRender.EditModeData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
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

    public static final AkatZumaLogger LOGGER = AkatZumaLogger.getInstance();


    public static Component Akat = Component.literal("AkatZuma").withStyle(ChatFormatting.GOLD)
            .append( Component.literal("]:").withStyle(ChatFormatting.WHITE));

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

    public static final DeferredRegister<Attribute> ATTRIBUTE_REGISTRY;
    public static final RegistryObject<Attribute> SET_FLAG_ATTRIBUTE;





    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        WOOD_AXE = ITEMS.register("wood_axe", () -> new WoodAxeItem(new Item.Properties().stacksTo(1)));
        Query_Item = ITEMS.register("query_blockstate_item", () ->  new QueryBlockStateItem(new Item.Properties().stacksTo(1)));
        Projector_Item = ITEMS.register("projector", () ->  new ProjectorItem(new Item.Properties().stacksTo(1)));
        BindInventory = ITEMS.register("bind_inventory", () ->  new BindInventoryItem(new Item.Properties().stacksTo(1)));
        Line_Item = ITEMS.register("line_item", () ->  new LineItem(new Item.Properties().stacksTo(1)));

        Building_Consumable_Block = BLOCKS.register("building_consumable", () -> new BuildingConsumable(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(0.2f, 1.5F)));
        Building_Consumable_Item = ITEMS.register("building_consumable",()-> new BlockItem(Building_Consumable_Block.get(), new Item.Properties()));


        ATTRIBUTE_REGISTRY = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MODID);
        SET_FLAG_ATTRIBUTE = ATTRIBUTE_REGISTRY.register("set_flag", () ->
                new RangedAttribute("attribute.akatzuma.set_flag", 0.0D, 0.0D, 1024.0D)
                .setSyncable(true)); // 如果需要同步到客户端，则设置为 true)

    }




    public AkatZumaWorldEdit() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        BLOCKS.register(bus);
        ATTRIBUTE_REGISTRY.register(bus);


//        FMLJavaModLoadingContext.get().getModEventBus().addListener(CreativeModeTab::buildContents);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
        AkatZumaCreativeModeTab.TABS.register(bus);

        init();

        ConfigFileUtil.createConfigDir();


    }


    @OnlyIn(Dist.CLIENT)
    public static void init(){
        EditModeData.init();
//        AkatZumaLog.onInitialize();
//        Schematic.init();
    }








    //一般发送错误提示消息
    public static void sendAkatMessage(Component component, Player player){

        player.sendSystemMessage(Component.literal("[").append(Akat).append(component));
    }

//    public static void sendAkatMessage(String message, Component component, Player player){
//        player.sendSystemMessage(Component.literal("[").append(Akat).append(message).append(component));
//    }

    public static void sendAkatMessage(Component component, String message,  Player player){
        player.sendSystemMessage(Component.literal("[").append(Akat).append(component).append(message));
    }

    //一般发送复制消息使用
    public static void sendAkatMessage(Component message, Component component, Player player){
        player.sendSystemMessage(Component.literal("[").append(Akat).append(message).append(component));
    }
    public static void sendClientMessage(Component message, Component component, Player player){
        player.displayClientMessage(Component.literal("[").append(Akat).append(message).append(component),true);
    }
    //一般发送放置成功消息，推荐使用SendCopyMessage.sendSuccessMsg()
    public static void sendClientMessage(Component component, Player player){
        player.displayClientMessage(Component.literal("[").append(Akat).append(component),true);
    }


}
