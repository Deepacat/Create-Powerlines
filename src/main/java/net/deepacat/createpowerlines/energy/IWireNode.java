package net.deepacat.createpowerlines.energy;

import java.util.HashMap;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.deepacat.createpowerlines.blocks.connector.WireMaterial;
import net.deepacat.createpowerlines.blocks.connector.WireMaterials;
import net.deepacat.createpowerlines.energy.network.EnergyNetwork;
import net.deepacat.createpowerlines.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IWireNode {

    /**
     * Get the {@link IWireNode} at the given index.
     *
     * @param index The index of the node.
     * @return The {@link IWireNode} at the given index, or null if the node
     * doesn't exist.
     */
    @Nullable
    IWireNode getWireNode(int index);

    /**
     * Used by {@link IWireNode#getWireNode(int)} to get a cached
     * {@link IWireNode}.
     */
    @Nullable
    static IWireNode getWireNodeFrom(int index, IWireNode obj, LocalNode[] localNodes, IWireNode[] nodeCache,
                                     Level level) {
        if (!obj.hasConnection(index)) return null;
        // Cache the node if it isn't already.
        if (nodeCache[index] == null)
            nodeCache[index] = IWireNode.getWireNode(level, localNodes[index].getPos());
        // If the node is still null, remove it.
        if (nodeCache[index] == null) obj.removeNode(index);
        return nodeCache[index];
    }

    /**
     * Get the {@link LocalNode} for the given index.
     *
     * @param index The index of the node.
     * @return The {@link LocalNode} for the given index, or null if the node
     * doesn't exist.
     */
    @Nullable
    LocalNode getLocalNode(int index);

    /**
     * Create a new node at the given location.
     *
     * @param index        The index of the node to connect.
     * @param other        The index of the node to connect to.
     * @param pos          The position of the node we're connecting to.
     * @param wireMaterial The material of wire we're using.
     */
    void setNode(int index, int other, BlockPos pos, WireMaterial wireMaterial);

    /**
     * Remove the given node.
     *
     * @param index    The index of the node to remove.
     * @param dropWire Whether to drop wires or not.
     */
    void removeNode(int index, boolean dropWire);

    /**
     * Remove the given node.
     *
     * @param index The index of the node to remove.
     */
    default void removeNode(int index) {
        removeNode(index, false);
    }

    /**
     * Remove the given node.
     *
     * @param node     The node to remove.
     * @param dropWire Whether to drop wires or not.
     */
    default void removeNode(LocalNode node, boolean dropWire) {
        removeNode(node.getIndex(), dropWire);
    }

    /**
     * Remove the given node.
     *
     * @param node The node to remove.
     */
    default void removeNode(@NotNull LocalNode node) {
        removeNode(node.getIndex());
    }

    /**
     * Get the number of nodes this {@link IWireNode} supports.
     *
     * @return The number of supported nodes.
     */
    default int getNodeCount() {
        return 1;
    }

    /**
     * Get an available node index from this {@link IWireNode}, based on the
     * given position.
     *
     * @param pos The position we want the node from.
     * @return The node index, or -1 if none are available.
     */
    default int getAvailableNode(Vec3 pos) {
        // before: return 0;
        // Might be a good idea to not return 0 if the method isn't implemented.
        return getAvailableNode();
    }

    /**
     * Get an available node index from this {@link IWireNode}.
     *
     * @return The node index, or -1 if none are available.
     */
    default int getAvailableNode() {
        for (int i = 0; i < getNodeCount(); i++) {
            if (hasConnection(i)) continue;
            return i;
        }
        return -1;
    }

    /**
     * Get the position of the node at the given index.
     *
     * @param index The index of the node.
     * @return The position of the node, or null if the node doesn't exist.
     */
    @Nullable
    default BlockPos getNodePos(int index) {
        LocalNode node = getLocalNode(index);
        if (node == null) return null;
        return node.getPos();
    }

    /**
     * Get the {@link WireMaterial} of the node at the given index.
     *
     * @param index The index of the node.
     * @return The {@link WireMaterial} of the node, or null if the node doesn't exist.
     */
    @Nullable
    default WireMaterial getNodeWireMaterial(int index) {
        LocalNode node = getLocalNode(index);
        if (node == null) return null;
        return node.getWireMaterial();
    }

    /**
     * Get the index of the other node connected to the given index.
     *
     * @param index The index of the node.
     * @return The index of the other node, or -1 if the node doesn't exist.
     */
    default int getOtherNodeIndex(int index) {
        LocalNode node = getLocalNode(index);
        if (node == null) return -1;
        return node.getOtherIndex();
    }

    /**
     * Check if this {@link IWireNode} has a node at the given index.
     *
     * @param index The index of the node to check.
     * @return True if the node exists, false otherwise.
     */
    default boolean hasConnection(int index) {
        return getLocalNode(index) != null;
    }

    /**
     * Check if this {@link IWireNode} has any connection.
     *
     * @return True if any node exists, false otherwise.
     */
    default boolean hasAnyConnection() {
        for (int i = 0; i < getNodeCount(); i++) {
            if (hasConnection(i)) return true;
        }
        return false;
    }

    /**
     * Check if this {@link IWireNode} has a node at the given position.
     *
     * @param pos The position to check.
     * @return True if a node at the given position exists, false otherwise.
     */
    default boolean hasConnectionTo(BlockPos pos) {
        if (pos == null) return false;
        for (int i = 0; i < getNodeCount(); i++) {
            LocalNode node = getLocalNode(i);
            if (node == null) continue;
            if (node.getPos().equals(pos)) return true;
        }
        return false;
    }

    /**
     * Get the {@link LocalNode} at the given position.
     *
     * @param pos The position to get the node from.
     * @return The {@link LocalNode} at the given position, or null if no node
     * exists at the given position.
     */
    @Nullable
    default LocalNode getConnectionTo(BlockPos pos) {
        if (pos == null) return null;
        for (int i = 0; i < getNodeCount(); i++) {
            LocalNode node = getLocalNode(i);
            if (node == null) continue;
            if (node.getPos().equals(pos)) return node;
        }
        return null;
    }

    /**
     * Check if the given node index is an input node.
     *
     * @param index The index of the node to check.
     * @return True if the node is an input node, false otherwise.
     */
    default boolean isNodeInput(int index) {
        return true;
    }

    /**
     * Check if the given node index is an output node.
     *
     * @param node The index of the node to check.
     * @return True if the node is an output node, false otherwise.
     */
    default boolean isNodeOutput(int node) {
        return true;
    }

    Vec3 getNodeOffset(int node);

    /**
     * Get the position of this {@link IWireNode}.
     *
     * @return The position of this {@link IWireNode}.
     */
    BlockPos getPos();

    void invalidateNodeCache();

    // Energy Network

    void setNetwork(int node, EnergyNetwork network);

    EnergyNetwork getNetwork(int node);

    default boolean awakeNetwork(Level world) {
        boolean b = false;
        for (int i = 0; i < getNodeCount(); i++) {
            if (!isNetworkValid(i)) {
                setNetwork(i, EnergyNetwork.nextNode(world, new EnergyNetwork(world), new HashMap<>(), this, i));
                b = true;
            }
        }
        return b;
    }

    default boolean isNetworkValid(int node) {
        if (getNetwork(node) == null)
            return false;
        else
            return getNetwork(node).isValid();
    }

    default boolean isNodeIndeciesConnected(int in, int other) {
        return true;
    }

    // Node Validation

    default boolean validateLocalNodes(LocalNode[] localNodes) {
        boolean changed = false;
        for (int i = 0; i < getNodeCount(); i++) {
            if (localNodes[i] == null) continue;
            IWireNode otherNode = getWireNode(i);
            if (otherNode == null) continue; // getWireNode removes the node if it's null.
            // If the other node exists but isn't connected to us.
            if (!otherNode.hasConnectionTo(getPos())) {
                changed = true;
                localNodes[i] = null;
            }
        }
        return changed;
    }

    // Item

    default void handleWireCache(Level level, Set<LocalNode> toDrop) {
        toDrop.forEach(node -> dropWire(level, node));
        toDrop.clear();
    }

    default void disconnectWires() {
        for (int i = 0; i < getNodeCount(); i++) {
            LocalNode node = getLocalNode(i);
            if (node == null) continue;
            node.invalid();
        }
    }

    default void dropWire(Level world, LocalNode node) {
        ItemStack wire = node.getWireMaterial().wire.asStack();
        node.invalid();
        dropWire(world, getPos(), wire);
    }

    default void dropWires(Level world, boolean dropItems) {
        if (!dropItems) {
            disconnectWires();
            return;
        }
        Reference2IntOpenHashMap<WireMaterial> wires = new Reference2IntOpenHashMap<>();
        for (int i = 0; i < getNodeCount(); i++) {
            LocalNode ln = getLocalNode(i);
            if (ln == null) continue;
            WireMaterial material = ln.getWireMaterial();
            wires.put(material, wires.getInt(material) + 1);
            ln.invalid();
        }
        for (Reference2IntMap.Entry<WireMaterial> drop : wires.reference2IntEntrySet()) {
            dropWire(world, getPos(), drop.getKey().wire.asStack(drop.getIntValue()));
        }
    }

    default void dropWires(Level world, Player player, boolean dropItems) {
        if (!dropItems) {
            disconnectWires();
            return;
        }
        Reference2IntOpenHashMap<WireMaterial> spools = new Reference2IntOpenHashMap<>();
        Reference2IntOpenHashMap<WireMaterial> wires = new Reference2IntOpenHashMap<>();
        for (int i = 0; i < getNodeCount(); i++) {
            LocalNode node = getLocalNode(i);
            if (node == null) continue;
            WireMaterial material = node.getWireMaterial();
            ItemStack emptySpool = Util.findStack(WireMaterials.EMPTY_SPOOL.get().asItem(), player.getInventory());
            if (emptySpool.getCount() > 0) {
                spools.put(material, spools.getInt(material) + 1);
                emptySpool.shrink(1);
            } else {
                wires.put(material, wires.getInt(material) + 1);
            }
            node.invalid();
        }
        for (Reference2IntMap.Entry<WireMaterial> entry : spools.reference2IntEntrySet()) {
            ItemStack stack = entry.getKey().spool.asStack(entry.getIntValue());
            dropWire(world, getPos(), player.getInventory().add(stack) ? ItemStack.EMPTY : stack);
        }
        for (Reference2IntMap.Entry<WireMaterial> entry : wires.reference2IntEntrySet()) {
            ItemStack stack = entry.getKey().wire.asStack(entry.getIntValue());
            dropWire(world, getPos(), player.getInventory().add(stack) ? ItemStack.EMPTY : stack);
        }
    }

    boolean acceptsWireMaterial(WireMaterial wireMaterial);

    int getMaxWireLength();

    // Static Helpers

    static WireConnectResult connect(Level world, BlockPos pos1, int node1, BlockPos pos2, int node2, WireMaterial wireMaterial) {
        BlockEntity te1 = world.getBlockEntity(pos1);
        BlockEntity te2 = world.getBlockEntity(pos2);
        if (te1 == te2 || !(te1 instanceof IWireNode wn1) || !(te2 instanceof IWireNode wn2))
            return WireConnectResult.INVALID;
        if (node1 < 0 || node2 < 0)
            return WireConnectResult.COUNT;
        int maxLength = Math.min(wn1.getMaxWireLength(), wn2.getMaxWireLength());
        if (pos1.distSqr(pos2) > maxLength * maxLength) return WireConnectResult.LONG;
        if (wn1.hasConnectionTo(pos2)) return WireConnectResult.EXISTS;
        if (!(wn1.acceptsWireMaterial(wireMaterial) && wn2.acceptsWireMaterial(wireMaterial)))
            return WireConnectResult.MISMATCHED_WIRE;
        wn1.setNode(node1, node2, wn2.getPos(), wireMaterial);
        wn2.setNode(node2, node1, wn1.getPos(), wireMaterial);
        return WireConnectResult.getLink(wn2.isNodeInput(node2), wn2.isNodeOutput(node2));
    }

    static WireConnectResult disconnect(Level world, BlockPos pos1, BlockPos pos2) {
        BlockEntity te1 = world.getBlockEntity(pos1);
        BlockEntity te2 = world.getBlockEntity(pos2);
        if (te1 == null || te2 == null || te1 == te2)
            return WireConnectResult.INVALID;
        if (!(te1 instanceof IWireNode wn1) || !(te2 instanceof IWireNode wn2))
            return WireConnectResult.INVALID;

        if (!wn1.hasConnectionTo(pos2))
            return WireConnectResult.NO_CONNECTION;

        LocalNode ln1 = wn1.getConnectionTo(pos2);
        LocalNode ln2 = wn2.getConnectionTo(pos1);
        if (ln1 == null || ln2 == null)
            return WireConnectResult.NO_CONNECTION;

        wn1.removeNode(ln1);
        wn2.removeNode(ln2);
        return WireConnectResult.REMOVED;
    }

    @Nullable
    static WireMaterial getWireMaterialOfConnection(Level world, BlockPos pos1, BlockPos pos2) {
        BlockEntity te = world.getBlockEntity(pos1);
        if (!(te instanceof IWireNode wn)) return null;
        LocalNode ln = wn.getConnectionTo(pos2);
        return ln == null ? null : ln.getWireMaterial();
    }

    static IWireNode getWireNode(Level world, BlockPos pos) {
        if (pos == null)
            return null;
        BlockEntity te = world.getBlockEntity(pos);
        if (!(te instanceof IWireNode))
            return null;
        return (IWireNode) te;
    }

    static void dropWire(Level world, BlockPos pos, ItemStack stack) {
        Containers.dropContents(world, pos, NonNullList.of(ItemStack.EMPTY, stack));
    }
}
