package net.deepacat.ccamorewires.network;

import net.minecraft.server.level.ServerPlayer;

public interface IObserveTileEntity {
	void onObserved(ServerPlayer player, ObservePacket pack);
}
