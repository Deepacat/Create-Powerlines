package net.deepacat.ccamorewires.index;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

import net.deepacat.ccamorewires.CCAMoreWires;
import net.deepacat.ccamorewires.blocks.connector.GiantConnectorBlock;
import net.deepacat.ccamorewires.blocks.connector.HugeConnectorBlock;
import net.deepacat.ccamorewires.blocks.connector.MassiveConnectorBlock;
import net.deepacat.ccamorewires.energy.NodeMovementBehaviour;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;

public class CABlocks {

	static {
		CCAMoreWires.REGISTRATE.setCreativeTab(CACreativeModeTabs.MAIN_TAB);
	}

	public static final BlockEntry<HugeConnectorBlock> HUGE_CONNECTOR = CCAMoreWires.REGISTRATE.block("huge_connector",  HugeConnectorBlock::new)
			.initialProperties(SharedProperties::stone)
			.onRegister(AllMovementBehaviours.movementBehaviour(new NodeMovementBehaviour()))
			.item()
			.transform(customItemModel())
			.register();

	public static final BlockEntry<GiantConnectorBlock> GIANT_CONNECTOR = CCAMoreWires.REGISTRATE.block("giant_connector",  GiantConnectorBlock::new)
			.initialProperties(SharedProperties::stone)
			.onRegister(AllMovementBehaviours.movementBehaviour(new NodeMovementBehaviour()))
			.item()
			.transform(customItemModel())
			.register();

	public static final BlockEntry<MassiveConnectorBlock> MASSIVE_CONNECTOR = CCAMoreWires.REGISTRATE.block("massive_connector",  MassiveConnectorBlock::new)
			.initialProperties(SharedProperties::stone)
			.onRegister(AllMovementBehaviours.movementBehaviour(new NodeMovementBehaviour()))
			.item()
			.transform(customItemModel())
			.register();

	/*public static final BlockEntry<AccumulatorBlock> ACCUMULATOR = ccawires.REGISTRATE.block("accumulator",  AccumulatorBlock::new)
			.initialProperties(SharedProperties::softMetal)
			.onRegister(AllMovementBehaviours.movementBehaviour(new NodeMovementBehaviour()))
			.item()
			//.tab()
			.transform(customItemModel())
			.register();*/

	/*public static final BlockEntry<HarmfulPlantBlock> HARMFUL_PLANT = ccawires.REGISTRATE.block("harmful_plant",  HarmfulPlantBlock::new)
			.initialProperties(Material.PLANT)
			.properties(props -> props.sound(SoundType.CROP).strength(0.5f))
			.item()
			.transform(customItemModel())
			.register();*/

	/*public static final BlockEntry<CasingBlock> COPPER_WIRE_CASING = REGISTRATE.block("copper_wire_casing", CasingBlock::new)
			.properties(p -> p.color(MaterialColor.PODZOL))
			.transform(BuilderTransformers.casing(() -> CASpriteShifts.COPPER_WIRE_CASING))
			.register();*/

	public static void register() {

	}
}
