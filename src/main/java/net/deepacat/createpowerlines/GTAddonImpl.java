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
//          Converting gt materials with wire property to powerline wires
			for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
				if (mat.hasProperty(PropertyKey.WIRE)) {
					WireMaterials.getOrRegister("GT " + mat.getName(), mat.getMaterialRGB());
					int tier = GTUtil.getTierByVoltage(mat.getProperty(PropertyKey.WIRE).getVoltage());
					tiers[tier].add(mat);
				}
			}
//          Making connectors from wires
			for (int i = 0; i < GTValues.TIER_COUNT; ++i) {
				if (!tiers[i].isEmpty()) {
					Material firstMat = tiers[i].get(0);
					int wireVoltage = firstMat.getProperty(PropertyKey.WIRE).getVoltage();
					ArrayList<WireMaterial> tierWireMats = new ArrayList<>(tiers[i].stream()
							.map(mat -> WireMaterials.getOrRegister(mat.getName(), mat.getMaterialARGB()))
							.toList());
					ConnectorTypes.registerTier(GTValues.VN[i], wireVoltage, (i + 1) * 2 + 2, tiers[i].get(0).getMaterialARGB(), tierWireMats);
				}
			}
		}
	}
}
