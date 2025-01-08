package net.deepacat.createpowerlines.mixins;

import com.mojang.datafixers.util.Pair;
import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.blocks.connector.ConnectorMode;
import net.deepacat.createpowerlines.blocks.connector.ConnectorType;
import net.deepacat.createpowerlines.blocks.connector.ConnectorTypes;
import net.deepacat.createpowerlines.item.WireMaterial;
import net.deepacat.createpowerlines.item.WireMaterials;
import net.deepacat.createpowerlines.packs.ModResources;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;
import java.util.concurrent.CompletableFuture;

// Slightly cursed

@Mixin(ModelManager.class)
public class ModelManagerMixin {
    @ModifyArg(method = "/lambda\\$loadBlockStates\\$14|m_246572_/", require = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;sequence(Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;"))
    private static List<CompletableFuture<Pair<ResourceLocation, List<ModelBakery.LoadedJson>>>>
    createpowerlines$loadBlockStates(List<CompletableFuture<Pair<ResourceLocation, List<ModelBakery.LoadedJson>>>> list) {
        for (ConnectorType type : ConnectorTypes.TYPES.values()) {
            list.add(CompletableFuture.supplyAsync(() -> {
                ResourceLocation loc = type.getBlockStateLocation().withSuffix(".json");
                ModelBakery.LoadedJson json = new ModelBakery.LoadedJson(CreatePowerlines.MODID, type.genBlockStates());
                return Pair.of(loc, List.of(json));
            }));
        }
        return list;
    }

    private static Pair<ResourceLocation, BlockModel> genFlatItemModel(String id) {
        return Pair.of(new ResourceLocation(CreatePowerlines.MODID, "models/item/" + id + ".json"),
                BlockModel.fromString(ModResources.genFlatItemModel(id)));
    }

    @ModifyArg(method = "/lambda\\$loadBlockModels\\$10|m_245318_/", require = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;sequence(Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;"))
    private static List<CompletableFuture<Pair<ResourceLocation, BlockModel>>>
    createpowerlines$loadBlockModels(List<CompletableFuture<Pair<ResourceLocation, BlockModel>>> list) {
        for (ConnectorType type : ConnectorTypes.TYPES.values()) {
            list.add(CompletableFuture.supplyAsync(() -> {
                ResourceLocation loc = type.getItemModelLocation().withPrefix("models/").withSuffix(".json");
                BlockModel model = BlockModel.fromString(type.genItemModel().toString());
                return Pair.of(loc, model);
            }));
            for (ConnectorMode mode : ConnectorMode.values()) {
                list.add(CompletableFuture.supplyAsync(() -> {
                    ResourceLocation loc = type.getBlockModelLocation(mode).withPrefix("models/").withSuffix(".json");
                    BlockModel model = BlockModel.fromString(type.genBlockModel(mode).toString());
                    return Pair.of(loc, model);
                }));
            }
        }
        for (WireMaterial material : WireMaterials.MATERIALS.values()) {
            list.add(CompletableFuture.supplyAsync(() -> genFlatItemModel(material.wireId())));
            list.add(CompletableFuture.supplyAsync(() -> genFlatItemModel(material.spoolId())));
        }
        return list;
    }
}
