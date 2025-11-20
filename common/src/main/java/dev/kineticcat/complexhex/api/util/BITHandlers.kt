package dev.kineticcat.complexhex.api.util

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapInternalException
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import dev.kineticcat.complexhex.Complexhex
import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.decoration.ItemFrame
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import ram.talia.moreiotas.api.casting.iota.ItemStackIota
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota
import ram.talia.moreiotas.api.casting.iota.StringIota

object BITHandlers {

    val BLOCK_MISHAP_STRING = mutableListOf<String>()
    val ITEM_MISHAP_STRING = mutableListOf<String>()
    val TEXT_MISHAP_STRING = mutableListOf<String>()

    fun blockMishap(iota : Iota, idx : Int): MishapInvalidIota {
        var constructedString : String = ""
        for ((idx, str) in BLOCK_MISHAP_STRING.withIndex()) {
            if (idx == 0) {
                constructedString += str
                continue
            }
            if (idx == BLOCK_MISHAP_STRING.size-1) {
                constructedString += " or $str"
                continue
            }
            constructedString = "$constructedString, $str"
        }
        return MishapInvalidIota.of(iota, idx, "block_displayable", constructedString)
    }
    fun itemMishap(iota : Iota, idx : Int): MishapInvalidIota {
        var constructedString : String = ""
        for ((idx, str) in ITEM_MISHAP_STRING.withIndex()) {
            if (idx == 0) {
                constructedString += str
                continue
            }
            if (idx == ITEM_MISHAP_STRING.size-1) {
                constructedString += " or $str"
                continue
            }
            constructedString = "$constructedString, $str"
        }
        return MishapInvalidIota.of(iota, idx, "item_displayable", constructedString)
    }
    fun textMishap(iota : Iota, idx : Int): MishapInvalidIota {
        var constructedString : String = ""
        for ((idx, str) in TEXT_MISHAP_STRING.withIndex()) {
            if (idx == 0) {
                constructedString += str
                continue
            }
            if (idx == TEXT_MISHAP_STRING.size-1) {
                constructedString += " or $str"
                continue
            }
            constructedString = "$constructedString, $str"
        }
        return MishapInvalidIota.of(iota, idx, "text_displayable", constructedString)
    }

    fun init() {
        val blockTypes = listOf("vector", "block item stack", "block item type", "item frame", "string", "block item entity")
        val itemTypes = listOf("vector", "block item stack", "block item type", "item frame", "string", "item entity")
        val textTypes =  listOf("text", "list of applicable iotas")

        for (type in blockTypes) {
            BLOCK_MISHAP_STRING.add(type)
        }
        for (type in itemTypes) {
            ITEM_MISHAP_STRING.add(type)
        }
        for (type in textTypes) {
            TEXT_MISHAP_STRING.add(type)
        }
    }


    val BLOCK_VEC3 = addBlockDisplayHandler(Vec3Iota::class.java, fun(iota, env, idx) : BlockState {
        if (env == null) {
            throw MishapInternalException(Exception("Env is null when it shouldn't be."))
        }
        val vec = (iota as Vec3Iota).vec3
        env.assertVecInRange(vec)
        val blockPos = BlockPos.containing(vec)
        val blockState = env.world.getBlockState(blockPos)
        if (blockState.isAir) {
            throw MishapBadBlock(blockPos, Component.translatable("non_air"))
        }
        return blockState
    })
    val BLOCK_MOREIOTAS_ITEMSTACK = addBlockDisplayHandler(ItemStackIota::class.java, fun(iota, _, idx) : BlockState {
        val item = (iota as ItemStackIota).itemStack.item
        if (item is BlockItem) {
            return item.block.defaultBlockState()
        } else {
            throw blockMishap(iota, idx)
        }
    })
    val BLOCK_MOREIOTAS_ITEMTYPE = addBlockDisplayHandler(ItemTypeIota::class.java, fun (iota, _, idx) : BlockState {
        val block = (iota as ItemTypeIota).block
        if (block == null) {
            throw blockMishap(iota, idx)
        }
        return block.defaultBlockState()
    })
    val BLOCK_STRING = addBlockDisplayHandler(StringIota::class.java,  fun(iota, _, idx) : BlockState {
        val string = (iota as StringIota).string
        val block = BuiltInRegistries.BLOCK.getOptional(ResourceLocation(string)).orElse(null)
            ?: throw blockMishap(iota, idx)
        return block.defaultBlockState()
    })
    val BLOCK_ENTITY = addBlockDisplayHandler(EntityIota::class.java, fun(iota, _, idx) : BlockState {
        return when (val entity = (iota as EntityIota).entity) {
            is ItemEntity -> {
                val item = entity.item.item
                if (item is BlockItem) {
                    return item.block.defaultBlockState()
                } else {
                    throw blockMishap(iota, idx)
                }
            }
            is ItemFrame -> {
                val item = entity.item.item
                if (item is BlockItem) {
                    return item.block.defaultBlockState()
                } else {
                    throw blockMishap(iota, idx)
                }
            }
            else -> throw blockMishap(iota, idx)
        }
    })

