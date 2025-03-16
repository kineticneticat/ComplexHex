package dev.kineticcat.complexhex.casting.assemblies

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.circle.MishapNoSpellCircle
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.pigment.FrozenPigment
import dev.kineticcat.complexhex.api.casting.castables.VarargSpellAction
import dev.kineticcat.complexhex.entity.AssemblyManagerEntity
import dev.kineticcat.complexhex.entity.ComplexHexEntities
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.Vec3

object OpBeginAssembly : VarargSpellAction {

    const val managerUUIDTag = "AssemblyManagerUUID"

    override fun argc(stack: List<Iota>): Int {
        if (stack.size == 2) {
            if (stack[0] is ListIota && stack[1] is ListIota) {
                var l1 = stack[0] as ListIota
                var l2 = stack[1] as ListIota
                l1.list.forEach { if (it !is Vec3Iota) return 0 }
                l1.list.forEach { if (it !is EntityIota) return 0 }
                return 2
            }
        }

        return 0
    }

    override fun execute(args: List<Iota>, argc: Int, env: CastingEnvironment): SpellAction.Result {
        throw IllegalStateException()
    }

    override fun executeWithUserdata(
        args: List<Iota>,
        argc: Int,
        env: CastingEnvironment,
        userData: CompoundTag
    ): SpellAction.Result {
        if (env !is CircleCastEnv) throw MishapNoSpellCircle()
        val circleState = env.circleState()
        val pos = circleState.currentPos.center

        val manager = AssemblyManagerEntity(ComplexHexEntities.ASSEMBLY_MANAGER, env.world)

        userData.putUUID(managerUUIDTag, manager.uuid)

        return SpellAction.Result(
            Spell(manager, pos, env.pigment),
            MediaConstants.CRYSTAL_UNIT,
            listOf()
        )
    }
    private data class Spell(val manager: AssemblyManagerEntity, val pos: Vec3, val pigment: FrozenPigment) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            manager.setPos(pos)
            manager.pigment = pigment
            env.world.addFreshEntity(manager)
        }
    }
}