package dev.kineticcat.complexhex.api

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.pigment.FrozenPigment
import dev.kineticcat.complexhex.api.casting.iota.QuaternionIota
import dev.kineticcat.complexhex.api.util.Quaternion
import net.minecraft.util.RandomSource
import net.minecraft.world.phys.Vec3

// stolen from hexcasting lmao
fun List<Iota>.getQuaternion(idx: Int, argc: Int = 0): Quaternion {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is QuaternionIota) {
        return x.quaternion
    } else {
        throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "quaternion")
    }
}
// from hexal
fun nextColour(pigment:FrozenPigment, random: RandomSource): Int {
    return pigment.colorProvider.getColor(
        random.nextFloat() * 16384,
        Vec3(
            random.nextFloat().toDouble(),
            random.nextFloat().toDouble(),
            random.nextFloat().toDouble()
        ).scale((random.nextFloat() * 3).toDouble())
    )
}


