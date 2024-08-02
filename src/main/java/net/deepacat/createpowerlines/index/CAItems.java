package net.deepacat.createpowerlines.index;

import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.item.WireSpool;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.item.Item;


public class CAItems {

    static {
        CreatePowerlines.REGISTRATE.setCreativeTab(CACreativeModeTabs.MAIN_TAB);
    }

    public static final ItemEntry<Item> COPPER_WIRE =
            CreatePowerlines.REGISTRATE.item("copper_wire", Item::new).register();
    public static final ItemEntry<Item> GOLD_WIRE =
            CreatePowerlines.REGISTRATE.item("gold_wire", Item::new).register();
    public static final ItemEntry<Item> ELECTRUM_WIRE =
            CreatePowerlines.REGISTRATE.item("electrum_wire", Item::new).register();

    public static final ItemEntry<WireSpool> SPOOL =
            CreatePowerlines.REGISTRATE.item("spool", WireSpool::new).register();
    public static final ItemEntry<WireSpool> COPPER_SPOOL =
            CreatePowerlines.REGISTRATE.item("copper_spool", WireSpool::new).register();
    public static final ItemEntry<WireSpool> GOLD_SPOOL =
            CreatePowerlines.REGISTRATE.item("gold_spool", WireSpool::new).register();
    public static final ItemEntry<WireSpool> ELECTRUM_SPOOL =
            CreatePowerlines.REGISTRATE.item("electrum_spool", WireSpool::new).register();

    public static void register() {

    }
}
