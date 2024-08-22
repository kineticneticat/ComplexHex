package dev.kineticcat.complexhex.casting.actions.assemblies

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.circle.MishapNoSpellCircle
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.utils.hasCompound
import dev.kineticcat.complexhex.Complexhex
import dev.kineticcat.complexhex.casting.actions.assemblies.OpBeginAssembly.managerUUIDTag
import dev.kineticcat.complexhex.casting.mishap.MishapBadAssembly
import dev.kineticcat.complexhex.casting.mishap.MishapPredecessorMissing
import dev.kineticcat.complexhex.entity.AssemblyManagerEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.Vec3

object OpEndAssembly : SpellAction {
    override val argc = 0
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        throw IllegalStateException()
    }

    override fun executeWithUserdata(args: List<Iota>, env: CastingEnvironment, userData: CompoundTag): SpellAction.Result {
        if (env !is CircleCastEnv) throw MishapNoSpellCircle()
        if (!userData.hasUUID(managerUUIDTag)) throw MishapPredecessorMissing.of("assembly/begin")
        val manager = env.world.getEntity(userData.getUUID(managerUUIDTag)) as AssemblyManagerEntity
        userData.remove(managerUUIDTag) // remove tag so the impetus mixin doesn't kill the manager

        val controller = Assemblies.findController(manager.vertices)
            ?: throw MishapBadAssembly.of("not_assembly")

        val circleState = env.circleState()
        return SpellAction.Result(
            Spell(circleState.currentPos.center, manager, controller),
            MediaConstants.CRYSTAL_UNIT,
            listOf()
        )
    }
    private data class Spell(val pos: Vec3, val manager: AssemblyManagerEntity, val controller: String) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            manager.triggerAssembly(controller)
            manager.tick() // ???
        }
    }
}