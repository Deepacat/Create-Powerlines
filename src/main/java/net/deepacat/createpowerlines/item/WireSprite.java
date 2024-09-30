package net.deepacat.createpowerlines.item;

import com.mojang.blaze3d.platform.NativeImage;
import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.util.ClientUtil;
import net.deepacat.createpowerlines.util.Util;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;

public class WireSprite implements SpriteSource.SpriteSupplier {
    public final WireMaterial material;
    public final ResourceLocation loc;

    public WireSprite(WireMaterial material) {
        this.material = material;
        loc = new ResourceLocation(CreatePowerlines.MODID, "item/" + material.wireId());
    }

    @Override
    public SpriteContents get() {
        NativeImage img = ClientUtil.loadNativeImage("template/wire");
        img.applyToAllPixels(pixel -> Util.tintColor(pixel, material.color));
        return new SpriteContents(loc, new FrameSize(16, 16), img, AnimationMetadataSection.EMPTY, null);
    }
}
