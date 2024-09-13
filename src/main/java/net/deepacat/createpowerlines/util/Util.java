package net.deepacat.createpowerlines.util;

import net.deepacat.createpowerlines.energy.WireType;
import net.deepacat.createpowerlines.item.WireSpool;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Util {
    public static ItemStack findStack(Item item, Inventory inv) {
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem() == item)
                return stack;
        }
        return ItemStack.EMPTY;
    }

    public static String format(int n) {
        if (n >= 1000_000_000)
            return Math.round((double) n / 100_000_000d) / 10d + "G";
        if (n >= 1000_000)
            return Math.round((double) n / 100_000d) / 10d + "M";
        if (n >= 1000)
            return Math.round((double) n / 100d) / 10d + "K";
        return n + "";
    }

    public static class Triple<A, B, C> {
        public final A a;
        public final B b;
        public final C c;

        private Triple(A a, B b, C c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public static <A, B, C> Triple<A, B, C> of(A a, B b, C c) {
            return new Triple<A, B, C>(a, b, c);
        }
    }

    public static Util.Triple<BlockPos, Integer, WireType> getWireNodeOfSpools(ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            if (stack.isEmpty()) continue;
            if (stack.getTag() == null) continue;
            if (WireSpool.hasPos(stack.getTag())) {
                return Util.Triple.of(WireSpool.getPos(stack.getTag()), WireSpool.getNode(stack.getTag()), WireType.of(stack.getItem()));
            }
        }
        return null;
    }

    public static int byteLerp(int from, int to, int factor) {
        return (from * (255 - factor) + to * factor) / 255;
    }

    public static String displayToId(String id) {
        return id.toLowerCase().replace(' ', '_');
    }
}
