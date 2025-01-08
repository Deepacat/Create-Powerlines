package net.deepacat.createpowerlines.packs;

import com.google.gson.JsonObject;
import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.blocks.connector.ConnectorMode;
import net.deepacat.createpowerlines.blocks.connector.ConnectorType;
import net.deepacat.createpowerlines.blocks.connector.ConnectorTypes;
import net.deepacat.createpowerlines.item.WireMaterial;
import net.deepacat.createpowerlines.item.WireMaterials;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

// Slightly less cursed

public class ModResources implements PackResources {
    @Override
    public boolean isBuiltin() {
        return true;
    }

    private static String tryRemovePrefix(String value, String prefix) {
        return value.startsWith(prefix) ? value.substring(prefix.length()) : null;
    }

    private static String tryRemoveSuffix(String value, String suffix) {
        return value.endsWith(suffix) ? value.substring(0, value.length() - suffix.length()) : null;
    }

    public static String genFlatItemModel(String id) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", CreatePowerlines.MODID + ":item/" + id);
        root.add("textures", textures);
        return root.toString();
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation loc) {
        String path = tryRemoveSuffix(loc.getPath(), ".json");
        if (path == null) return null;
        String id = tryRemovePrefix(path, "models/item/");
        if (id != null) {
            String matId = tryRemoveSuffix(id, WireMaterial.WIRE_SUFFIX);
            if (matId == null) matId = tryRemoveSuffix(id, WireMaterial.SPOOL_SUFFIX);
            if (matId != null) {
                WireMaterial mat = WireMaterials.MATERIALS.get(matId);
                if (mat == null) return null;
                String data = genFlatItemModel(id);
                return () -> new ByteArrayInputStream(data.getBytes());
            }
            ConnectorType type = ConnectorTypes.TYPES.get(id);
            if (type == null) return null;
            return () -> new ByteArrayInputStream(type.genItemModel().toString().getBytes());
        }
        id = tryRemovePrefix(path, "models/block/");
        if (id != null) {
            String[] parts = id.split("/");
            if (parts.length != 2) return null;
            ConnectorType type = ConnectorTypes.TYPES.get(parts[0]);
            if (type == null) return null;
            ConnectorMode mode = ConnectorMode.CODEC.byName(parts[1]);
            if (mode == null) return null;
            return () -> new ByteArrayInputStream(type.genBlockModel(mode).toString().getBytes());
        }
        id = tryRemovePrefix(path, "blockstates/");
        if (id == null) return null;
        ConnectorType type = ConnectorTypes.TYPES.get(id);
        if (type == null) return null;
        return () -> new ByteArrayInputStream(type.genBlockStates().toString().getBytes());
    }

    @Override
    public void listResources(PackType type, String ns, String path, ResourceOutput out) {
    }

    @Override
    public IoSupplier<InputStream> getRootResource(String... names) {
        return null;
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return Set.of(CreatePowerlines.MODID);
    }

    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> ser) {
        return null;
    }

    @Override
    public String packId() {
        return CreatePowerlines.MODID;
    }

    @Override
    public void close() {
    }
}
