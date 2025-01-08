package net.deepacat.createpowerlines.item;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.blocks.connector.ConnectorMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class WireMaterial {
    public final static String WIRE_SUFFIX = "_wire";
    public final static String SPOOL_SUFFIX = "_spool";

    public final String id;
    public final String display;
    public final int color;
    public final ItemEntry<Item> wire;
    public final ItemEntry<WireSpool> spool;

    public WireMaterial(String id, String display, int color) {
        this.id = id;
        this.display = display;
        this.color = color;
        wire = CreatePowerlines.REGISTRATE.item(wireId(), Item::new).register();
        spool = CreatePowerlines.REGISTRATE.item(spoolId(), (props) -> new WireSpool(props, this)).register();
    }

    public String wireId() {
        return id + WIRE_SUFFIX;
    }

    public String spoolId() {
        return id + SPOOL_SUFFIX;
    }
}
