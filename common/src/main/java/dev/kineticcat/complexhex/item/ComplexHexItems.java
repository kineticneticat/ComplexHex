package dev.kineticcat.complexhex.item;

import dev.kineticcat.complexhex.block.ComplexHexBlocks;
import dev.kineticcat.complexhex.item.magic.ItemBygoneCypher;
import dev.kineticcat.complexhex.item.magic.ItemMultifact;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static dev.kineticcat.complexhex.Complexhex.id;

public class ComplexHexItems {
    public static void registerItems(BiConsumer<Item, ResourceLocation> r) {
        for (var e : ITEMS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }
    public static void registerItemCreativeTab(CreativeModeTab.Output r, CreativeModeTab tab) {
        for (TabEntry item : ITEM_TABS.getOrDefault(tab, List.of())) {
            item.register(r);
        }
    }
    private static final Map<ResourceLocation, Item> ITEMS = new LinkedHashMap<>();
    private static final Map<CreativeModeTab, List<TabEntry>> ITEM_TABS = new LinkedHashMap<>();

    public static final Item BURNT_SHARD = make("burnt_shard", new Item(new Item.Properties()));
    public static final ItemAwakenedBurntShard AWAKENED_BURNT_SHARD = make("awakened_burnt_shard", new ItemAwakenedBurntShard(ComplexHexBlocks.AWAKENED_BURNT,new Item.Properties().rarity(Rarity.RARE)));
    public static final ItemMultifact MULTIFACT = make("multifact", new ItemMultifact(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final ItemBygoneCypher BYGONE_CYPHER = make("bygone_cypher", new ItemBygoneCypher(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final PigmentDebug PIGMENT_DEBUG = make("pigment_debug", new PigmentDebug(new Item.Properties()));

    public static <T extends Item> T make(String name, T item, CreativeModeTab tab) {
        Item old = ITEMS.put(id(name), item);
        if (old != null) {
            throw new IllegalArgumentException("complexhex's dev's a dumbass: " + name);
        }
        ITEM_TABS.computeIfAbsent(tab, t -> new ArrayList<>()).add(new TabEntry.ItemEntry(item));
        return item;
    }
    public static <T extends Item> T make(String name, T item) {
        return make(name, item, ComplexHexCreativeTabs.COMPLEXHEX);
    }

    private static abstract class TabEntry {
        abstract void register(CreativeModeTab.Output r);

        static class ItemEntry extends TabEntry {
            private final Item item;

            ItemEntry(Item item) {
                this.item = item;
            }

            @Override
            void register(CreativeModeTab.Output r) {
                r.accept(item);
            }
        }

        static class StackEntry extends TabEntry {
            private final Supplier<ItemStack> stack;

            StackEntry(Supplier<ItemStack> stack) {
                this.stack = stack;
            }

            @Override
            void register(CreativeModeTab.Output r) {
                r.accept(stack.get());
            }
        }
    }
}
