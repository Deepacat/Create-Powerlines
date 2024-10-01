package net.deepacat.createpowerlines;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import net.deepacat.createpowerlines.config.Config;
import net.deepacat.createpowerlines.item.WireMaterials;

@GTAddon
public class GTAddonImpl implements IGTAddon {
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
        if(Config.USE_GT_CONNECTORS.get()){
            for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
                if (mat.hasProperty(PropertyKey.WIRE)) {
                    WireMaterials.getOrRegister("GT " + mat.getName(), mat.getMaterialRGB());
                }
            }
        }
    }
}
