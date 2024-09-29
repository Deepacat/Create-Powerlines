package net.deepacat.createpowerlines.mixins;

import net.deepacat.createpowerlines.blocks.connector.ConnectorMode;
import net.deepacat.createpowerlines.blocks.connector.ConnectorSprite;
import net.deepacat.createpowerlines.blocks.connector.ConnectorType;
import net.deepacat.createpowerlines.blocks.connector.ConnectorTypes;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DirectoryLister.class)
public class DirectorySpriteSourceMixin {
    @Final
    @Shadow
    private String sourcePath;

    @Inject(method = "run", require = 1, at = @At("HEAD"))
    private void createpowerlines$load(ResourceManager resMgr, SpriteSource.Output out, CallbackInfo ci) {
        if (sourcePath.equals("block")) {
            for (ConnectorType type : ConnectorTypes.TYPES) {
                for (ConnectorMode mode : ConnectorMode.values()) {
                    ConnectorSprite sprite = new ConnectorSprite(type, mode);
                    out.add(sprite.loc, sprite);
                }
            }
        }
    }
}
