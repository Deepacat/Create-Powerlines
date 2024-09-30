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
                                    ArrayList<WireMaterial> wireMaterials, int width, int height, int color, ConnectorStyle style) {
        String display = size + " " + tier + " Connector";
        String id = Util.displayToId(display);
        int energy = (int) Math.round(baseEnergy * energyMult);
        TYPES.add(new ConnectorType(id, display, connections, wireLength, energy, energy, wireMaterials, width, height, color, style));
    }

    private static void registerTier(String tier, int baseEnergy, int amps, int color, ArrayList<WireMaterial> wireMaterials) {
        registerOne(tier, "Small", 4, 16, baseEnergy, amps, wireMaterials, 1, 0, color, ConnectorStyle.SMALL);
        registerOne(tier, "Large", 6, 32, baseEnergy, amps*2, wireMaterials, 2, 1, color, ConnectorStyle.SMALL);
        registerOne(tier, "Huge", 4, 64, baseEnergy, amps*4, wireMaterials, 3, 1, color, ConnectorStyle.LARGE);
        registerOne(tier, "Giant", 3, 128, baseEnergy, amps*8, wireMaterials, 3, 2, color, ConnectorStyle.LARGE);
        registerOne(tier, "Massive", 3, 256, baseEnergy, amps*16, wireMaterials, 3, 4, color, ConnectorStyle.LARGE);
    }

    public static void registerAll() {
        if (Config.USE_BASE_CONNECTORS.get() && !Config.USE_GT_CONNECTORS.get()) {
            WireMaterial copper = WireMaterials.getOrRegister("Copper", 0xE77C56);
            WireMaterial gold = WireMaterials.getOrRegister("Gold", 0xFDF55F);
            ArrayList<WireMaterial> baseTier = new ArrayList<WireMaterial>();
            baseTier.add(copper);
            baseTier.add(gold);
            registerTier("Copper", 2048, 1, 0xC55E4B, baseTier);
            registerTier("Gold", 8192, 1, 0xE6AF15, baseTier);
        }
        if (Config.USE_CCA_CONNECTORS.get() && ModList.get().isLoaded("createaddition") && !Config.USE_GT_CONNECTORS.get()) {
            WireMaterial electrum = WireMaterials.getOrRegister("Electrum", 0xFFFF8B);
            ArrayList<WireMaterial> ccaTier = new ArrayList<WireMaterial>();
            ccaTier.add(electrum);
            registerTier("Electrum", 32768, 1, 0xF8D86F, ccaTier);
        }
        if (Config.USE_GT_CONNECTORS.get() && ModList.get().isLoaded("gtceu")) {
            ArrayList<WireMaterial> tier1 = new ArrayList<WireMaterial>();
            WireMaterial tin = WireMaterials.getOrRegister("Tin", 0xFAFEFF);
            WireMaterial copper = WireMaterials.getOrRegister("Copper", 0xff8a3d);
            WireMaterial gold = WireMaterials.getOrRegister("Gold", 0xffe01c);
            WireMaterial electrum = WireMaterials.getOrRegister("Electrum", 0xF8D86F);
            tier1.add(tin);
            tier1.add(copper);
            registerTier("LV", 32, 4, 0xa7a7a7, tier1);
            registerTier("MV", 128, 4, 0x7db9d8, tier1);
            registerTier("HV", 512, 4, 0xededfd, tier1);
            registerTier("EV", 2048, 6, 0xed8eea, tier1);
            registerTier("IV", 8192, 6, 0x687ece, tier1);
            registerTier("LuV", 32768, 6, 0xd1d1d1, tier1);
            registerTier("ZPM", 131072, 8, 0x323232, tier1);
            registerTier("UV", 524288, 8, 0x578062, tier1);
            registerTier("UHV", 2097152, 8, 0xFFFFFF, tier1);
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
