package net.deepacat.createpowerlines.event;

import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.util.ClientUtil;
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
        if (ClientUtil.getPlayer() == null) return;
        ItemStack stack = ClientUtil.getPlayer().getInventory().getSelected();
        if (stack.isEmpty()) return;
        clientRenderHeldWire = Util.getWireNodeOfSpools(stack) != null;
    }
}