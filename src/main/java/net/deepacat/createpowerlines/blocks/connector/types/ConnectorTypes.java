package net.deepacat.createpowerlines.blocks.connector.types;

import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.blocks.connector.base.SpoolType;
import net.deepacat.createpowerlines.config.Config;
import net.deepacat.createpowerlines.energy.NodeMovementBehaviour;
import net.deepacat.createpowerlines.blocks.connector.base.ConnectorRenderer;
import net.minecraftforge.fml.ModList;

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
        if(Config.USE_BASE_CONNECTORS.get()){
            addTier("Copper", 2048, SpoolType.COPPER, 0xC55E4B);
            addTier("Gold", 8192, SpoolType.GOLD, 0xE6AF15);
        }
        if(Config.USE_CCA_CONNECTORS.get() && ModList.get().isLoaded("createaddition")){
            addTier("Electrum", 32768, SpoolType.ELECTRUM, 0xF8D86F);
        }
        if(Config.USE_GT_CONNECTORS.get() && ModList.get().isLoaded("gtceu")){
            addTier("LV", 160, SpoolType.COPPER, 0x6F6F6F);
            addTier("MV", 640, SpoolType.GOLD, 0x33CCFF);
            addTier("HV", 2560, SpoolType.ELECTRUM, 0xFFFFFF);
            addTier("EV", 10240, SpoolType.ELECTRUM, 0xFFFFFF);
            addTier("IV", 40960, SpoolType.ELECTRUM, 0xFFFFFF);
            addTier("LuV", 163840, SpoolType.ELECTRUM, 0xFFFFFF);
            addTier("ZPM", 655360, SpoolType.ELECTRUM, 0xFFFFFF);
            addTier("UV", 2621440, SpoolType.ELECTRUM, 0xFFFFFF);
            addTier("UHV", 10485760, SpoolType.ELECTRUM, 0xFFFFFF);
        }

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
