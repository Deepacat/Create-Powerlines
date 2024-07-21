package net.deepacat.ccamorewires.blocks.connector.types;

import com.mojang.blaze3d.platform.NativeImage;
import net.deepacat.ccamorewires.CCAMoreWires;
import net.deepacat.ccamorewires.blocks.connector.base.ConnectorMode;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;

public class ConnectorSprite implements SpriteSource.SpriteSupplier {
    private final static int[] INDICES = new int[]{
            0, 1, 2, 3, 16, 17, 18, 19, 32, 33, 34, 35, 48, 49, 50, 51,
            149, 150, 165, 166, 209, 210, 213, 214, 225, 226, 229, 230
    };

    private final ConnectorType type;
    private final ConnectorMode mode;
    public final ResourceLocation loc;

    public ConnectorSprite(ConnectorType type, ConnectorMode mode) {
        this.type = type;
        this.mode = mode;
        this.loc = type.getBlockModelLocation(mode);
    }

    static int intLerp(int from, int to, int factor) {
        return (from * (255 - factor) + to * factor) / 255;
    }

    static int mapPixel(int src, int color) {
        int dark = ((src >> 8) & 255) / 3;
        int bright = src & 255;
        int r = intLerp(dark, bright, color >> 16);
        int g = intLerp(dark, bright, (color >> 8) & 255);
        int b = intLerp(dark, bright, color & 255);
        return r | (g << 8) | (b << 16) | 0xFF000000;
    }

    @Override
    public SpriteContents get() {
        NativeImage img;
        try {
            try (InputStream is = CCAMoreWires.class.getResourceAsStream("/assets/ccawires/textures/connector/" + mode.getSerializedName() + ".png")) {
                img = NativeImage.read(is);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IntBuffer buffer = MemoryUtil.memIntBuffer(img.pixels, 256);
        for (int idx : INDICES) {
            buffer.put(idx, mapPixel(buffer.get(idx), type.color));
        }
        return new SpriteContents(loc, new FrameSize(16, 16), img, AnimationMetadataSection.EMPTY);
    }
}
