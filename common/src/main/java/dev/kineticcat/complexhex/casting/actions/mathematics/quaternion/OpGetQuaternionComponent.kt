package dev.kineticcat.complexhex.casting.actions.mathematics.quaternion

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.getQuaternion

class OpGetQuaternionComponent(val component: Component) : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val Q = args.getQuaternion(0, argc)
        return when (component) {
            Component.W -> Q.w.asActionResult
            Component.X -> Q.x.asActionResult
            Component.Y -> Q.y.asActionResult
            Component.Z -> Q.z.asActionResult
        }
    }
    enum class Component {
        W,X,Y,Z
    }
}