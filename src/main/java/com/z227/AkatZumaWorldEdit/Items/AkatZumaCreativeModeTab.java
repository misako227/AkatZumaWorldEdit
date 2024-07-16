package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AkatZumaCreativeModeTab {

    public static final DeferredRegister<net.minecraft.world.item.CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AkatZumaWorldEdit.MODID);
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = TABS.register(AkatZumaWorldEdit.MODID,
        () -> CreativeModeTab.builder()
            // 设置所要展示的页的名称
            .title(Component.translatable("item_group." + AkatZumaWorldEdit.MODID + ".item"))
            // 设置页图标
            .icon(() -> new ItemStack(AkatZumaWorldEdit.WOOD_AXE.get()))
            // 为物品栏页添加默认物品
            .displayItems((params, output) -> {
                output.accept(AkatZumaWorldEdit.WOOD_AXE.get());
                output.accept(AkatZumaWorldEdit.Query_Item.get());
                output.accept(AkatZumaWorldEdit.Building_Consumable_Item.get());
                output.accept(AkatZumaWorldEdit.Projector_Item.get());
                output.accept(AkatZumaWorldEdit.BindInventory.get());
//                output.accept(AkatZumaWorldEdit.Line_Item.get());
            })
            .build()
    );

}
