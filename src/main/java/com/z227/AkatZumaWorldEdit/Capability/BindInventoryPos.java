package com.z227.AkatZumaWorldEdit.Capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public class BindInventoryPos {

    private CompoundTag compoundNBT;

    public BindInventoryPos() {
        this.compoundNBT = new CompoundTag();
    }


    public CompoundTag serializeNBT() {
//        CompoundTag compoundNBT = new CompoundTag();
        for(int i = 1; i <= 5; ++i){
            compoundNBT.putIntArray("pos"+i, new int[]{i,i+1,i+2});
        }

        return compoundNBT;
    }


    public void deserializeNBT(CompoundTag nbt) {
        this.compoundNBT = nbt;

//        for(int i = 1; i <= 5; ++i){
//            this.invPosMap.put("pos"+i, nbt.getIntArray("pos"+i));
//        }

    }

    public CompoundTag getCompoundNBT() {
        return compoundNBT;
    }

    public void setCompoundNBT(CompoundTag compoundNBT) {
        this.compoundNBT = compoundNBT;
    }
}
