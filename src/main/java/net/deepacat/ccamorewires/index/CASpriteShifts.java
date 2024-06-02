package net.deepacat.ccamorewires.index;

import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;

import net.deepacat.ccamorewires.CCAMoreWires;
import net.minecraft.resources.ResourceLocation;

import static com.simibubi.create.foundation.block.connected.CTSpriteShifter.getCT;
import static com.simibubi.create.foundation.block.connected.AllCTTypes.OMNIDIRECTIONAL;
import static com.simibubi.create.foundation.block.connected.AllCTTypes.RECTANGLE;

public class CASpriteShifts {
	//public static final CTSpriteShiftEntry OVERCHARGED_CASING = getCT(OMNIDIRECTIONAL,  new ResourceLocation(ccawires.MODID, "block/overcharged_casing/overcharged_casing"), new ResourceLocation(ccawires.MODID, "block/overcharged_casing/overcharged_casing_connected"));
	public static final CTSpriteShiftEntry
		ACCUMULATOR = getCT(
				RECTANGLE,
				new ResourceLocation(CCAMoreWires.MODID, "block/modular_accumulator/block"),
				new ResourceLocation(CCAMoreWires.MODID, "block/modular_accumulator/block_connected")
			),
		ACCUMULATOR_TOP = getCT(
				RECTANGLE,
				new ResourceLocation(CCAMoreWires.MODID, "block/modular_accumulator/block_top"),
				new ResourceLocation(CCAMoreWires.MODID, "block/modular_accumulator/block_top_connected")
			),

		COPPER_WIRE_CASING = getCT(
				OMNIDIRECTIONAL,
			new ResourceLocation(CCAMoreWires.MODID, "block/copper_wire_casing/block"),
			new ResourceLocation(CCAMoreWires.MODID, "block/copper_wire_casing/block_connected")
		);
}
