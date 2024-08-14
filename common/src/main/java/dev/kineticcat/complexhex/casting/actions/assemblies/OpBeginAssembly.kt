package dev.kineticcat.complexhex.casting.actions.assemblies

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.circle.MishapNoSpellCircle
import at.petrak.hexcasting.api.misc.MediaConstants
import com.mojang.math.Transformation
import dev.kineticcat.complexhex.entity.AssemblyManagerEntity
import dev.kineticcat.complexhex.entity.ComplexHexEntities
import dev.kineticcat.complexhex.mixin.BITInvokers.BlockDisplayInvoker
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f

object OpBeginAssembly : SpellAction {
    override val argc = 0
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        if (env !is CircleCastEnv) throw MishapNoSpellCircle()
        val circleState = env.circleState()
        return SpellAction.Result(
            Spell(circleState.currentPos.center),
            MediaConstants.CRYSTAL_UNIT,
            listOf()
        )
    }
    private data class Spell(val pos: Vec3) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val manager = AssemblyManagerEntity(ComplexHexEntities.ASSEMBLY_MANAGER, env.world)
            manager.addNode(pos)
            manager.
            env.world.addFreshEntity(manager)
        }
    }
}