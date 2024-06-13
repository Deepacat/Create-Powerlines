package net.deepacat.ccamorewires.blocks.connector.builder;

import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.deepacat.ccamorewires.CCAMoreWires;
import net.deepacat.ccamorewires.blocks.connector.base.SpoolType;
import net.deepacat.ccamorewires.energy.NodeMovementBehaviour;
import net.deepacat.ccamorewires.blocks.connector.base.ConnectorRenderer;
import net.deepacat.ccamorewires.energy.WireType;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ConnectorBuilder {
    public static void connectorBuilder(String displayName, int connections, int wireLength, int energyIn, int energyOut, SpoolType SpoolType) {
        // TODO: maybe change to record
        ConnectorProperties connProps = new ConnectorProperties();
        connProps.connections = connections;
        connProps.wireLength = wireLength;
        connProps.energyIn = energyIn;
        connProps.energyOut = energyOut;
        connProps.SpoolType = SpoolType;

        // TODO: use something less janky
        BlockEntityEntry<ConnectorBlockEntity>[] beEntry = new BlockEntityEntry[1];
        BlockEntry<ConnectorBlock> block = CCAMoreWires.REGISTRATE.block(displayName, (blockProps) -> new ConnectorBlock(blockProps, connProps, beEntry))
                .initialProperties(SharedProperties::stone)
                .onRegister(AllMovementBehaviours.movementBehaviour(new NodeMovementBehaviour()))
                .item()
                .transform(customItemModel())
                .register();

        beEntry[0] = CCAMoreWires.REGISTRATE
                .<ConnectorBlockEntity>blockEntity(displayName, (beType, pos, state) -> new ConnectorBlockEntity(beType, pos, state, connProps))
                .validBlocks(block)
                .renderer(() -> ConnectorRenderer::new)
                .register();
    }

    public static void register() {
        connectorBuilder("lv_connector_small", 4, 16, 2048, 2048, SpoolType.COPPER);
        connectorBuilder("lv_connector_large", 6, 32, 2048, 2048, SpoolType.COPPER);
        connectorBuilder("lv_connector_huge", 4, 64, 2048, 2048, SpoolType.COPPER);
        connectorBuilder("lv_connector_giant", 3, 128, 2048, 2048, SpoolType.COPPER);
        connectorBuilder("lv_connector_massive", 3, 256, 2048, 2048, SpoolType.COPPER);
        connectorBuilder("mv_connector_small", 4, 16, 8192, 8192, SpoolType.GOLD);
        connectorBuilder("mv_connector_large", 6, 32, 8192, 8192, SpoolType.GOLD);
        connectorBuilder("mv_connector_huge", 4, 64, 8192, 8192, SpoolType.GOLD);
        connectorBuilder("mv_connector_giant", 3, 128, 8192, 8192, SpoolType.GOLD);
        connectorBuilder("mv_connector_massive", 3, 256, 8192, 8192, SpoolType.GOLD);
    }
}
