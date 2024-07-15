package net.deepacat.ccamorewires.rendering;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

public class CARenderType extends RenderType {
    private CARenderType() { super(null, null, null, 0, false, false, null, null); }

    public static final RenderType WIRE = create("wire", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP,
            VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().
                    setShaderState(RENDERTYPE_LEASH_SHADER).setTextureState(NO_TEXTURE)
                    .setCullState(NO_CULL).setLightmapState(LIGHTMAP).createCompositeState(false));
}
