package net.deepacat.createpowerlines.mixins;

import net.deepacat.createpowerlines.blocks.connector.base.ConnectorMode;
import net.deepacat.createpowerlines.blocks.connector.types.ConnectorSprite;
import net.deepacat.createpowerlines.blocks.connector.types.ConnectorType;
import net.deepacat.createpowerlines.blocks.connector.types.ConnectorTypes;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SpriteResourceLoader.class)
public class SpriteResourceLoaderMixin {
    @ModifyVariable(method = "list", require = 1, at = @At(value = "STORE", ordinal = 0))
    private SpriteSource.Output createpowerlines$load(SpriteSource.Output out) {
        for (ConnectorType type : ConnectorTypes.TYPES) {
            for (ConnectorMode mode : ConnectorMode.values()) {
                ConnectorSprite sprite = new ConnectorSprite(type, mode);
                out.add(sprite.loc, sprite);
            }
        }
        return out;
    }
}
