package dev.kineticcat.complexhex.casting.actions.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kineticcat.complexhex.casting.mishap.MishapBadString
import dev.kineticcat.complexhex.mixin.BITInvokers.BlockDisplayInvoker
import dev.kineticcat.complexhex.mixin.BITInvokers.ItemDisplayInvoker
import dev.kineticcat.complexhex.mixin.BITInvokers.TextDisplayInvoker
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.Display.*
import net.minecraft.world.item.ItemStack
import ram.talia.moreiotas.api.getString

object OpUpdateBIT : SpellAction {
    override val argc = 2
    private var cost = MediaConstants.DUST_UNIT
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val e = args.getEntity(0, argc)
        val id = args.getString(1, argc)

        env.assertEntityInRange(e)

        if (!when (e) {
                is Display.BlockDisplay -> BuiltInRegistries.BLOCK.containsKey(ResourceLocation(id))
                is Display.ItemDisplay -> BuiltInRegistries.ITEM.containsKey(ResourceLocation(id))
                is Display.TextDisplay -> true
                else -> throw MishapBadEntity.of(e, "bit")
            }) throw MishapBadString.of(id, "generalid")

        return SpellAction.Result(
            Spell(e as Display, id),
            cost,
            listOf(ParticleSpray.burst(env.castingEntity?.position() ?: env.mishapSprayPos(), 1.0))
        )
    }

    private data class Spell(val BIT: Display, val id: String) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
//            val oldRotation = BIT.entityData.get((BIT as DisplayInvoker).GetLeftRotationDataID())
//            val oldScale = BIT.entityData.get((BIT as DisplayInvoker).GetScaleDataID())
//
//            (BIT as DisplayInvoker).invokeSetTransformation(Transformation(pos.toVector3f(), oldRotation, oldScale, null))
            when (BIT) {
                is BlockDisplay -> (BIT as BlockDisplayInvoker).invokeSetBlockState(BuiltInRegistries.BLOCK.get(ResourceLocation(id)).defaultBlockState())
                is ItemDisplay -> (BIT as ItemDisplayInvoker).invokeSetItemStack(ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation(id))))
                is TextDisplay -> (BIT as TextDisplayInvoker).invokeSetText(Component.literal(id))
            }
            BIT.tick() // for good measure i guess???
        }
    }
}