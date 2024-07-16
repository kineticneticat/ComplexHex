package dev.kineticcat.complexhex.item;

import at.petrak.hexcasting.common.lib.HexItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static dev.kineticcat.complexhex.Complexhex.id;

public class ComplexHexCreativeTabs {
    public static void registerCreativeTabs(BiConsumer<CreativeModeTab, ResourceLocation> r) {
        for (var e : TABS.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }
    private static final Map<ResourceLocation, CreativeModeTab> TABS = new LinkedHashMap<>();


    public static final CreativeModeTab COMPLEXHEX = tab("complexhex", CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 7)
            .icon(() -> new ItemStack(HexItems.ABACUS)));


    private static CreativeModeTab tab(String name, CreativeModeTab.Builder bob) {
        var tab = bob.title(Component.translatable("itemGroup." + name)).build();
        var old = TABS.put(id(name), tab);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return tab;
    }
}