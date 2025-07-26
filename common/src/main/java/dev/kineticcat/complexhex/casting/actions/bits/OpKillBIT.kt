package dev.kineticcat.complexhex.casting.actions.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import dev.kineticcat.complexhex.entity.ParametricLineEntity
import dev.kineticcat.complexhex.entity.ParametricSurfaceEntity
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.Entity


object OpKillBIT : SpellAction {
    override val argc = 1
    private val cost = 0L
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val e = args.getEntity(0, argc)

        env.assertEntityInRange(e)

        if (!(e is Display || e is ParametricLineEntity || e is ParametricSurfaceEntity)) throw MishapBadEntity.of(e, "bit_kill")

        val pos = e.position()

        return SpellAction.Result(
            Spell(e),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val BIT: Entity) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            BIT.kill()
        }
    }
}