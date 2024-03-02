package com.z227.AkatZumaWorldEdit.Capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
@AutoRegisterCapability
public class BindInventoryPosCapability implements ICapabilitySerializable<CompoundTag> {
    public static Capability<BindInventoryPos> BIND_INV_POS_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    private BindInventoryPos bindInventoryPos = null;
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap == BIND_INV_POS_CAP ? LazyOptional.of(this::createCapability).cast() : LazyOptional.empty();
    }

    @Nonnull
    private BindInventoryPos createCapability() {
        if (bindInventoryPos == null) {
            this.bindInventoryPos = new BindInventoryPos();
        }
        return bindInventoryPos;
    }

    @Override
    public CompoundTag serializeNBT() {
        return createCapability().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createCapability().deserializeNBT(nbt);
    }
}
