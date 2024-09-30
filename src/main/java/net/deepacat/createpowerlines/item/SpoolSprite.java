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
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class SpoolSprite implements SpriteSource.SpriteSupplier {
    public final WireMaterial material;
    public final ResourceLocation loc;

    public SpoolSprite(WireMaterial material) {
        this.material = material;
        loc = new ResourceLocation(CreatePowerlines.MODID, "item/" + material.spoolId());
    }

    @Override
    public SpriteContents get() {
        NativeImage base = ClientUtil.loadNativeImage("item/spool");
        NativeImage overlay = ClientUtil.loadNativeImage("template/spool_overlay");
        IntBuffer baseBuffer = MemoryUtil.memIntBuffer(base.pixels, 256);
        IntBuffer overlayBuffer = MemoryUtil.memIntBuffer(overlay.pixels, 256);
        for (int i = 0; i != 256; ++i) {
            baseBuffer.put(i, Util.blendColor(baseBuffer.get(i), Util.tintColor(overlayBuffer.get(i), material.color)));
        }
        overlay.close();
        return new SpriteContents(loc, new FrameSize(16, 16), base, AnimationMetadataSection.EMPTY, null);
    }
}
