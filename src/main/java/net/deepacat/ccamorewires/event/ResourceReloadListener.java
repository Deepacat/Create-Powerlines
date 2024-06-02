package net.deepacat.ccamorewires.event;

import net.deepacat.ccamorewires.sound.CASoundScapes;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class ResourceReloadListener implements ResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        CASoundScapes.invalidateAll();
    }
}
