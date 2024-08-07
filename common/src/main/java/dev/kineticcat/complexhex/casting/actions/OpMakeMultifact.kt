package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadItem
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.extractMedia
import at.petrak.hexcasting.api.utils.isMediaItem
import at.petrak.hexcasting.xplat.IXplatAbstractions
import dev.kineticcat.complexhex.Complexhex
import dev.kineticcat.complexhex.api.casting.castables.VarargSpellAction
import dev.kineticcat.complexhex.item.magic.ItemMultifact
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import kotlin.math.pow

object OpMakeMultifact : VarargSpellAction {
    override fun argc(stack: List<Iota>): Int {
        if (stack.size == 1) return 1
        return if (stack[1] is EntityIota) 2 else 1
    }

    private fun isMultifact(stack: ItemStack): Boolean {
        val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(stack)
        return stack.item is ItemMultifact && hexHolder != null
    }
    override fun execute(args: List<Iota>, argc: Int, env: CastingEnvironment): SpellAction.Result {

        val (stack, hand) = env.getHeldItemToOperateOn(this::isMultifact)
            ?: throw MishapBadOffhandItem.of(ItemStack.EMPTY.copy(), null, "multifact")
        val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(stack)
        if (stack.item !is ItemMultifact) {
            throw MishapBadOffhandItem.of(stack, hand, "multifact")
        } else if ( hexHolder == null ) {
            throw  MishapBadOffhandItem.of(stack, hand, "iota.write")
        }


        if (argc == 2 && (stack.item as ItemMultifact).hasHex(stack)) {
            throw MishapBadOffhandItem.of(stack, hand, "multifact.already_init")
        }
        if (argc == 1 && !(stack.item as ItemMultifact).hasHex(stack)) {
            throw MishapBadOffhandItem.of(stack, hand, "multifact.not_init")
        }

        val hex:List<Iota>
        val entityIota: EntityIota?

        if (argc == 1) {
            hex = args.getList(0).toList()
            entityIota = null
        } else {
            hex = args.getList(1).toList()
            entityIota = args[0] as EntityIota
        }

        var entity: ItemEntity? = null

        if (entityIota is EntityIota && entityIota.entity is ItemEntity && isMediaItem((entityIota.entity as ItemEntity).item) && !hexHolder.hasHex()) {
            entity = entityIota.entity as ItemEntity
            env.assertEntityInRange(entity)
            if (!isMediaItem(entity.item) || extractMedia(
                    entity.item,
                    drainForBatteries = true,
                    simulate = true
                ) <= 0
            ) throw MishapBadItem.of(entity, "media_for_battery")
        }

        val trueName = MishapOthersName.getTrueNameFromArgs(hex, env.castingEntity as? ServerPlayer);
        if (trueName != null) throw MishapOthersName(trueName)

        // base cost is 10 crystals, but doubles for each subsequent hex
        val cost = (2.0.pow((stack.item as ItemMultifact).getAmount(stack).toDouble())) * (MediaConstants.CRYSTAL_UNIT*10)

        // if entity is used, spray there, else at caster and if all else fails, just do it where no one will see lmao
        val sprayPos = entity?.position() ?: env.castingEntity?.position() ?: Vec3.ZERO

        return SpellAction.Result(
            Spell(entity, hex, stack),
            cost.toLong(),
            listOf(ParticleSpray.burst(sprayPos, 0.5))
        )

    }

    private data class Spell(val itemEntity: ItemEntity?, val hex: List<Iota>, val stack: ItemStack) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val hexHolder = IXplatAbstractions.INSTANCE.findHexHolder(stack) ?: return
            if (itemEntity != null) {
                // due to previous stuff, this hexHolder.hasHex() must be false here
                val entityStack = itemEntity.item.copy()
                val mediaAmount = extractMedia(entityStack, drainForBatteries = true)
                if (mediaAmount > 0) {
                    hexHolder.writeHex(hex, env.pigment, mediaAmount)
                }
                itemEntity.item = entityStack
                if (entityStack.isEmpty) itemEntity.kill()
            } else {
                // if media is 0, then hex should be appended
                hexHolder.writeHex(hex, env.pigment, 0)
            }


        }
    }
}