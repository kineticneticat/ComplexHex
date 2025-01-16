package dev.kineticcat.complexhex.casting.actions.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import com.mojang.math.Transformation
import dev.kineticcat.complexhex.mixin.BITInvokers.DisplayInvoker
import net.minecraft.world.entity.Display
import net.minecraft.world.phys.Vec3


object OpTranslateBIT : SpellAction {
    override val argc = 2
    private var cost = 0L
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val e = args.getEntity(0, argc)
        val pos = args.getVec3(1, argc)

        if (e !is Display) throw MishapBadEntity.of(e, "bit")

        env.assertEntityInRange(e)
        env.assertVecInRange(pos)

        return SpellAction.Result(
            Spell(e, pos),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val BIT: Display, val pos: Vec3) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val oldRotation = BIT.entityData.get((BIT as DisplayInvoker).GetLeftRotationDataID())
            val oldScale = BIT.entityData.get((BIT as DisplayInvoker).GetScaleDataID())

            (BIT as DisplayInvoker).invokeSetTransformation(Transformation(pos.toVector3f(), oldRotation, oldScale, null))
            BIT.tick() // for good measure i guess????
        }
    }
}