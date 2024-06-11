package net.deepacat.ccamorewires.blocks.connector.builder;

import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.deepacat.ccamorewires.CCAMoreWires;
import net.deepacat.ccamorewires.energy.NodeMovementBehaviour;
import net.minecraft.world.level.block.Block;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ConnectorBuilder {
    public static void connectorBuilder(String displayName, int connections, int wireLength, int energyIn, int energyOut) {
        // Register Block

//        BlockEntityEntry<GiantConnectorBlockEntity> GIANT_CONNECTOR = CCAMoreWires.REGISTRATE
//                .blockEntity("giant_connector", GiantConnectorBlockEntity::new)
//                .validBlocks(CABlocks.GIANT_CONNECTOR)
//                .renderer(() -> ConnectorRenderer::new)
//                .register();

//        BlockEntry<GiantConnectorBlock> GIANT_CONNECTOR = CCAMoreWires.REGISTRATE.block("giant_connector",  GiantConnectorBlock::new)
//                .initialProperties(SharedProperties::stone)
//                .onRegister(AllMovementBehaviours.movementBehaviour(new NodeMovementBehaviour()))
//                .item()
//                .transform(customItemModel())
//                .register();

        BlockEntry<Block> newConnector =
            CCAMoreWires.REGISTRATE.block(displayName, Block::new)
                .initialProperties(SharedProperties::stone)
                .onRegister(AllMovementBehaviours.movementBehaviour(new NodeMovementBehaviour()))
                .item()
                .transform(customItemModel())
                .register();
    }

    public static void register(){
        connectorBuilder("lv_connector_small", 4, 16, 2048, 2048);
        connectorBuilder("lv_connector_large", 6, 32, 2048, 2048);
        connectorBuilder("lv_connector_huge", 4, 64, 2048, 2048);
        connectorBuilder("lv_connector_giant", 3, 128, 2048, 2048);
        connectorBuilder("lv_connector_massive", 3, 256, 2048, 2048);
    }
}
