package dev.kineticcat.complexhex.casting.actions.assemblies

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.circle.MishapNoSpellCircle
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kineticcat.complexhex.Complexhex
import dev.kineticcat.complexhex.api.util.utilStuffLmao
import dev.kineticcat.complexhex.entity.AssemblyManagerEntity
import dev.kineticcat.complexhex.entity.ComplexHexEntities
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.Vec3

object OpBeginAssembly : SpellAction {

    public const val managerUUIDTag = "AssemblyManagerUUID"

    override val argc = 0
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        throw IllegalStateException()
    }

    override fun executeWithUserdata(
        args: List<Iota>,
        env: CastingEnvironment,
        userData: CompoundTag
    ): SpellAction.Result {
        if (env !is CircleCastEnv) throw MishapNoSpellCircle()
        val circleState = env.circleState()
        val pos = circleState.currentPos.center

        val manager = AssemblyManagerEntity(ComplexHexEntities.ASSEMBLY_MANAGER, env.world)

        userData.putUUID(managerUUIDTag, manager.uuid)

        return SpellAction.Result(
            Spell(manager, pos),
            MediaConstants.CRYSTAL_UNIT,
            listOf()
        )
    }
    private data class Spell(val manager: AssemblyManagerEntity, val pos: Vec3) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            manager.setPos(pos)
            env.world.addFreshEntity(manager)
        }
    }
}