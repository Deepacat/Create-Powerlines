package net.deepacat.createpowerlines.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class ClientUtil {
	public static LocalPlayer getPlayer() {
		return Minecraft.getInstance().player;
	}
}
