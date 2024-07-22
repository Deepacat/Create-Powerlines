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

    static void groupAdd(String prefix, int energy, SpoolType spool, int color) {
        TYPES.add(new ConnectorType(prefix + "_connector_small", 4, 16, energy, energy, spool, 1, 0, color));
        TYPES.add(new ConnectorType(prefix + "_connector_large", 6, 32, (int) Math.round(energy * 1.5), (int) Math.round(energy * 1.5), spool, 2, 1, color));
        TYPES.add(new ConnectorType(prefix + "_connector_huge", 4, 64, energy * 2, energy * 2, spool, 3, 1, color));
        TYPES.add(new ConnectorType(prefix + "_connector_giant", 3, 128, (int) Math.round(energy * 2.5), (int) Math.round(energy * 2.5), spool, 3, 2, color));
        TYPES.add(new ConnectorType(prefix + "_connector_massive", 3, 256, energy * 3, energy * 3, spool, 3, 4, color));
    }

    public static void register() {
        groupAdd("lv", 2048, SpoolType.COPPER, 0x6F6F6F);
        groupAdd("mv", 8192, SpoolType.GOLD, 0x33CCFF);
        groupAdd("hv", 32768, SpoolType.ELECTRUM, 0xE1E1E1);

        for (ConnectorType type : TYPES) {
            BlockEntry<ConnectorBlock> block = CCAMoreWires.REGISTRATE.block(type.id, (props) -> new ConnectorBlock(props, type))
                    .initialProperties(SharedProperties::stone)
                    .onRegister(AllMovementBehaviours.movementBehaviour(new NodeMovementBehaviour()))
                    .item()
                    .transform(customItemModel())
                    .defaultLang()
                    .register();
            type.beEntry = CCAMoreWires.REGISTRATE
                    .<ConnectorBlockEntity>blockEntity(type.id, (beType, pos, state) -> new ConnectorBlockEntity(beType, pos, state, type))
                    .validBlocks(block)
                    .renderer(() -> ConnectorRenderer::new)
                    .register();
        }
    }
}
