package net.deepacat.createpowerlines.blocks.connector;

import com.tterrag.registrate.util.entry.ItemEntry;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.deepacat.createpowerlines.CreatePowerlines;
import net.deepacat.createpowerlines.item.WireSpool;
import net.deepacat.createpowerlines.util.Util;

import java.util.Map;

public class WireMaterials {
    public static final Map<String, WireMaterial> MATERIALS = new Object2ObjectOpenHashMap<>();
    public static final ItemEntry<WireSpool> EMPTY_SPOOL =
            CreatePowerlines.REGISTRATE.item("spool", (props) -> new WireSpool(props, null)).register();

    public static WireMaterial getOrRegister(String display, int color) {
        String id = Util.displayToId(display);
        WireMaterial material = MATERIALS.get(id);
        if (material == null) {
            material = new WireMaterial(id, display, color);
            MATERIALS.put(id, material);
        }
        return material;
    }
}
