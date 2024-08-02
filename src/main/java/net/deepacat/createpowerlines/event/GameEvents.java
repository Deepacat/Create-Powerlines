package net.deepacat.createpowerlines.event;

import net.deepacat.createpowerlines.debug.CADebugger;
import net.deepacat.createpowerlines.energy.network.EnergyNetworkManager;
import net.deepacat.createpowerlines.network.ObservePacket;

import net.minecraft.core.Direction;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GameEvents {

	@SubscribeEvent
	public static void worldTickEvent(TickEvent.LevelTickEvent evt) {
		if(evt.level.isClientSide()) return;
		if(evt.phase == Phase.END) return;
		EnergyNetworkManager.tickWorld(evt.level);
	}


	@SubscribeEvent
	public static void clientTickEvent(TickEvent.ClientTickEvent evt) {
		if(evt.phase == Phase.START) return;
		ObservePacket.tick();
		CADebugger.tick();
	}

	@SubscribeEvent
	public static void loadEvent(LevelEvent.Load evt) {
		if(evt.getLevel().isClientSide()) return;
		new EnergyNetworkManager(evt.getLevel());
	}

	private static final Direction[] horizontalDirections = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	/*@SubscribeEvent
	public static void grow(BlockEvent.CropGrowEvent.Pre evt) {
		try {
			double chance = 0.001d;
			for(Direction dir : horizontalDirections) {
				BlockState state = evt.getLevel().getBlockState(evt.getPos().relative(dir));
				if(state.is(CABlocks.HARMFUL_PLANT.get()))
					chance *= state.getValue(HarmfulPlantBlock.AGE)*3;
			}
			if(Math.random() < chance)
				evt.getLevel().setBlock(evt.getPos(), CABlocks.HARMFUL_PLANT.getDefaultState(), 3);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}*/
}
