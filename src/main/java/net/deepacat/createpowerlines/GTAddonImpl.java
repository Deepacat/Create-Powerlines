package net.deepacat.createpowerlines;

import com.google.common.collect.ImmutableList;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.ItemMaterialInfo;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;
import com.gregtechceu.gtceu.utils.GTUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.deepacat.createpowerlines.blocks.connector.ConnectorStyle;
import net.deepacat.createpowerlines.blocks.connector.ConnectorType;
import net.deepacat.createpowerlines.blocks.connector.ConnectorTypes;
import net.deepacat.createpowerlines.config.Config;
import net.deepacat.createpowerlines.item.WireMaterial;
import net.deepacat.createpowerlines.item.WireMaterials;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@GTAddon
public class GTAddonImpl implements IGTAddon {
    // UHV = 9, above UHV causes max int issues with FE, GTValues.TIER_COUNT for all tiers
    private static final int MAX_SUPPORTED_TIER = GTValues.UHV;
    private static final int[] tierColors = new int[]{0x575351, 0xa7a7a7, 0x7db9d8, 0xededfd, 0xed8eea,
            0x687ece, 0xd1d1d1, 0x323232, 0x578062, 0xFFFFFF, 0x54fc54, 0xfed947, 0x974451, 0x9f2db2, 0x5d5578};

    private ConnectorType[][] connectors;
    private Object2ObjectOpenHashMap<Material, WireMaterial> wireMats;

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
        if (!Config.USE_GT_CONNECTORS.get()) return;

        List<WireMaterial>[] tierWireMats = (List<WireMaterial>[]) new List[MAX_SUPPORTED_TIER + 1];
        for (int i = 0; i <= MAX_SUPPORTED_TIER; ++i) tierWireMats[i] = new ArrayList<>();

        // Converting GT materials with wire property to powerline wires
        wireMats = new Object2ObjectOpenHashMap<>();
        for (Material mat : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            if (mat.hasProperty(PropertyKey.WIRE)) {
                int tier = GTUtil.getTierByVoltage(mat.getProperty(PropertyKey.WIRE).getVoltage());
                if (tier <= MAX_SUPPORTED_TIER) {
                    WireMaterial wireMat = WireMaterials.getOrRegister(
                            WordUtils.capitalizeFully(mat.getName().replace("_", " ")), mat.getMaterialRGB());
                    wireMats.put(mat, wireMat);
                    tierWireMats[tier].add(wireMat);
                }
            }
        }

        // Making connectors from wires
        connectors = new ConnectorType[MAX_SUPPORTED_TIER + 1][];
        List<WireMaterial> acc = new ArrayList<>();
        for (int i = MAX_SUPPORTED_TIER; i >= 0; --i) {
            if (tierWireMats[i].isEmpty()) continue;
            acc.addAll(tierWireMats[i]);
			connectors[i] = new ConnectorType[] {
				ConnectorTypes.registerOne(GTValues.VN[i], "Small",   4, 16,  GTValues.V[i], 4,  List.copyOf(acc), 1, 0, tierColors[i], ConnectorStyle.SMALL),
				ConnectorTypes.registerOne(GTValues.VN[i], "Large",   4, 32,  GTValues.V[i], 6,  List.copyOf(acc), 2, 1, tierColors[i], ConnectorStyle.SMALL),
				ConnectorTypes.registerOne(GTValues.VN[i], "Huge",    3, 64,  GTValues.V[i], 8,  List.copyOf(acc), 3, 1, tierColors[i], ConnectorStyle.LARGE),
				ConnectorTypes.registerOne(GTValues.VN[i], "Giant",   3, 128, GTValues.V[i], 12, List.copyOf(acc), 3, 2, tierColors[i], ConnectorStyle.LARGE),
				ConnectorTypes.registerOne(GTValues.VN[i], "Massive", 2, 256, GTValues.V[i], 16, List.copyOf(acc), 3, 4, tierColors[i], ConnectorStyle.LARGE)
			};
        }
    }

    @Override
    public void addRecipes(Consumer<FinishedRecipe> out) {
        if (connectors == null) return;
//        Base spool recipe
        VanillaRecipeHelper.addShapedRecipe(out,
                new ResourceLocation(CreatePowerlines.MODID, "empty_spool"), WireMaterials.EMPTY_SPOOL.asStack(2),
                "III", " N ", "III",
                'I', new UnificationEntry(TagPrefix.ingot, GTMaterials.Iron),
                'N', new UnificationEntry(TagPrefix.nugget, GTMaterials.Iron)
        );

        for (int i = 0; i <= MAX_SUPPORTED_TIER; ++i) {
            ConnectorType[] connectors = this.connectors[i];
            if (connectors == null) continue;
            ItemMaterialInfo hull = ChemicalHelper.getMaterialInfo(GTMachines.HULL[i].getBlock());
            ImmutableList<MaterialStack> hullMats = hull.getMaterials();

            Material gtWireMat = hullMats.get(1).material();
            Material tierMat = hullMats.get(0).material();

            TagKey<Item> hullPlate = ChemicalHelper.getTag(TagPrefix.plate, tierMat);
            TagKey<Item> wirePlate = ChemicalHelper.getTag(TagPrefix.plate, gtWireMat);

            WireMaterial wireMat = wireMats.get(gtWireMat);
            Object[] circuits = new Object[]{
                    CustomTags.CIRCUITS_ARRAY[i],
                    GTMachines.ENERGY_CONVERTER_1A[i].getItem(),
                    GTMachines.ENERGY_CONVERTER_4A[i].getItem(),
                    GTMachines.ENERGY_CONVERTER_8A[i].getItem(),
                    GTMachines.ENERGY_CONVERTER_16A[i].getItem()
            };
            for (int j = 0; j < connectors.length; ++j) {
                ConnectorType connector = connectors[j];
                VanillaRecipeHelper.addShapedRecipe(out,
                        new ResourceLocation(CreatePowerlines.MODID, connector.id), connector.blockEntry.asStack(),
                        "WWW", "PSP", "PCP",
						'W', wirePlate,
                        'P', hullPlate,
						'S', wireMat.spool.get(), 'C', circuits[j]);
            }
        }

        for (Map.Entry<Material, WireMaterial> entry : wireMats.entrySet()) {
            Material gtWireMat = entry.getKey();
            WireMaterial wireMat = entry.getValue();
            GTRecipeTypes.WIREMILL_RECIPES.recipeBuilder(new ResourceLocation(CreatePowerlines.MODID, "wires/" + wireMat.wireId()))
                    .inputItems(TagPrefix.ingot, gtWireMat)
                    .outputItems(new ItemStack(wireMat.wire.get(), 2))
                    .circuitMeta(5).duration(200).EUt(7).save(out);
            VanillaRecipeHelper.addShapedRecipe(out,
                    new ResourceLocation(CreatePowerlines.MODID, "spools/" + wireMat.spoolId()),
                    new ItemStack(wireMat.spool.get()),
                    "WWW", "WSW", "WWW",
                    'S', WireMaterials.EMPTY_SPOOL.get(),
                    'W', wireMat.wire.get());
        }
    }
}
