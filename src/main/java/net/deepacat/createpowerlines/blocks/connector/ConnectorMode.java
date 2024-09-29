package net.deepacat.createpowerlines.blocks.connector;

import net.deepacat.createpowerlines.CreatePowerlines;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum ConnectorMode implements StringRepresentable {
    Push("push"),
    Pull("pull"),
    None("none");

    private final String name;

    ConnectorMode(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public ConnectorMode getNext() {
        return switch (this) {
            case None -> Pull;
            case Pull -> Push;
            case Push -> None;
        };
    }

    public MutableComponent getTooltip() {
        return switch (this) {
            case None -> Component.translatable(CreatePowerlines.MODID + ".tooltip.energy.none");
            case Pull -> Component.translatable(CreatePowerlines.MODID + ".tooltip.energy.pull");
            case Push -> Component.translatable(CreatePowerlines.MODID + ".tooltip.energy.push");
        };
    }

    public boolean isActive() {
        return this == Push || this == Pull;
    }

    public static ConnectorMode test(Level level, BlockPos pos, Direction face) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be == null) return None;
        LazyOptional<IEnergyStorage> lazy = be.getCapability(ForgeCapabilities.ENERGY, face);
        if (!lazy.isPresent()) lazy = be.getCapability(ForgeCapabilities.ENERGY);
        if (!lazy.isPresent()) return None;
        Optional<IEnergyStorage> resolved = lazy.resolve();
        if (resolved.isEmpty()) return None;
        IEnergyStorage e = resolved.get();
        if (e.canExtract()) return Pull;
        if (e.canReceive()) return Push;
        return None;
    }
}
