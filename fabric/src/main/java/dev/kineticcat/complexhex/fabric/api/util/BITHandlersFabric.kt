package dev.kineticcat.complexhex.fabric.api.util

import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import dev.architectury.platform.Platform
import dev.kineticcat.complexhex.api.util.BITHandlerRegistry
import dev.kineticcat.complexhex.api.util.BITHandlers
import miyucomics.hexpose.iotas.IdentifierIota
import miyucomics.hexpose.iotas.ItemStackIota
import miyucomics.hexpose.iotas.TextIota
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import ram.talia.hexal.api.casting.iota.MoteIota
import ram.talia.moreiotas.api.casting.iota.StringIota

object BITHandlersFabric {

    fun init() {
        if (Platform.isModLoaded("hexpose")) {
            for (entry in listOf("identifier", "text")) {
                BITHandlers.addBlockMishapString(entry)
                BITHandlers.addItemMishapString(entry)
            }
            BITHandlers.addTextMishapString("text")
            val BLOCK_HEXPOSE_ITEMSTACK = BITHandlers.addBlockDisplayHandler(ItemStackIota::class.java, fun(iota, _, idx) : BlockState {
                val item = (iota as ram.talia.moreiotas.api.casting.iota.ItemStackIota).itemStack.item
                if (item is BlockItem) {
                    return item.block.defaultBlockState()
                } else {
                    throw BITHandlers.blockMishap(iota, idx)
                }
            })
            val BLOCK_IDENTIFIER = BITHandlers.addBlockDisplayHandler(IdentifierIota::class.java, fun(iota, _, idx) : BlockState {
                val identifier = (iota as IdentifierIota).identifier
                val block = BuiltInRegistries.BLOCK.getOptional(identifier).orElse(null)
                    ?: throw BITHandlers.blockMishap(iota, idx)
                return block.defaultBlockState()
            })
            val BLOCK_DISPLAY = BITHandlers.addBlockDisplayHandler(TextIota::class.java, fun(iota, _, idx) : BlockState {
                val display = (iota as TextIota).display()
                val block = BuiltInRegistries.BLOCK.getOptional(ResourceLocation(display.plainCopy().string)).orElse(null)
                    ?: throw BITHandlers.blockMishap(iota, idx)
                return block.defaultBlockState()
            })

            val ITEM_HEXPOSE_ITEMSTACK = BITHandlers.addItemDisplayHandler(ItemStackIota::class.java, fun(iota, _, idx) : ItemStack {
                val item = (iota as ram.talia.moreiotas.api.casting.iota.ItemStackIota).itemStack
                if (item.isEmpty) {
                    throw MishapInvalidIota.ofType(iota, 0, "non_empty_item_stack")
                }
                return item
            })
            val ITEM_IDENTIFIER = BITHandlers.addItemDisplayHandler(IdentifierIota::class.java, fun(iota, _, idx) : ItemStack {
                val identifier = (iota as IdentifierIota).identifier
                val item = BuiltInRegistries.ITEM.getOptional(identifier).orElse(null) ?: throw BITHandlers.itemMishap(iota, idx)
                return ItemStack(item)
            })
            val ITEM_DISPLAY = BITHandlers.addItemDisplayHandler(ItemStackIota::class.java, fun(iota, _, idx) : ItemStack {
                val display = (iota as TextIota).display()
                val item = BuiltInRegistries.ITEM.getOptional(ResourceLocation(display.plainCopy().string)).orElse(null)
                    ?: throw  BITHandlers.itemMishap(iota, idx)
                return ItemStack(item)
            })

            val TEXT_DISPLAY = BITHandlers.addTextDisplayHandler(TextIota::class.java, fun(iota, _, idx) : Component {
                val display = (iota as TextIota).display()
                if (display.string.isEmpty()) {
                    throw BITHandlers.textMishap(iota, idx)
                }
                return display
            })
        }
        if (Platform.isModLoaded("hexal")) {
            for (entry in listOf("mote")) {
                BITHandlers.addBlockMishapString(entry)
                BITHandlers.addItemMishapString(entry)
            }
            val BLOCK_MOTE = BITHandlers.addBlockDisplayHandler(MoteIota::class.java, fun(iota, _, idx) : BlockState {
                val mote = (iota as MoteIota)
                if (mote.isEmpty) {
                    throw MishapInvalidIota.ofType(iota, 0, "non_empty_mote")
                }
                val item = mote.item
                if (item is BlockItem) {
                    return item.block.defaultBlockState()
                } else {
                    throw BITHandlers.blockMishap(iota, idx)
                }
            })

            val ITEM_MOTE = BITHandlers.addItemDisplayHandler(MoteIota::class.java, fun(iota, _, idx) : ItemStack {
                val mote = (iota as MoteIota)
                if (mote.isEmpty) {
                    throw MishapInvalidIota.ofType(iota, 0, "non_empty_mote")
                }
                val item = mote.item
                return ItemStack(item)
            })
        }
    }
}