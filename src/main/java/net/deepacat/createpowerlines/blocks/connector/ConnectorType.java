package net.deepacat.createpowerlines.blocks.connector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.utility.VoxelShaper;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.energy.NodeMovementBehaviour;
import net.deepacat.createpowerlines.item.WireMaterial;
import net.deepacat.createpowerlines.shapes.CAShapes;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ConnectorType {
    public final String id;
    public final String display;
    public final int connections;
    public final int wireLength;
    public final int energyRate;
    public final Set<WireMaterial> wireMaterials;
    public final int width, height;
    public final int color;
    public final ConnectorStyle style;

    public final VoxelShaper shape;
    public final BlockEntry<ConnectorBlock> blockEntry;
    public final BlockEntityEntry<ConnectorBlockEntity> beEntry;

    public ConnectorType(String id, String display, int connections, int wireLength, int energyRate,
                         List<WireMaterial> wireMaterials, int width, int height, int color, ConnectorStyle style) {
        this.id = id;
        this.display = display;
        this.connections = connections;
        this.wireLength = wireLength;
        this.energyRate = energyRate;
        this.wireMaterials = new ReferenceOpenHashSet<>(wireMaterials);
        this.width = width;
        this.height = height;
        this.color = color;
        this.style = style;
        int min = 8 - width - 1;
        int max = 8 + width + 1;

        shape = CAShapes.shape(min, 0, min, max, style.baseHeight + height, max).forDirectional();
        blockEntry = CreatePowerlines.REGISTRATE.block(id, props -> new ConnectorBlock(props, this))
                .initialProperties(SharedProperties::stone)
                .onRegister(AllMovementBehaviours.movementBehaviour(new NodeMovementBehaviour()))
                .item()
                .transform(customItemModel())
                .defaultLang()
                .register();
        beEntry = CreatePowerlines.REGISTRATE
                .<ConnectorBlockEntity>blockEntity(id, (beType, pos, state) -> new ConnectorBlockEntity(beType, pos, state, this))
                .validBlocks(blockEntry)
                .renderer(() -> ConnectorRenderer::new)
                .register();
    }

    static final IntIntPair[] ROTS = new IntIntPair[]{
            IntIntPair.of(0, 90),
            IntIntPair.of(180, 90),
            IntIntPair.of(90, 180),
            IntIntPair.of(90, 0),
            IntIntPair.of(90, 90),
            IntIntPair.of(90, 270),
    };

    public ResourceLocation getBlockModelLocation(ConnectorMode mode) {
        return new ResourceLocation(CreatePowerlines.MODID, "block/" + id + "/" + mode.getSerializedName());
    }

    public ResourceLocation getBlockTextureLocation(ConnectorMode mode) {
        return new ResourceLocation(CreatePowerlines.MODID, "block/" + id + "/" + style.name + "_" + mode.getSerializedName());
    }

    public ResourceLocation getItemModelLocation() {
        return new ResourceLocation(CreatePowerlines.MODID, "item/" + id);
    }

    public ResourceLocation getBlockStateLocation() {
        return new ResourceLocation(CreatePowerlines.MODID, "blockstates/" + id);
    }

    public JsonElement genBlockStates() {
        JsonObject root = new JsonObject();
        JsonArray parts = new JsonArray();
        for (Direction dir : Direction.values()) {
            JsonObject apply = new JsonObject();
            IntIntPair rot = ROTS[dir.get3DDataValue()];
            apply.addProperty("x", rot.firstInt());
            apply.addProperty("y", rot.secondInt());
            for (ConnectorMode mode : ConnectorMode.values()) {
                JsonObject part = new JsonObject();
                JsonObject when = new JsonObject();
                when.addProperty("facing", dir.getSerializedName());
                when.addProperty("mode", mode.getSerializedName());
                part.add("when", when);
                apply.addProperty("model", getBlockModelLocation(mode).toString());
                part.add("apply", apply);
                apply = apply.deepCopy();
                parts.add(part);
            }
            JsonObject part = new JsonObject();
            JsonObject when = new JsonObject();
            when.addProperty("facing", dir.getSerializedName());
            when.addProperty("variant", ConnectorVariant.Girder.getSerializedName());
            part.add("when", when);
            apply.addProperty("model", new ResourceLocation(CreatePowerlines.MODID, "block/girder_base").toString());
            part.add("apply", apply);
            parts.add(part);
        }
        root.add("multipart", parts);
        return root;
    }

    static private JsonArray intArray(int... values) {
        JsonArray array = new JsonArray();
        for (int value : values) array.add(value);
        return array;
    }

    static private void addModelFace(JsonObject faces, Direction dir, int u0, int v0, int u1, int v1, boolean cull) {
        JsonObject face = new JsonObject();
        face.addProperty("texture", "#0");
        face.add("uv", intArray(u0, v0, u1, v1));
        if (cull) {
            face.addProperty("cullface", dir.getSerializedName());
        }
        faces.add(dir.getSerializedName(), face);
    }

    private void addModelElement(JsonArray elements, int yn, int yp, int expand, Consumer<JsonObject> faceGen) {
        JsonObject element = new JsonObject();
        int min = 8 - width - expand;
        int max = 8 + width + expand;
        element.add("from", intArray(min, yn, min));
        element.add("to", intArray(max, yp, max));
        JsonObject faces = new JsonObject();
        faceGen.accept(faces);
        element.add("faces", faces);
        elements.add(element);
    }

    public JsonElement genBlockModel(ConnectorMode mode) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/block");
        JsonObject textures = new JsonObject();
        textures.addProperty("0", getBlockTextureLocation(mode).toString());
        textures.addProperty("particle", "create:block/andesite_casing");
        root.add("textures", textures);
        JsonArray elements = new JsonArray();
        switch (style) {
            case SMALL -> {
                addModelElement(elements, 1, style.baseHeight + height, 0, (faces) -> {
                    addModelFace(faces, Direction.NORTH, 2, 0, 4, 4, false);
                    addModelFace(faces, Direction.EAST, 0, 0, 2, 4, false);
                    addModelFace(faces, Direction.SOUTH, 2, 0, 4, 4, false);
                    addModelFace(faces, Direction.WEST, 0, 0, 2, 4, false);
                    addModelFace(faces, Direction.UP, 5, 9, 7, 11, false);
                });
                addModelElement(elements, 0, 2 + height / 3, 1, (faces) -> {
                    addModelFace(faces, Direction.NORTH, 4, 6, 8, 8, false);
                    addModelFace(faces, Direction.EAST, 0, 6, 4, 8, false);
                    addModelFace(faces, Direction.SOUTH, 4, 6, 8, 8, false);
                    addModelFace(faces, Direction.WEST, 0, 6, 4, 8, false);
                    addModelFace(faces, Direction.UP, 0, 8, 4, 12, false);
                    addModelFace(faces, Direction.DOWN, 0, 12, 4, 16, true);
                });
                addModelElement(elements, 3 + height / 2, 4 + height, 1, (faces) -> {
                    addModelFace(faces, Direction.NORTH, 4, 4, 8, 5, false);
                    addModelFace(faces, Direction.EAST, 0, 4, 4, 5, false);
                    addModelFace(faces, Direction.SOUTH, 4, 4, 8, 5, false);
                    addModelFace(faces, Direction.WEST, 0, 4, 4, 5, false);
                    addModelFace(faces, Direction.UP, 4, 8, 8, 12, false);
                    addModelFace(faces, Direction.DOWN, 4, 12, 8, 16, false);
                });
            }
            case LARGE -> {
                addModelElement(elements, 2, style.baseHeight + height, 0, (faces) -> {
                    addModelFace(faces, Direction.NORTH, 0, 0, 4, 5, false);
                    addModelFace(faces, Direction.EAST, 0, 0, 4, 5, false);
                    addModelFace(faces, Direction.SOUTH, 0, 0, 4, 5, false);
                    addModelFace(faces, Direction.WEST, 0, 0, 4, 5, false);
                    addModelFace(faces, Direction.UP, 5, 5, 9, 9, false);
                });
                addModelElement(elements, 0, 2 + height / 4, 1, (faces) -> {
                    addModelFace(faces, Direction.NORTH, 10, 2, 16, 4, false);
                    addModelFace(faces, Direction.EAST, 4, 2, 10, 4, false);
                    addModelFace(faces, Direction.SOUTH, 10, 2, 16, 4, false);
                    addModelFace(faces, Direction.WEST, 4, 2, 10, 4, false);
                    addModelFace(faces, Direction.UP, 10, 4, 16, 10, false);
                    addModelFace(faces, Direction.DOWN, 10, 10, 16, 16, true);
                });
                addModelElement(elements, 3 + height / 3, 4 + height / 2, 1, (faces) -> {
                    addModelFace(faces, Direction.NORTH, 4, 9, 10, 10, false);
                    addModelFace(faces, Direction.EAST, 4, 4, 10, 5, false);
                    addModelFace(faces, Direction.SOUTH, 4, 9, 10, 10, false);
                    addModelFace(faces, Direction.WEST, 4, 4, 10, 5, false);
                    addModelFace(faces, Direction.UP, 4, 4, 10, 10, false);
                    addModelFace(faces, Direction.DOWN, 4, 10, 10, 16, false);
                });
                addModelElement(elements, 5 + height / 2, 6 + height, 1, (faces) -> {
                    addModelFace(faces, Direction.NORTH, 4, 9, 10, 10, false);
                    addModelFace(faces, Direction.EAST, 4, 4, 10, 5, false);
                    addModelFace(faces, Direction.SOUTH, 4, 9, 10, 10, false);
                    addModelFace(faces, Direction.WEST, 4, 4, 10, 5, false);
                    addModelFace(faces, Direction.UP, 4, 4, 10, 10, false);
                    addModelFace(faces, Direction.DOWN, 4, 10, 10, 16, false);
                });
            }
        }
        root.add("elements", elements);
        return root;
    }

    public JsonElement genItemModel() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", getBlockModelLocation(ConnectorMode.None).toString());
        return root;
    }
}
