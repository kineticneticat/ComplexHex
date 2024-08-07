package dev.kineticcat.complexhex.casting.actions.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.world.entity.Display


object OpKillBIT : SpellAction {
    override val argc = 1
    private val cost = 1 * MediaConstants.DUST_UNIT
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val e = args.getEntity(0, argc)

        env.assertEntityInRange(e)

        if (e !is Display) throw MishapBadEntity.of(e, "bit")

        val pos = (e as Display).position()

        return SpellAction.Result(
            Spell(e),
            cost,
            listOf(ParticleSpray.burst(pos, 1.0))
        )
    }

    private data class Spell(val BIT: Display) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {

            BIT.kill()
        }
    }
}