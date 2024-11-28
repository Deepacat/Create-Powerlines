package net.deepacat.createpowerlines.energy.network;

import java.util.Map;

import net.deepacat.createpowerlines.energy.IWireNode;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class EnergyNetwork {
    // Input
    private long inBuff;
    private long inBuffCap;
    // Output
    private long outBuff;
    private long outBuffRetained;
    private boolean valid;

    private int pulled = 0;
    private int pushed = 0;

    public EnergyNetwork(Level world) {
        this.inBuff = 0;
        this.outBuff = 0;
        this.outBuffRetained = 0;
        this.valid = true;

        EnergyNetworkManager.instances.get(world).add(this);
    }

    public void tick() {
        long oldOutBuff = outBuff;
        outBuffRetained = outBuff = inBuff;
        inBuff = oldOutBuff;

        pulled = 0;
        pushed = 0;
    }

    public long getBuff() {
        return outBuffRetained;
    }

    // Returns the amount of energy pushed to network
    public int push(int energy, boolean simulate) {
        energy = (int) Math.max(0, Math.min(inBuffCap - inBuff, energy));
        if (!simulate) {
            inBuff += energy;
            pushed += energy;
        }
        return energy;
    }

    public void push(int energy) {
        push(energy, false);
    }

    public int getPulled() {
        return pulled;
    }

    public int getPushed() {
        return pushed;
    }

    // Returns amount of energy pulled from network
    public int pull(int energy, boolean simulate) {
        int r = (int) Math.max(Math.min(energy, outBuff), 0);
        if (!simulate) {
            outBuff -= r;
            pulled += r;
        }
        return r;
    }

    public int pull(int max) {
        return pull(max, false);
    }

    public static EnergyNetwork nextNode(EnergyNetwork en, Map<String, IWireNode> visited, IWireNode current, int index) {
        if (visited.containsKey(posKey(current.getPos(), index)))
            return null; // should never matter?
        current.setNetwork(index, en);
        visited.put(posKey(current.getPos(), index), current);
        en.inBuffCap += current.getCapacity();

        for (int i = 0; i < current.getNodeCount(); i++) {
            IWireNode next = current.getWireNode(i);
            if (next == null) continue;
            if (!current.isNodeIndeciesConnected(index, i)) continue;
            nextNode(en, visited, next, current.getOtherNodeIndex(i));
        }
        return en;
    }

    private static String posKey(BlockPos pos, int index) {
        return pos.getX() + "," + pos.getY() + "," + pos.getZ() + ":" + index;
    }

    public void invalidate() {
        this.valid = false;
    }

    public boolean isValid() {
        return this.valid;
    }
}
