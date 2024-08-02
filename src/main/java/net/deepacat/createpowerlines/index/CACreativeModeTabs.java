package net.deepacat.createpowerlines.index;

import net.deepacat.createpowerlines.CreatePowerlines;
import com.tterrag.registrate.util.entry.RegistryEntry;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CACreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> TAB_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreatePowerlines.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = TAB_REGISTER.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.createpowerlines.main"))
                    .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(CAItems.COPPER_SPOOL::asStack)
                    .displayItems(new RegistrateDisplayItemsGenerator())
                    .build());

    public static void register(IEventBus modEventBus) {
        TAB_REGISTER.register(modEventBus);
    }

    public static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

        private List<Item> collectBlocks(RegistryObject<CreativeModeTab> tab, Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Block> entry : CreatePowerlines.REGISTRATE.getAll(Registries.BLOCK)) {
                if (!CreatePowerlines.REGISTRATE.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get()
                        .asItem();
                if (item == Items.AIR)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
            return items;
        }

        private List<Item> collectItems(RegistryObject<CreativeModeTab> tab, Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();


            for (RegistryEntry<Item> entry : CreatePowerlines.REGISTRATE.getAll(Registries.ITEM)) {
                if (!CreatePowerlines.REGISTRATE.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            return items;
        }

        private static void outputAll(CreativeModeTab.Output output, List<Item> items) {
            for (Item item : items) {
                output.accept(item);
            }
        }

        List<Item> exclude = List.of();

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output) {
            List<Item> items = new LinkedList<>();
            items.addAll(collectBlocks(MAIN_TAB, (item) -> {
                return false;
            }));
            items.addAll(collectItems(MAIN_TAB, (item) -> exclude.contains(item)));

            outputAll(output, items);
        }
    }
}
