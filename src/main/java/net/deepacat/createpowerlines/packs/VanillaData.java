package net.deepacat.createpowerlines.packs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.blocks.connector.ConnectorType;
import net.deepacat.createpowerlines.blocks.connector.ConnectorTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

public class VanillaData implements PackResources {
    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public void listResources(PackType packType, String ns, String path, ResourceOutput out) {
        if (path.equals("tags/blocks")) {
            JsonObject root = new JsonObject();
            JsonArray values = new JsonArray();
            for (ConnectorType type : ConnectorTypes.TYPES.values()) {
                values.add(type.blockEntry.getId().toString());
            }
            root.add("values", values);
            byte[] data = root.toString().getBytes();
            out.accept(new ResourceLocation("tags/blocks/mineable/pickaxe.json"),
                    () -> new ByteArrayInputStream(data));
        }
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation loc) {
        return null;
    }

    @Override
    public IoSupplier<InputStream> getRootResource(String... names) {
        return null;
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return Set.of(ResourceLocation.DEFAULT_NAMESPACE);
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
