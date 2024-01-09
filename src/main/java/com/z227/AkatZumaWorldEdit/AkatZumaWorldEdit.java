package com.z227.AkatZumaWorldEdit;

import com.z227.AkatZumaWorldEdit.Items.testItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


@Mod(AkatZumaWorldEdit.MODID)
public class AkatZumaWorldEdit{
    public static final String MODID = "akatzumaworldedit";
    public static final String MODNAME = "AkatZumaWorldEdit";

    public AkatZumaWorldEdit() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(CreativeModeTab::buildContents);
    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item", () -> new testItem(new Item.Properties()));


}
