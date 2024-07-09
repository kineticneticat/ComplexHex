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
import dev.kineticcat.complexhex.stuff.Quaternion
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import org.joml.Quaterniond
import org.joml.Quaternionf
import ram.talia.moreiotas.api.getEntityType
import ram.talia.moreiotas.api.getString


object OpScaleBIT : SpellAction {
    override val argc = 2
    private val cost = 2 * MediaConstants.DUST_UNIT
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val e = args.getEntity(0, argc)
        val vec = args.getVec3(1, argc)


        env.assertEntityInRange(e)
        if (e !is Display) throw MishapBadEntity(e, Component.translatable("bits.rotate.badentity"))

        val pos = (e as Display).position()

        return SpellAction.Result(
            Spell(e, vec),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val BIT: Display, val vec: Vec3) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {

            val oldRotation = BIT.entityData.get((BIT as DisplayInvoker).GetLeftRoatationDataID())

            (BIT as DisplayInvoker).invokeSetTransformation(Transformation(null, oldRotation, vec.toVector3f(), null))
            BIT.tick() //??????
        }
    }
}