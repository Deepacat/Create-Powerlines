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
    public static ConnectorType RELAY;

    public static ConnectorType registerOne(String tier, String size, int connections, int wireLength, long baseEnergy, double energyMult,
                                            List<WireMaterial> wireMaterials, int width, int height, int color, ConnectorStyle style) {
        String display = size + " " + tier + " Connector";
        String id = Util.displayToId(display);
        int energyRate = (int) Math.round(baseEnergy * energyMult);
        ConnectorType result = new ConnectorType(id, display, connections, wireLength, energyRate, wireMaterials, width, height, color, style);
        TYPES.add(result);
        return result;
    }

    public static ConnectorType[] registerTier(String tier, long baseEnergy, double[] energyMults, int color, List<WireMaterial> wireMaterials) {
        return new ConnectorType[]{
                registerOne(tier, "Small", 4, 16, baseEnergy, energyMults[0], wireMaterials, 1, 0, color, ConnectorStyle.SMALL),
                registerOne(tier, "Large", 4, 32, baseEnergy, energyMults[1], wireMaterials, 2, 1, color, ConnectorStyle.SMALL),
                registerOne(tier, "Huge", 3, 64, baseEnergy, energyMults[2], wireMaterials, 3, 1, color, ConnectorStyle.LARGE),
                registerOne(tier, "Giant", 3, 128, baseEnergy, energyMults[3], wireMaterials, 3, 2, color, ConnectorStyle.LARGE),
                registerOne(tier, "Massive", 2, 256, baseEnergy, energyMults[4], wireMaterials, 3, 4, color, ConnectorStyle.LARGE)
        };
    }

    public static void registerTier(String tier, long baseEnergy, int amps, int color, List<WireMaterial> wireMaterials) {
        registerTier(tier, baseEnergy, new double[]{
                amps,
                amps * 1.5,
                amps * 2,
                amps * 3,
                amps * 4
        }, color, wireMaterials);
    }

    public static void registerAll() {
        ArrayList<WireMaterial> baseTier1 = new ArrayList<>();
        ArrayList<WireMaterial> baseTier2 = new ArrayList<>();
        ArrayList<WireMaterial> ccaTier = new ArrayList<>();
        ArrayList<WireMaterial> allTier = new ArrayList<>();


        if (Config.USE_BASE_CONNECTORS.get() && !ModList.get().isLoaded("gtceu")) {
            WireMaterial copper = WireMaterials.getOrRegister("Copper", 0xE77C56);
            WireMaterial gold = WireMaterials.getOrRegister("Gold", 0xFDF55F);
            baseTier1.add(copper);
            baseTier1.add(gold);
            baseTier2.add(gold);
        }

        if (Config.USE_CCA_CONNECTORS.get() && ModList.get().isLoaded("createaddition") && !ModList.get().isLoaded("gtceu")) {
            WireMaterial electrum = WireMaterials.getOrRegister("Electrum", 0xFFFF8B);
            baseTier1.add(electrum);
            baseTier2.add(electrum);
            ccaTier.add(electrum);
        }

        if (Config.USE_BASE_CONNECTORS.get() && !ModList.get().isLoaded("gtceu")) {
            registerTier("Copper", 2048, 1, 0xC55E4B, baseTier1);
            registerTier("Gold", 8192, 1, 0xf7bf31, baseTier2);
        }

        if (Config.USE_CCA_CONNECTORS.get() && ModList.get().isLoaded("createaddition") && !ModList.get().isLoaded("gtceu")) {
            registerTier("Electrum", 32768, 1, 0xF8D86F, ccaTier);
        }

        if (Config.USE_BASE_CONNECTORS.get() && !ModList.get().isLoaded("gtceu")) {
            allTier.addAll(baseTier1);
            allTier.addAll(baseTier2);
            allTier.addAll(ccaTier);
        }

        RELAY = registerOne("Relay", "Powerline", 8, 64, 0, 0, null, 3, 1, 0x663e1d, ConnectorStyle.LARGE);
    }

    public static void getDefaultTranslations(Map<String, String> out) {
        String prefix = "item." + CreatePowerlines.MODID + ".";
        for (WireMaterial material : WireMaterials.MATERIALS.values()) {
            out.put(prefix + material.wireId(), material.display + " Wire");
            out.put(prefix + material.spoolId(), material.display + " Powerlines Spool");
        }
        prefix = "block." + CreatePowerlines.MODID + ".";
        for (ConnectorType type : TYPES) {
            out.put(prefix + type.id, type.display);
        }
    }
}
