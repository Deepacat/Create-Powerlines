package net.deepacat.createpowerlines.blocks.connector;

import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.config.Config;
import net.deepacat.createpowerlines.item.WireMaterial;
import net.deepacat.createpowerlines.item.WireMaterials;
import net.deepacat.createpowerlines.util.Util;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConnectorTypes {
    public static final List<ConnectorType> TYPES = new ArrayList<>();

    private static void registerOne(String tier, String size, int connections, int wireLength, int baseEnergy, double energyMult,
                                    WireMaterial[] wireMaterials, int width, int height, int color, ConnectorStyle style) {
        String display = size + " " + tier + " Connector";
        String id = Util.displayToId(display);
        int energy = (int) Math.round(baseEnergy * energyMult);
        TYPES.add(new ConnectorType(id, display, connections, wireLength, energy, energy, wireMaterials, width, height, color, style));
    }

    private static void registerTier(String tier, int baseEnergy, int color, WireMaterial... wireMaterials) {
        registerOne(tier, "Small", 4, 16, baseEnergy, 1, wireMaterials, 1, 0, color, ConnectorStyle.SMALL);
        registerOne(tier, "Large", 6, 32, baseEnergy, 1.5, wireMaterials, 2, 1, color, ConnectorStyle.SMALL);
        registerOne(tier, "Huge", 4, 64, baseEnergy, 2, wireMaterials, 3, 1, color, ConnectorStyle.LARGE);
        registerOne(tier, "Giant", 3, 128, baseEnergy, 2.5, wireMaterials, 3, 2, color, ConnectorStyle.LARGE);
        registerOne(tier, "Massive", 3, 256, baseEnergy, 3, wireMaterials, 3, 4, color, ConnectorStyle.LARGE);
    }

    public static void registerAll() {
        if (Config.USE_BASE_CONNECTORS.get()) {
            WireMaterial copper = WireMaterials.getOrRegister("Copper", 0xE77C56);
            WireMaterial gold = WireMaterials.getOrRegister("Gold", 0xFDF55F);
            registerTier("Copper", 2048, 0xC55E4B, copper);
            registerTier("Gold", 8192, 0xE6AF15, gold);
        }
        if (Config.USE_CCA_CONNECTORS.get() && ModList.get().isLoaded("createaddition")) {
            WireMaterial electrum = WireMaterials.getOrRegister("Electrum", 0xFFFF8B);
            registerTier("Electrum", 32768, 0xF8D86F, electrum);
        }
        if (Config.USE_GT_CONNECTORS.get() && ModList.get().isLoaded("gtceu")) {
            WireMaterial tin = WireMaterials.getOrRegister("Tin", 0xFAFEFF);
            WireMaterial copper = WireMaterials.getOrRegister("Copper", 0xE77C56);
            WireMaterial gold = WireMaterials.getOrRegister("Gold", 0xFDF55F);
            WireMaterial electrum = WireMaterials.getOrRegister("Electrum", 0xFFFF8B);
            registerTier("LV", 160, 0x6F6F6F, tin, copper);
            registerTier("MV", 640, 0x33CCFF, tin, copper, gold);
            registerTier("HV", 2560, 0xFFFFFF, tin, copper, gold, electrum);
            registerTier("EV", 10240, 0xFFFFFF, tin, copper, gold, electrum);
            registerTier("IV", 40960, 0xFFFFFF, tin, copper, gold, electrum);
            registerTier("LuV", 163840, 0xFFFFFF, tin, copper, gold, electrum);
            registerTier("ZPM", 655360, 0xFFFFFF, tin, copper, gold, electrum);
            registerTier("UV", 2621440, 0xFFFFFF, tin, copper, gold, electrum);
            registerTier("UHV", 10485760, 0xFFFFFF, tin, copper, gold, electrum);
        }
    }

    public static void getDefaultTranslations(Map<String, String> out) {
        String prefix = "item." + CreatePowerlines.MODID + ".";
        for (WireMaterial material : WireMaterials.MATERIALS.values()) {
            out.put(prefix + material.wireId(), material.display + " Wire");
            out.put(prefix + material.spoolId(), material.display + " Spool");
        }
        prefix = "block." + CreatePowerlines.MODID + ".";
        for (ConnectorType type : TYPES) {
            out.put(prefix + type.id, type.display);
        }
    }
}
