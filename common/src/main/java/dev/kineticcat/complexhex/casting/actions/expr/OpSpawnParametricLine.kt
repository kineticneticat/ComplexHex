package dev.kineticcat.complexhex.casting.actions.expr

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kineticcat.complexhex.api.casting.castables.SpellActionButPushesAnIota
import dev.kineticcat.complexhex.api.getExprOrNum
import dev.kineticcat.complexhex.api.util.Expr
import dev.kineticcat.complexhex.entity.ComplexHexEntities
import dev.kineticcat.complexhex.entity.ParameticLineEntity
import net.minecraft.world.phys.Vec3

object OpSpawnParametricLine : SpellActionButPushesAnIota {
    override val argc: Int = 4

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellActionButPushesAnIota.Result {
        val pos = args.getVec3(0)
        val xpr = args.getExprOrNum(1)
        val ypr = args.getExprOrNum(2)
        val zpr = args.getExprOrNum(3)

        env.assertVecInRange(pos)
        val line = ParameticLineEntity(ComplexHexEntities.PARAMETRIC_LINE, env.world)

        return SpellActionButPushesAnIota.Result(
            Spell(line, pos, xpr, ypr, zpr),
            MediaConstants.QUENCHED_SHARD_UNIT,
            listOf(ParticleSpray.burst(pos, 1.0)),
            line.asActionResult
        )
    }

    data class Spell(val line: ParameticLineEntity, val pos: Vec3, val xpr: Expr, val ypr: Expr, val zpr: Expr) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            line.pigment = env.pigment
            line.xpr = xpr
            line.ypr = ypr
            line.zpr = zpr
            line.setPos(pos)
            env.world.addFreshEntity(line)
        }

    }
}