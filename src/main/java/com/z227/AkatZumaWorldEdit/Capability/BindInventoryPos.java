package com.z227.AkatZumaWorldEdit.Capability;

import net.minecraft.nbt.CompoundTag;


public class BindInventoryPos {

    private CompoundTag compoundNBT;


    public BindInventoryPos() {
        this.compoundNBT = new CompoundTag();
        compoundNBT.putIntArray("pos", new int[]{});
    }


    public CompoundTag serializeNBT() {
        return this.compoundNBT;
    }


    public void deserializeNBT(CompoundTag nbt) {
        this.compoundNBT = nbt;
    }

    public CompoundTag getCompoundNBT() {
        return compoundNBT;
    }

    public void setCompoundNBT(CompoundTag compoundNBT) {
        this.compoundNBT = compoundNBT;
    }


}