    val ITEM_VEC3 = addItemDisplayHandler(Vec3Iota::class.java, fun (iota, env, idx) : ItemStack {
        if (env == null) {
            throw MishapInternalException(Exception("Env is null when it shouldn't be."))
        }
        val vec = (iota as Vec3Iota).vec3
        env.assertVecInRange(vec)
        val blockPos = BlockPos.containing(vec)
        val blockState = env.world.getBlockState(blockPos)
        if (blockState.isAir) {
            throw MishapBadBlock(blockPos, Component.translatable("non_air"))
        }
        return ItemStack(blockState.block.asItem())
    })
    val ITEM_MOREIOTAS_ITEMSTACK = addItemDisplayHandler(ItemStackIota::class.java, fun(iota, _, idx) : ItemStack {
        val itemStack = (iota as ItemStackIota).itemStack
        if (itemStack.isEmpty) {
            throw MishapInvalidIota.ofType(iota, 0, "non_empty_item_stack")
        }
        return itemStack
    })
    val ITEM_MOREIOTAS_ITEMTYPE = addItemDisplayHandler(ItemTypeIota::class.java, fun(iota, _, idx) : ItemStack {
        val item = (iota as ItemTypeIota).item
        if (item == null) {
            throw itemMishap(iota, idx)
        }
        return ItemStack(item)
    })
    val ITEM_STRING = addItemDisplayHandler(StringIota::class.java, fun(iota, _, idx) : ItemStack {
        val string = (iota as StringIota).string
        val item = BuiltInRegistries.ITEM.getOptional(ResourceLocation(string)).orElse(null) ?: throw itemMishap(iota, idx)
        return ItemStack(item)
    })
    val ITEM_ENTITY = addItemDisplayHandler(EntityIota::class.java, fun(iota, env, idx) : ItemStack {
        return when (val entity = (iota as EntityIota).entity) {
            is ItemEntity -> {
                entity.item
            }
            is ItemFrame -> {
                entity.item
            }
            else -> throw itemMishap(iota, idx)
        }
    })

    val TEXT_STRING = addTextDisplayHandler(StringIota::class.java, fun (iota, _, idx) : Component {
        if ((iota as StringIota).string.isEmpty()) {
            throw MishapInvalidIota.ofType(iota, 0, "non_empty_string")
        }
        return Component.literal(iota.string)
    })
    val TEXT_PATTERN = addTextDisplayHandler(StringIota::class.java, fun (iota, _, idx) : Component {
        val pattern = (iota as PatternIota)
        return pattern.display()
    })
    val TEXT_LIST = addTextDisplayHandler(ListIota::class.java, fun(iota, _, idx) : Component {
        val list = (iota as ListIota)
        if (list.list.all { iota -> iota is PatternIota }) {
            return list.display()
        }
        // TODO: more applicable iotas for lists
        throw textMishap(iota, idx)
    })

    fun <U : Class<*>, T : (Iota, CastingEnvironment?, Int) -> BlockState> addBlockDisplayHandler(name: U, function: T) {
        BITHandlerRegistry.registerBlockDisplayHandler(function, name)
    }
    fun <U : Class<*>, T : (Iota, CastingEnvironment?, Int) -> ItemStack> addItemDisplayHandler(name: U, function: T) {
        BITHandlerRegistry.registerItemDisplayHandler(function, name)
    }
    fun <U : Class<*>, T : (Iota, CastingEnvironment?, Int) -> Component> addTextDisplayHandler(name: U, function: T) {
        BITHandlerRegistry.registerTextDisplayHandler(function, name)
    }
    fun addBlockMishapString(string : String) {
        BLOCK_MISHAP_STRING.add(string)
    }
    fun addItemMishapString(string : String) {
        ITEM_MISHAP_STRING.add(string)
    }
    fun addTextMishapString(string : String) {
        TEXT_MISHAP_STRING.add(string)
    }
}