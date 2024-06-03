package net.deepacat.ccamorewires.index;

import net.deepacat.ccamorewires.CCAMoreWires;
import net.deepacat.ccamorewires.item.WireSpool;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.item.Item;


public class CAItems {

	static {
		CCAMoreWires.REGISTRATE.setCreativeTab(CACreativeModeTabs.MAIN_TAB);
	}

	public static final ItemEntry<Item> COPPER_WIRE =
			CCAMoreWires.REGISTRATE.item("copper_wire", Item::new).register();
	public static final ItemEntry<Item> GOLD_WIRE =
			CCAMoreWires.REGISTRATE.item("gold_wire", Item::new).register();
	public static final ItemEntry<Item> ELECTRUM_WIRE =
			CCAMoreWires.REGISTRATE.item("electrum_wire", Item::new).register();

	public static final ItemEntry<WireSpool> SPOOL =
			CCAMoreWires.REGISTRATE.item("spool", WireSpool::new).register();
	public static final ItemEntry<WireSpool> COPPER_SPOOL =
			CCAMoreWires.REGISTRATE.item("copper_spool", WireSpool::new).register();

	public static final ItemEntry<WireSpool> GOLD_SPOOL =
			CCAMoreWires.REGISTRATE.item("gold_spool", WireSpool::new).register();
	public static final ItemEntry<WireSpool> ELECTRUM_SPOOL =
			CCAMoreWires.REGISTRATE.item("electrum_spool", WireSpool::new).register();

	public static void register() {

	}
}
