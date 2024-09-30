package net.deepacat.createpowerlines.util;

import net.deepacat.createpowerlines.item.WireMaterial;
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
            return new Triple<>(a, b, c);
        }
    }

    public static Util.Triple<BlockPos, Integer, WireMaterial> getWireNodeOfSpools(ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            if (stack.isEmpty()) continue;
            if (!(stack.getItem() instanceof WireSpool spool)) continue;
            if (spool.material == null) continue;
            if (!WireSpool.hasPos(stack.getTag())) continue;
            return Util.Triple.of(WireSpool.getPos(stack.getTag()), WireSpool.getNode(stack.getTag()), spool.material);
        }
        return null;
    }

    public static int byteLerp(int from, int to, int factor) {
        return (from * (255 - factor) + to * factor) / 255;
    }

    public static int byteScale(int x, int y) {
        return x * y / 255;
    }

    public static int tintColor(int base, int tint) {
        int r = byteScale(base & 0xFF, tint >> 16);
        int g = byteScale((base >> 8) & 0xFF, (tint >> 8) & 0xFF);
        int b = byteScale((base >> 16) & 0xFF, tint & 0xFF);
        return r | (g << 8) | (b << 16) | (base & 0xFF000000);
    }

    public static int blendColor(int dst, int src) {
        int factor = (src >> 24) & 0xFF;
        int r = byteLerp(dst & 0xFF, src & 0xFF, factor);
        int g = byteLerp((dst >> 8) & 0xFF, (src >> 8) & 0xFF, factor);
        int b = byteLerp((dst >> 16) & 0xFF, (src >> 16) & 0xFF, factor);
        int a = byteLerp((dst >> 24) & 0xFF, factor, factor);
        return r | (g << 8) | (b << 16) | (a << 24);
    }

    public static String displayToId(String id) {
        return id.toLowerCase().replace(' ', '_');
    }
}
