package net.deepacat.createpowerlines;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.utils.GTUtil;
import net.deepacat.createpowerlines.blocks.connector.ConnectorTypes;
import net.deepacat.createpowerlines.config.Config;
import net.deepacat.createpowerlines.item.WireMaterial;
import net.deepacat.createpowerlines.item.WireMaterials;

import java.util.ArrayList;

@GTAddon
public class GTAddonImpl implements IGTAddon {

    public static WireMaterial[] getWireMatFromGTMat(Material gtMat){

    }

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
            ArrayList<Material>[] tiers = (ArrayList<Material>[]) new ArrayList[GTValues.TIER_COUNT];
            for (int i = 0; i < GTValues.TIER_COUNT; ++i) {
                tiers[i] = new ArrayList();
            }

            for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
                if (mat.hasProperty(PropertyKey.WIRE)) {
                    WireMaterials.getOrRegister("GT " + mat.getName(), mat.getMaterialRGB());
                    int tier = GTUtil.getTierByVoltage(mat.getProperty(PropertyKey.WIRE).getVoltage());
//                    CreatePowerlines.LOGGER.warn("voltage" + " " + mat.getName() + " " + GTValues.VN[tier]);
                    tiers[tier].add(mat);
//                    CreatePowerlines.LOGGER.warn(GTValues.VN[tier] + " " + tiers[tier]);
                }
            }

            for (int i = 0; i < GTValues.TIER_COUNT; ++i) {
//                CreatePowerlines.LOGGER.warn(GTValues.VN[i] + " voltetestsete" + tiers[i].toString());
                 if(!tiers[i].isEmpty()) {
                    Material firstMat = tiers[i].get(0);
                    int wireVoltage = firstMat.getProperty(PropertyKey.WIRE).getVoltage();
//                    CreatePowerlines.LOGGER.warn(" " + tiers[i].get(0));
                    WireMaterials.getOrRegister(firstMat.getName(), firstMat.getMaterialRGB());
                    ConnectorTypes.registerTier(GTValues.VN[i], wireVoltage, (i+1)*2+2, tiers[i].get(0).getMaterialRGB(), getWireMatFromGTMat(tiers[i].get(0)));
                }
            }
        }

//        ConnectorTypes.registerTier("LV", 32, 4, 0xa7a7a7, tierLv);
//        ConnectorTypes.registerTier("MV", 128, 6, 0x7db9d8, tierLv);
//        ConnectorTypes.registerTier("HV", 512, 8, 0xededfd, tierLv);
//        ConnectorTypes.registerTier("EV", 2048, 10, 0xed8eea, tierLv);
//        ConnectorTypes.registerTier("IV", 8192, 12, 0x687ece, tierLv);
//        ConnectorTypes.registerTier("LuV", 32768, 14, 0xd1d1d1, tierLv);
//        ConnectorTypes.registerTier("ZPM", 131072, 16, 0x323232, tierLv);
//        ConnectorTypes.registerTier("UV", 524288, 18, 0x578062, tierLv);
//        ConnectorTypes.registerTier("UHV", 2097152, 20, 0xFFFFFF, tierLv);
    }
}
