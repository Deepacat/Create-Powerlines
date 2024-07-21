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

    static int makeColor(float r, float g, float b) {
        return (int) (r * 255) | ((int) (g * 255) << 8) | ((int) (b * 255) << 16) | 0xFF000000;
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
            int raw = buffer.get(idx);
            float r = (raw & 255) / 255F;
            float g = ((raw >> 8) & 255) / 255F;
            float b = ((raw >> 16) & 255) / 255F;
            float min = Math.min(Math.min(r, g), b);
            float max = Math.max(Math.max(r, g), b);
            float sat = 1 - min / max;
            float color;
            if (max == r) color = (g - b) / (6 * (max - min));
            else if (max == g) color = (b - r) / (6 * (max - min));
            else color = (r - g) / (6 * (max - min));
            color += 1 + type.color;
            color = (color - (int) color) * 6;
            int region = (int) color;
            color -= region;
            float p = max * (1 - sat);
            float q = max * (1 - color * sat);
            float t = max * (1 - (1 - color) * sat);
            raw = switch (region) {
                case 0 -> makeColor(max, t, p);
                case 1 -> makeColor(q, max, p);
                case 2 -> makeColor(p, max, t);
                case 3 -> makeColor(p, q, max);
                case 4 -> makeColor(t, p, max);
                default -> makeColor(max, p, q);
            };
            buffer.put(idx, raw);
        }
        return new SpriteContents(loc, new FrameSize(16, 16), img, AnimationMetadataSection.EMPTY);
    }
}
