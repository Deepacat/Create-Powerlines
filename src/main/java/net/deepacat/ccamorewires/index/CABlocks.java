package net.deepacat.ccamorewires.index;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

import net.deepacat.ccamorewires.CCAMoreWires;
import net.deepacat.ccamorewires.blocks.connector.Manual.GiantConnectorBlock;
import net.deepacat.ccamorewires.blocks.connector.Manual.HugeConnectorBlock;
import net.deepacat.ccamorewires.blocks.connector.Manual.MassiveConnectorBlock;
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

	public static void register() {

	}
}
