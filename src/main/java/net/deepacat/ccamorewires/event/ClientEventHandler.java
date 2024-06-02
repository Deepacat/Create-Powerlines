package net.deepacat.ccamorewires.event;

import net.deepacat.ccamorewires.CCAMoreWires;

import net.deepacat.ccamorewires.item.WireSpool;
import net.deepacat.ccamorewires.sound.CASoundScapes;
import net.deepacat.ccamorewires.util.ClientMinecraftWrapper;
import net.deepacat.ccamorewires.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CCAMoreWires.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {

    public static boolean clientRenderHeldWire = false;

    @SubscribeEvent
    public static void playerRendererEvent(TickEvent.ClientTickEvent evt) {
        if(ClientMinecraftWrapper.getPlayer() == null) return;
        ItemStack stack = ClientMinecraftWrapper.getPlayer().getInventory().getSelected();
        if(stack.isEmpty()) return;
        if(WireSpool.isRemover(stack.getItem())) return;
        clientRenderHeldWire = Util.getWireNodeOfSpools(stack) != null;
    }

    @SubscribeEvent
    public static void tickSoundscapes(TickEvent.ClientTickEvent event) {
        CASoundScapes.tick();
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void registerReloadListener(RegisterClientReloadListenersEvent event) {
            event.registerReloadListener(new ResourceReloadListener());
        }
    }
}