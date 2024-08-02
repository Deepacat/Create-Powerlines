package net.deepacat.createpowerlines.event;

import net.deepacat.createpowerlines.CreatePowerlines;

import net.deepacat.createpowerlines.item.WireSpool;
import net.deepacat.createpowerlines.util.ClientMinecraftWrapper;
import net.deepacat.createpowerlines.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreatePowerlines.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
    public static boolean clientRenderHeldWire = false;

    @SubscribeEvent
    public static void playerRendererEvent(TickEvent.ClientTickEvent evt) {
        if (ClientMinecraftWrapper.getPlayer() == null) return;
        ItemStack stack = ClientMinecraftWrapper.getPlayer().getInventory().getSelected();
        if (stack.isEmpty()) return;
        if (WireSpool.isRemover(stack.getItem())) return;
        clientRenderHeldWire = Util.getWireNodeOfSpools(stack) != null;
    }
}