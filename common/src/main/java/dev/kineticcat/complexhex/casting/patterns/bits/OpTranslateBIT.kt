package dev.kineticcat.complexhex.casting.patterns.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.misc.MediaConstants
import com.mojang.math.Transformation
import dev.kineticcat.complexhex.Complexhex
import dev.kineticcat.complexhex.api.getQuaternion
import dev.kineticcat.complexhex.casting.mishap.MishapBadString
import dev.kineticcat.complexhex.mixin.BITInvokers.BlockDisplayInvoker
import dev.kineticcat.complexhex.mixin.BITInvokers.DisplayInvoker
import dev.kineticcat.complexhex.mixin.BITInvokers.ItemDisplayInvoker
import dev.kineticcat.complexhex.mixin.BITInvokers.TextDisplayInvoker
import dev.kineticcat.complexhex.stuff.Quaternion
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.Display.BlockDisplay
import net.minecraft.world.entity.Display.TextDisplay
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import org.joml.Vector3d
import org.joml.Vector3f
import ram.talia.moreiotas.api.getEntityType
import ram.talia.moreiotas.api.getString


object OpTranslateBIT : SpellAction {
    override val argc = 2
    private val cost: long
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val e = args.getEntity(0, argc)
        val delta = args.getVec3(1, argc)

        if (e !is Display) throw MishapBadEntity(e, Component.translatable("bits.badentity"))

        env.assertEntityInRange(e)
        val pos = (e as Display).position()
        env.assertVecInRange(pos.add(delta))

        // cost = delta.length ??? check when home

        return SpellAction.Result(
            Spell(e, delta),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val BIT: Display, val delta: Vec3) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val currentPos = BIT.position()
            val newPos = currentPos.add(delta)
            BIT.setPos(newPos)
            BIT.tick() // for good measure i guess????
        }
    }
}