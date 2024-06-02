package net.deepacat.ccamorewires.index;

import com.jozufozu.flywheel.core.PartialModel;
import net.deepacat.ccamorewires.CCAMoreWires;

import net.minecraft.resources.ResourceLocation;

public class CAPartials {

	public static final PartialModel SMALL_LIGHT = block("connector/small_light");

	private static PartialModel block(String path) {
		return new PartialModel(new ResourceLocation(CCAMoreWires.MODID, "block/" + path));
	}

	private static PartialModel entity(String path) {
		return new PartialModel(new ResourceLocation(CCAMoreWires.MODID, "entity/" + path));
	}

	public static void init() {
		// init static fields
	}
}
