package net.deepacat.createpowerlines.index;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.deepacat.createpowerlines.CreatePowerlines;
import com.tterrag.registrate.util.entry.RegistryEntry;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import net.deepacat.createpowerlines.blocks.connector.WireMaterials;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CACreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreatePowerlines.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + CreatePowerlines.MODID + ".main"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(WireMaterials.MATERIALS.values().iterator().next().spool::asStack)
                    .displayItems(new RegistrateDisplayItemsGenerator())
                    .build());

    public static void register(IEventBus modEventBus) {
        TAB_REGISTER.register(modEventBus);
        CreatePowerlines.REGISTRATE.setCreativeTab(MAIN_TAB);
    }

    public static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

        private List<Item> collectBlocks() {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Block> entry : CreatePowerlines.REGISTRATE.getAll(Registries.BLOCK)) {
                if (!CreateRegistrate.isInCreativeTab(entry, CACreativeModeTabs.MAIN_TAB))
                    continue;
                Item item = entry.get().asItem();
                if (item == Items.AIR)
                    continue;
                items.add(item);
            }
            items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
            return items;
        }

        private List<Item> collectItems() {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Item> entry : CreatePowerlines.REGISTRATE.getAll(Registries.ITEM)) {
                if (!CreateRegistrate.isInCreativeTab(entry, CACreativeModeTabs.MAIN_TAB))
                    continue;
                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;
                items.add(item);
            }
            return items;
        }

        @Override
        public void accept(CreativeModeTab.@NotNull ItemDisplayParameters params, CreativeModeTab.@NotNull Output output) {
            List<Item> items = new LinkedList<>();
            items.addAll(collectBlocks());
            items.addAll(collectItems());
            for (Item item : items) {
                output.accept(item);
            }
        }
    }
}
