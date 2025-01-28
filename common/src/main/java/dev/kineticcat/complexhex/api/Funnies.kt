package dev.kineticcat.complexhex.api

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import dev.kineticcat.complexhex.api.casting.iota.QuaternionIota
import dev.kineticcat.complexhex.stuff.ComplexNumber
import dev.kineticcat.complexhex.stuff.Quaternion

// stolen from hexcasting lmao
fun List<Iota>.getQuaternion(idx: Int, argc: Int = 0): Quaternion {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is QuaternionIota) {
        return x.quaternion
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "quaternion")
    }
}

// yoinked from https://math.toronto.edu/mathnet/questionCorner/complexexp.html
fun CNpow(a: Double, power: ComplexNumber): ComplexNumber {
    val b = power.real
    val c = power.imag
    return ComplexNumber.polar(Math.pow(a,b), c * Math.log(a))
}
