package net.deepacat.ccamorewires.blocks.connector.types;

import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.deepacat.ccamorewires.CCAMoreWires;
import net.deepacat.ccamorewires.blocks.connector.base.SpoolType;
import net.deepacat.ccamorewires.energy.NodeMovementBehaviour;
import net.deepacat.ccamorewires.blocks.connector.base.ConnectorRenderer;

import java.util.ArrayList;
import java.util.List;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ConnectorTypes {
    public static final List<ConnectorType> TYPES = new ArrayList<>();

    public static void register() {
        TYPES.add(new ConnectorType("lv_connector_small", 4, 16, 2048, 2048, SpoolType.COPPER, 1, 0, 0xFFB947));
        TYPES.add(new ConnectorType("lv_connector_large", 6, 32, 2048, 2048, SpoolType.COPPER, 2, 1, 0xFFB947));
        TYPES.add(new ConnectorType("lv_connector_huge", 4, 64, 2048, 2048, SpoolType.COPPER, 3, 1, 0xFFB947));
        TYPES.add(new ConnectorType("lv_connector_giant", 3, 128, 2048, 2048, SpoolType.COPPER, 3, 2, 0xFFB947));
        TYPES.add(new ConnectorType("lv_connector_massive", 3, 256, 2048, 2048, SpoolType.COPPER, 3, 4, 0xFFB947));
        TYPES.add(new ConnectorType("mv_connector_small", 4, 16, 8192, 8192, SpoolType.GOLD, 1, 0, 0x4AC3FF));
        TYPES.add(new ConnectorType("mv_connector_large", 6, 32, 8192, 8192, SpoolType.GOLD, 2, 1, 0x4AC3FF));
        TYPES.add(new ConnectorType("mv_connector_huge", 4, 64, 8192, 8192, SpoolType.GOLD, 3, 1, 0x4AC3FF));
        TYPES.add(new ConnectorType("mv_connector_giant", 3, 128, 8192, 8192, SpoolType.GOLD, 3, 2, 0x4AC3FF));
        TYPES.add(new ConnectorType("mv_connector_massive", 3, 256, 8192, 8192, SpoolType.GOLD, 3, 4, 0x4AC3FF));

        for (ConnectorType type : TYPES) {
            BlockEntry<ConnectorBlock> block = CCAMoreWires.REGISTRATE.block(type.id, (props) -> new ConnectorBlock(props, type))
                    .initialProperties(SharedProperties::stone)
                    .onRegister(AllMovementBehaviours.movementBehaviour(new NodeMovementBehaviour()))
                    .item()
                    .transform(customItemModel())
                    .register();
            type.beEntry = CCAMoreWires.REGISTRATE
                    .<ConnectorBlockEntity>blockEntity(type.id, (beType, pos, state) -> new ConnectorBlockEntity(beType, pos, state, type))
                    .validBlocks(block)
                    .renderer(() -> ConnectorRenderer::new)
                    .register();
        }
    }
}
