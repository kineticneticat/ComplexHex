package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.getQuaternion
import org.jblas.DoubleMatrix
import ram.talia.moreiotas.api.asActionResult

object OpQuaternionToMatrix : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val Q = args.getQuaternion(0)

        val w = Q.w
        val x = Q.x
        val y = Q.y
        val z = Q.z

        return DoubleMatrix(arrayOf(
            doubleArrayOf(2*(x*x+w*w)-1, 2*(x*y-w*z),   2*(x*z+w*y)   ),
            doubleArrayOf(2*(x*y+w*z),   2*(w*w+y*y)-1, 2*(y*z-w*x)   ),
            doubleArrayOf(2*(x*z-w*y),   2*(y*z+w*x),   2*(w*w+z*z)-1 )
        )).asActionResult
    }
}