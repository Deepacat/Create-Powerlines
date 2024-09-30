package net.deepacat.createpowerlines.network;

import java.util.function.Supplier;

import net.deepacat.createpowerlines.CreatePowerlines;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;


public class EnergyNetworkPacket {
	private BlockPos pos;
	private int demand;
	private int buff;
	
	public static double clientSaturation = 0;
	public static int clientDemand = 0;
	public static int clientBuff = 0;
	
	public EnergyNetworkPacket(BlockPos pos, int demand, int buff) {
		this.pos = pos;
		this.demand = demand;
		this.buff = buff;
	}
	
	public static void encode(EnergyNetworkPacket packet, FriendlyByteBuf tag) {
        tag.writeBlockPos(packet.pos);
        tag.writeInt(packet.demand);
        tag.writeInt(packet.buff);
    }
	
	public static EnergyNetworkPacket decode(FriendlyByteBuf buf) {
        return new EnergyNetworkPacket(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }
	
	public static void handle(EnergyNetworkPacket pkt, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			try {
				updateClientCache(pkt.pos, pkt.demand, pkt.buff);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		ctx.get().setPacketHandled(true);
	}
	
	private static void updateClientCache(BlockPos pos, int demand, int buff) {
		clientDemand = demand;
		clientBuff = buff;
		clientSaturation = buff - demand;
    }
	
	public static void send(BlockPos pos, int demand, int buff, ServerPlayer player) {
		CreatePowerlines.Network.send(PacketDistributor.PLAYER.with(() -> player), new EnergyNetworkPacket(pos, demand, buff));
	}
}
