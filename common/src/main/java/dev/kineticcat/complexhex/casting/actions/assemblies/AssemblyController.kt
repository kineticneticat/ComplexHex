package dev.kineticcat.complexhex.casting.actions.assemblies

import dev.kineticcat.complexhex.entity.AssemblyManagerEntity
import net.minecraft.world.phys.Vec3
import kotlin.math.pow
import kotlin.math.sqrt

class AssemblyController(val test: (verts: List<Vec3>) -> Boolean) {

    companion object {
        fun simple(vertCount: Int): AssemblyController {
            fun test(verts: List<Vec3>): Boolean {
                if (verts.size != vertCount) return false

                val centre = AssemblyManagerEntity.centre(verts);
                val dists = verts.map {v -> v.subtract(centre).length() }
                val avgdist = dists.reduce{a, b -> a+b} / dists.size
                val sd = sqrt(dists.map{a -> (a - avgdist).pow(2.0) }.reduce{ a, b -> a+b} / dists.size)
                return sd <= 1

            }
            return AssemblyController {l -> test(l)}
        }
    }

}