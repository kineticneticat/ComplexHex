package net.complexhex.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.complexhex.Complexhex;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static net.complexhex.Complexhex.id;

public class ComplexhexItemRegistry {
    // Register items through this
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Complexhex.MOD_ID, Registries.ITEM);
    public static void init() {
        ITEMS.register();
    }

    // A new creative tab. Notice how it is one of the few things that are not deferred
    public static final CreativeModeTab DUMMY_GROUP = CreativeTabRegistry.create(Component.translatable("dummy_group"), () -> new ItemStack(ComplexhexItemRegistry.DUMMY_ITEM.get()));

    // During the loading phase, refrain from accessing suppliers' items (e.g. EXAMPLE_ITEM.get()), they will not be available
    public static final RegistrySupplier<Item> DUMMY_ITEM = ITEMS.register("dummy_item", () -> new Item(new Item.Properties()));


}
