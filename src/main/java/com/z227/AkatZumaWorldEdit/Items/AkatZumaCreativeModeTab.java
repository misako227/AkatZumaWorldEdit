package com.z227.AkatZumaWorldEdit.Items;

import com.z227.AkatZumaWorldEdit.AkatZumaWorldEdit;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class AkatZumaCreativeModeTab extends CreativeModeTab{

    public AkatZumaCreativeModeTab() {
        super(AkatZumaWorldEdit.MODID);
    }

    @Override
    public ItemStack makeIcon() {
        return AkatZumaWorldEdit.WOOD_AXE.get().getDefaultInstance();
    }
}
