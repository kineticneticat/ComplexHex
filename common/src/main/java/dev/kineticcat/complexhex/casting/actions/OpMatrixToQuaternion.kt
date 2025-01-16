package dev.kineticcat.complexhex.casting.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import dev.kineticcat.complexhex.stuff.Quaternion
import ram.talia.moreiotas.api.getMatrix
import kotlin.math.sqrt

// code stolen from:
// https://www.euclideanspace.com/maths/geometry/rotations/conversions/matrixToQuaternion/
object OpMatrixToQuaternion : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val M = args.getMatrix(0)
        if (M.columns != 3 || M.rows != 3) throw MishapInvalidIota.of(args[0], 0, "not3x3matrix")

        val tr = M[0,0] +M[1,1] + M[2,2]
        val a: Double
        val b: Double
        val c: Double
        val d: Double

        if (tr > 0) {
            val S = sqrt(tr + 1.0) * 2; // S=4*a
            a = 0.25 * S;
            b = (M[2,1] - M[1,2]) / S;
            c = (M[0,2] - M[2,0]) / S;
            d = (M[1,0] - M[0,1]) / S;
        } else if ((M[0,0] > M[1,1])&&(M[0,0] > M[2,2])) {
            val S = sqrt (1.0 + M[0,0] - M[1,1] - M[2,2]) * 2; // S=4*b
            a = (M[2,1] - M[1,2]) / S;
            b = 0.25 * S;
            c = (M[0,1] + M[1,0]) / S;
            d = (M[0,2] + M[2,0]) / S;
        } else if (M[1,1] > M[2,2]) {
            val S = sqrt (1.0 + M[1,1] - M[0,0] - M[2,2]) * 2; // S=4*c
            a = (M[0,2] - M[2,0]) / S;
            b = (M[0,1] + M[1,0]) / S;
            c = 0.25 * S;
            d = (M[1,2] + M[2,1]) / S;
        } else {
            val S = sqrt (1.0 + M[2,2] - M[0,0] - M[1,1]) * 2; // S=4*d
            a = (M[1,0] - M[0,1]) / S;
            b = (M[0,2] + M[2,0]) / S;
            c = (M[1,2] + M[2,1]) / S;
            d = 0.25 * S;
        }
        return Quaternion(a, b, c, d).asActionResult()
    }
}