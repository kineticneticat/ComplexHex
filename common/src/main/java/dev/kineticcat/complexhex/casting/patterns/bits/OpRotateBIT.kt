package dev.kineticcat.complexhex.casting.patterns.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.misc.MediaConstants
import com.mojang.math.Transformation
import dev.kineticcat.complexhex.api.getQuaternion
import dev.kineticcat.complexhex.mixin.BITInvokers.DisplayInvoker
import dev.kineticcat.complexhex.util.Quaternion
import net.minecraft.world.entity.Display
import org.joml.Quaternionf
import org.joml.Vector3f


object OpRotateBIT : SpellAction {
    override val argc = 2
    private val cost = 2 * MediaConstants.DUST_UNIT
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val e = args.getEntity(0, argc)
        val quaternion = args.getQuaternion(1, argc)


        env.assertEntityInRange(e)
        if (e !is Display) throw MishapBadEntity.of(e, "bit")

        val pos = (e as Display).position()

        return SpellAction.Result(
            Spell(e, quaternion),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val BIT: Display, val quat: Quaternion) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {

            val oldScale = BIT.entityData.get((BIT as DisplayInvoker).GetScaleDataID())

            (BIT as DisplayInvoker).invokeSetTransformation(Transformation(Vector3f(-0.5f, -0.5f, -0.5f), Quaternionf(quat), oldScale, null))
            BIT.tick() //??????
        }
    }
}