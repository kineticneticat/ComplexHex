package dev.kineticcat.complexhex.casting.patterns

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

        val a = Q.a
        val b = Q.b
        val c = Q.c
        val d = Q.d

        return DoubleMatrix(arrayOf(
            doubleArrayOf(2*(b*b+a*a)-1, 2*(b*c-a*d),   2*(b*d+a*c)   ),
            doubleArrayOf(2*(b*c+a*d),   2*(a*a+c*c)-1, 2*(c*d-a*b)   ),
            doubleArrayOf(2*(b*d-a*c),   2*(c*d+a*b),   2*(a*a+d*d)-1 )
        )).asActionResult
    }
}