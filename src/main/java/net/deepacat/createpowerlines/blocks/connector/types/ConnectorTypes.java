package net.deepacat.createpowerlines.blocks.connector.types;

import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.blocks.connector.base.SpoolType;
import net.deepacat.createpowerlines.energy.NodeMovementBehaviour;
import net.deepacat.createpowerlines.blocks.connector.base.ConnectorRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ConnectorTypes {
    public static final List<ConnectorType> TYPES = new ArrayList<>();

    static void add(String tier, String size, int connections, int wireLength, int baseEnergy, double energyMult,
                    SpoolType spoolType, int width, int height, int color, ConnectorStyle style) {
        String id = tier.toLowerCase() + "_connector_" + size.toLowerCase();
        String defaultDisplayName = size + " " + tier + " Connector";
        int energy = (int) Math.round(baseEnergy * energyMult);
        TYPES.add(new ConnectorType(id, connections, wireLength, energy, energy, spoolType, width, height, color, style, defaultDisplayName));
    }

    static void addTier(String tier, int baseEnergy, SpoolType spoolType, int color) {
        add(tier, "Small", 4, 16, baseEnergy, 1, spoolType, 1, 0, color, ConnectorStyle.SMALL);
        add(tier, "Large", 6, 32, baseEnergy, 1.5, spoolType, 2, 1, color, ConnectorStyle.SMALL);
        add(tier, "Huge", 4, 64, baseEnergy, 2, spoolType, 3, 1, color, ConnectorStyle.LARGE);
        add(tier, "Giant", 3, 128, baseEnergy, 2.5, spoolType, 3, 2, color, ConnectorStyle.LARGE);
        add(tier, "Massive", 3, 256, baseEnergy, 3, spoolType, 3, 4, color, ConnectorStyle.LARGE);
    }

    public static void register() {
        addTier("Copper", 2048, SpoolType.COPPER, 0xC55E4B);
        addTier("Gold", 8192, SpoolType.GOLD, 0xE6AF15);

        for (ConnectorType type : TYPES) {
            BlockEntry<ConnectorBlock> block = CreatePowerlines.REGISTRATE.block(type.id, (props) -> new ConnectorBlock(props, type))
                    .initialProperties(SharedProperties::stone)
                    .onRegister(AllMovementBehaviours.movementBehaviour(new NodeMovementBehaviour()))
                    .item()
                    .transform(customItemModel())
                    .defaultLang()
                    .register();
            type.beEntry = CreatePowerlines.REGISTRATE
                    .<ConnectorBlockEntity>blockEntity(type.id, (beType, pos, state) -> new ConnectorBlockEntity(beType, pos, state, type))
                    .validBlocks(block)
                    .renderer(() -> ConnectorRenderer::new)
                    .register();
        }
    }

    public static void getDefaultTranslations(Map<String, String> out) {
        for (ConnectorType type : TYPES) {
            out.put("block." + CreatePowerlines.MODID + "." + type.id, type.defaultDisplayName);
        }
    }
}
