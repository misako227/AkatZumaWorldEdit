package com.z227.AkatZumaWorldEdit;

import com.z227.AkatZumaWorldEdit.Core.PlayerMapData;
import com.z227.AkatZumaWorldEdit.Items.testItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
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

    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item", () -> new testItem(new Item.Properties()));



}
