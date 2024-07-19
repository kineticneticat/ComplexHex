package dev.kineticcat.complexhex.fabric;

import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.block.ComplexHexBlocks
import dev.kineticcat.complexhex.block.entity.ComplexHexBlockEntities
import dev.kineticcat.complexhex.item.ComplexHexCreativeTabs
import dev.kineticcat.complexhex.item.ComplexHexItems
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import java.util.function.BiConsumer

/**
 * This is your loading entrypoint on fabric(-likes), in case you need to initialize
 * something platform-specific.
 * <br/>
 * Since quilt can load fabric mods, you develop for two platforms in one fell swoop.
 * Feel free to check out the <a href="https://github.com/architectury/architectury-templates">Architectury templates</a>
 * if you want to see how to add quilt-specific code.
 */
object ComplexhexFabric : ModInitializer {

    override fun onInitialize() {
        Complexhex.init()

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register {tab, entries ->
            ComplexHexBlocks.registerBlockCreativeTab(entries::accept, tab)
            ComplexHexItems.registerItemCreativeTab(entries::accept, tab)
        }
        ComplexHexBlocks.registerBlocks(bind(BuiltInRegistries.BLOCK))
        ComplexHexBlocks.registerBlockItems(bind(BuiltInRegistries.ITEM))
        ComplexHexItems.registerItems(bind(BuiltInRegistries.ITEM))
        ComplexHexCreativeTabs.registerCreativeTabs(bind(BuiltInRegistries.CREATIVE_MODE_TAB))
        ComplexHexBlockEntities.registerTiles(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE))
    }
    private fun <T> bind(registry: Registry<in T>): BiConsumer<T, ResourceLocation> =
        BiConsumer<T, ResourceLocation> { t, id -> Registry.register(registry, id, t) }
}
