package dev.kineticcat.complexhex.casting.actions.expr

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kineticcat.complexhex.api.casting.castables.SpellActionButPushesAnIota
import dev.kineticcat.complexhex.api.getExprLike
import dev.kineticcat.complexhex.api.util.Expr
import dev.kineticcat.complexhex.entity.ComplexHexEntities
import dev.kineticcat.complexhex.entity.ParametricLineEntity
import net.minecraft.world.phys.Vec3

object OpSpawnParametricLine : SpellActionButPushesAnIota {
    override val argc: Int = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellActionButPushesAnIota.Result {
        val pos = args.getVec3(0)
        val expr = args.getExprLike(1)

        env.assertVecInRange(pos)
        val line = ParametricLineEntity(ComplexHexEntities.PARAMETRIC_LINE, env.world)

        return SpellActionButPushesAnIota.Result(
            Spell(line, pos, expr),
            MediaConstants.QUENCHED_SHARD_UNIT,
            listOf(ParticleSpray.burst(pos, 1.0)),
            line.asActionResult
        )
    }

    data class Spell(val line: ParametricLineEntity, val pos: Vec3, val expr: Expr) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            line.pigment = env.pigment
            line.expr = expr
            line.setPos(pos)
            env.world.addFreshEntity(line)
        }
    }
}