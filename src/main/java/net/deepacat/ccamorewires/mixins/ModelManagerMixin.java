package net.deepacat.ccamorewires.mixins;

import com.mojang.datafixers.util.Pair;
import net.deepacat.ccamorewires.CCAMoreWires;
import net.deepacat.ccamorewires.blocks.connector.base.ConnectorMode;
import net.deepacat.ccamorewires.blocks.connector.types.ConnectorType;
import net.deepacat.ccamorewires.blocks.connector.types.ConnectorTypes;
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
    ccawires$loadBlockStates(List<CompletableFuture<Pair<ResourceLocation, List<ModelBakery.LoadedJson>>>> list) {
        for (ConnectorType type : ConnectorTypes.TYPES) {
            list.add(CompletableFuture.supplyAsync(() -> {
                ResourceLocation loc = type.getBlockStateLocation().withSuffix(".json");
                ModelBakery.LoadedJson json = new ModelBakery.LoadedJson(CCAMoreWires.MODID, type.genBlockStates());
                return Pair.of(loc, List.of(json));
            }));
        }
        return list;
    }

    @ModifyArg(method = "/lambda\\$loadBlockModels\\$10|m_245318_/", require = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;sequence(Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;"))
    private static List<CompletableFuture<Pair<ResourceLocation, BlockModel>>>
    ccawires$loadBlockModels(List<CompletableFuture<Pair<ResourceLocation, BlockModel>>> list) {
        for (ConnectorType type : ConnectorTypes.TYPES) {
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
        return list;
    }
}
