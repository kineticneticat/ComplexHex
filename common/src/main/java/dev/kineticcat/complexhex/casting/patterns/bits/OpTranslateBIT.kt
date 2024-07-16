package dev.kineticcat.complexhex.casting.patterns.bits

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import net.minecraft.world.entity.Display
import net.minecraft.world.phys.Vec3


object OpTranslateBIT : SpellAction {
    override val argc = 2
    private var cost: Long = 0
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val e = args.getEntity(0, argc)
        val delta = args.getVec3(1, argc)

        if (e !is Display) throw MishapBadEntity.of(e, "bit")

        env.assertEntityInRange(e)
        val pos = (e as Display).position()
        env.assertVecInRange(pos.add(delta))
        cost = delta.length().toLong()

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