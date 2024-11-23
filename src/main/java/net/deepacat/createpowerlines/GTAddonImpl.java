package net.deepacat.createpowerlines;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.utils.GTUtil;
import net.deepacat.createpowerlines.blocks.connector.ConnectorType;
import net.deepacat.createpowerlines.blocks.connector.ConnectorTypes;
import net.deepacat.createpowerlines.config.Config;
import net.deepacat.createpowerlines.item.WireMaterial;
import net.deepacat.createpowerlines.item.WireMaterials;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

@GTAddon
public class GTAddonImpl implements IGTAddon {
    // UHV = 9, above UHV causes max int issues with FE, GTValues.TIER_COUNT for all tiers
    private static final int MAX_SUPPORTED_TIER = GTValues.UHV;
    private static final int[] tierColors = new int[]{0x575351, 0xa7a7a7, 0x7db9d8, 0xededfd, 0xed8eea,
            0x687ece, 0xd1d1d1, 0x323232, 0x578062, 0xFFFFFF, 0x54fc54, 0xfed947, 0x974451, 0x9f2db2, 0x5d5578};

    private ConnectorType[][] connectorTypes;

    @Override
    public String addonModId() {
        return CreatePowerlines.MODID;
    }

    @Override
    public GTRegistrate getRegistrate() {
        return null;
    }

    @Override
    public void initializeAddon() {
        if (Config.USE_GT_CONNECTORS.get()) {
            List<Material>[] tiers = (List<Material>[]) new List[MAX_SUPPORTED_TIER + 1];
            for (int i = 0; i <= MAX_SUPPORTED_TIER; ++i) tiers[i] = new ArrayList<>();

            // Converting gt materials with wire property to powerline wires
            for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
                if (mat.hasProperty(PropertyKey.WIRE)) {
                    int tier = GTUtil.getTierByVoltage(mat.getProperty(PropertyKey.WIRE).getVoltage());
                    if (tier <= MAX_SUPPORTED_TIER) tiers[tier].add(mat);
                }
            }

            // Making connectors from wires
            connectorTypes = new ConnectorType[MAX_SUPPORTED_TIER + 1][];
            for (int i = 0; i <= MAX_SUPPORTED_TIER; ++i) {
                if (!tiers[i].isEmpty()) {
                    Material firstMat = tiers[i].get(0);
                    long wireVoltage = firstMat.getProperty(PropertyKey.WIRE).getVoltage();
                    List<WireMaterial> tierWireMats = tiers[i].stream().map(mat -> WireMaterials.getOrRegister(
                            WordUtils.capitalizeFully(mat.getName().replace("_", " ")), mat.getMaterialRGB())).toList();
                    connectorTypes[i] = ConnectorTypes.registerTier(GTValues.VN[i], wireVoltage, (i + 1) * 2 + 2, tierColors[i], tierWireMats);
                }
            }
        }
    }
}
