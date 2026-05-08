package dev.kineticcat.complexhex.api.util

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapInternalException
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.packs.resources.Resource
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import oshi.util.tuples.Triplet
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.api.casting.iota.StringIota
import vazkii.patchouli.client.book.template.component.ComponentText
import kotlin.jvm.javaClass

object BITHandlerRegistry {
    private val blockDisplayHandlerRegistry = mutableMapOf<Class<*>, (Iota, CastingEnvironment?, Int) -> BlockState>()
    private val itemDisplayHandlerRegistry = mutableMapOf<Class<*>, (Iota, CastingEnvironment?, Int) -> ItemStack>()
    private val textDisplayHandlerRegistry = mutableMapOf<Class<*>, (Iota, CastingEnvironment?, Int) -> Component>()

    fun registerBlockDisplayHandler(handler: (Iota, CastingEnvironment?, Int) -> BlockState, iotaClass: Class<*>) {
        blockDisplayHandlerRegistry[iotaClass] = handler
    }
    fun registerItemDisplayHandler(handler: (Iota, CastingEnvironment?, Int) -> ItemStack, iotaClass: Class<*>) {
        itemDisplayHandlerRegistry[iotaClass] = handler
    }

    fun registerTextDisplayHandler(handler: (Iota, CastingEnvironment?, Int) -> Component, iotaClass: Class<*>) {
        textDisplayHandlerRegistry[iotaClass] = handler
    }

    fun matchBlockDisplayIota(iota: Iota, level : CastingEnvironment?, idx: Int) : BlockState {
        if (!blockDisplayHandlerRegistry.containsKey(iota.javaClass)) {
            throw BITHandlers.blockMishap(iota, idx)
        }
        val handler = blockDisplayHandlerRegistry[iota.javaClass]!!
        if (level != null) {
            return handler(iota, level, idx)
        }
        return handler(iota, null, idx)
    }

    fun matchItemDisplayIota(iota: Iota, level : CastingEnvironment?, idx : Int) : ItemStack {
        if (!itemDisplayHandlerRegistry.containsKey(iota.javaClass)) {
            throw BITHandlers.itemMishap(iota, idx)
        }
        val handler = itemDisplayHandlerRegistry[iota.javaClass]!!
        if (level != null) {
            return handler(iota, level, idx)
        }
        return handler(iota, null, idx)
    }

    fun matchTextDisplayIota(iota: Iota, level : CastingEnvironment?, idx: Int) : Component {
        if (!textDisplayHandlerRegistry.containsKey(iota.javaClass)) {
            throw BITHandlers.textMishap(iota, idx)
        }
        val handler = textDisplayHandlerRegistry[iota.javaClass]!!
        if (level != null) {
            return handler(iota, level, idx)
        }
        return handler(iota, null, idx)
    }
}