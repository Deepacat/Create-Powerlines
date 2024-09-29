package net.deepacat.createpowerlines.blocks.connector;

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

public enum ConnectorMode implements StringRepresentable {
	Push("push"),
	Pull("pull"),
	None("none");

	private final String name;

	ConnectorMode(String name) {
		this.name = name;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}

	public ConnectorMode getNext() {
		switch (this) {
			case None:
				return Pull;
			case Pull:
				return Push;
			case Push:
				return None;
		}
		return None;
	}

	public MutableComponent getTooltip() {
		switch (this) {
			case None:
				return Component.translatable("createpowerlines.tooltip.energy.none");
			case Pull:
				return Component.translatable("createpowerlines.tooltip.energy.pull");
			case Push:
				return Component.translatable("createpowerlines.tooltip.energy.push");
		}
		return Component.translatable("createpowerlines.tooltip.energy.none");
	}

	public boolean isActive() {
		return this == Push || this == Pull;
	}

	public static ConnectorMode test(Level level, BlockPos pos, Direction face) {
		BlockEntity be = level.getBlockEntity(pos);
		if(be == null) return None;
		LazyOptional<IEnergyStorage> optional = be.getCapability(ForgeCapabilities.ENERGY, face);
		if(!optional.isPresent()) optional = be.getCapability(ForgeCapabilities.ENERGY);
		if(!optional.isPresent()) return None;
		if(optional.orElse(null) == null) return None;

		IEnergyStorage e = optional.orElse(null);

		if(e.canExtract()) return Pull;
		if(e.canReceive()) return Push;

		return None;
	}
}
