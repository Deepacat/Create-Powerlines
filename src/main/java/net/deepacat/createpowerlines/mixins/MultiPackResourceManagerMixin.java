package net.deepacat.createpowerlines.mixins;

import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.packs.ModResources;
import net.deepacat.createpowerlines.packs.VanillaData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(MultiPackResourceManager.class)
public class MultiPackResourceManagerMixin {
    @Shadow
    @Final
    private Map<String, FallbackResourceManager> namespacedManagers;

    @Inject(method = "<init>", at = @At(value = "RETURN"), require = 1)
    private void ctor(PackType type, List<PackResources> packs, CallbackInfo ci) {
        if (type == PackType.SERVER_DATA) {
            FallbackResourceManager mgr = namespacedManagers.get(ResourceLocation.DEFAULT_NAMESPACE);
            mgr.push(new VanillaData());
        } else {
            FallbackResourceManager mgr = namespacedManagers.get(CreatePowerlines.MODID);
            if (mgr != null) mgr.push(new ModResources());
        }
    }
}
