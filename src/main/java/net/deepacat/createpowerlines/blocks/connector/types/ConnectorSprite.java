package net.deepacat.createpowerlines.blocks.connector.types;

import com.mojang.blaze3d.platform.NativeImage;
import net.deepacat.createpowerlines.blocks.connector.base.ConnectorMode;
import net.deepacat.createpowerlines.util.ClientUtil;
import net.deepacat.createpowerlines.util.Util;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class ConnectorSprite implements SpriteSource.SpriteSupplier {
    private final ConnectorType type;
    private final ConnectorMode mode;
    public final ResourceLocation loc;

    public ConnectorSprite(ConnectorType type, ConnectorMode mode) {
        this.type = type;
        this.mode = mode;
        this.loc = type.getBlockTextureLocation(mode);
    }

    static int mapPixel(int src, int color) {
        int dark = ((src >> 8) & 255) / 3;
        int bright = src & 255;
        int r = Util.byteLerp(dark, bright, color >> 16);
        int g = Util.byteLerp(dark, bright, (color >> 8) & 255);
        int b = Util.byteLerp(dark, bright, color & 255);
        return r | (g << 8) | (b << 16) | 0xFF000000;
    }

    @Override
    public SpriteContents get() {
        NativeImage img = ClientUtil.loadTextureTemplate(type.style.name + "_" + mode.getSerializedName());
        IntBuffer buffer = MemoryUtil.memIntBuffer(img.pixels, 256);
        for (int idx : type.style.indices) {
            buffer.put(idx, mapPixel(buffer.get(idx), type.color));
        }
        return new SpriteContents(loc, new FrameSize(16, 16), img, AnimationMetadataSection.EMPTY, null);
    }
}
