package net.deepacat.createpowerlines.energy;

import net.deepacat.createpowerlines.item.WireMaterial;
import net.deepacat.createpowerlines.item.WireMaterials;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * A class representing a node connected to a {@link IWireNode}.
 */
public class LocalNode {

    public static final String NODES = "nodes";
    public static final String ID = "id";
    public static final String OTHER = "other";
    public static final String MATERIAL = "material";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String Z = "z";

    private final BlockEntity entity;

    /**
     * The index of this node.
     */
    private final int index;
    /**
     * The index of the node this node is connected to.
     */
    private final int otherIndex;
    /**
     * The material of wire used to connect to this node.
     */
    private final WireMaterial wireMaterial;
    /**
     * The relative position of this node from the original block entity.
     */
    private Vec3i relativePos;

    /**
     * Whether this node is invalid.
     */
    private boolean invalid = false;

    public LocalNode(BlockEntity entity, int index, int other, WireMaterial wireMaterial, BlockPos position) {
        this.entity = entity;
        this.index = index;
        this.otherIndex = other;
        this.wireMaterial = wireMaterial;
        this.relativePos = position.subtract(entity.getBlockPos());
    }

    public LocalNode(BlockEntity entity, CompoundTag tag) {
        String materialId = tag.getString(MATERIAL);
        wireMaterial = WireMaterials.MATERIALS.get(materialId);
        if (wireMaterial == null) {
            throw new RuntimeException("Unknown wire material " + materialId);
        }
        this.entity = entity;
        this.index = tag.getInt(ID);
        this.otherIndex = tag.getInt(OTHER);
        this.relativePos = new Vec3i(tag.getInt(X), tag.getInt(Y), tag.getInt(Z));
    }

    public void write(CompoundTag tag) {
        tag.putInt(ID, this.index);
        tag.putInt(OTHER, this.otherIndex);
        tag.putString(MATERIAL, this.wireMaterial.id);
        tag.putInt(X, this.relativePos.getX());
        tag.putInt(Y, this.relativePos.getY());
        tag.putInt(Z, this.relativePos.getZ());
    }

    public void updateRelative(NodeRotation rotation) {
        this.relativePos = rotation.updateRelative(this.relativePos);
    }

    public int getIndex() {
        return index;
    }

    public int getOtherIndex() {
        return otherIndex;
    }

    public WireMaterial getWireMaterial() {
        return wireMaterial;
    }

    public BlockPos getPos() {
        return entity.getBlockPos().offset(this.relativePos);
    }

    public boolean isInvalid() {
        return invalid;
    }

    public void invalid() {
        this.invalid = true;
    }
}
