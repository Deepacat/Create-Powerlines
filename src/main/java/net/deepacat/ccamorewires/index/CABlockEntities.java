package net.deepacat.ccamorewires.index;

import net.deepacat.ccamorewires.CCAMoreWires;
import net.deepacat.ccamorewires.blocks.connector.HugeConnectorBlockEntity;
import net.deepacat.ccamorewires.blocks.connector.GiantConnectorBlockEntity;
import net.deepacat.ccamorewires.blocks.connector.MassiveConnectorBlockEntity;
import net.deepacat.ccamorewires.blocks.connector.base.ConnectorRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class CABlockEntities {

	public static final BlockEntityEntry<HugeConnectorBlockEntity> HUGE_CONNECTOR = CCAMoreWires.REGISTRATE
			.blockEntity("huge_connector", HugeConnectorBlockEntity::new)
			.validBlocks(CABlocks.HUGE_CONNECTOR)
			.renderer(() -> ConnectorRenderer::new)
			.register();

	public static final BlockEntityEntry<GiantConnectorBlockEntity> GIANT_CONNECTOR = CCAMoreWires.REGISTRATE
			.blockEntity("giant_connector", GiantConnectorBlockEntity::new)
			.validBlocks(CABlocks.GIANT_CONNECTOR)
			.renderer(() -> ConnectorRenderer::new)
			.register();

	public static final BlockEntityEntry<MassiveConnectorBlockEntity> MASSIVE_CONNECTOR = CCAMoreWires.REGISTRATE
			.blockEntity("massive_connector", MassiveConnectorBlockEntity::new)
			.validBlocks(CABlocks.MASSIVE_CONNECTOR)
			.renderer(() -> ConnectorRenderer::new)
			.register();
	
	public static void register() {}
}