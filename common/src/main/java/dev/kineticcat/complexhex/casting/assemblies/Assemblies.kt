package dev.kineticcat.complexhex.casting.assemblies

import dev.kineticcat.complexhex.Complexhex
import dev.kineticcat.complexhex.casting.assemblies.complex.AssemblyComplex
import dev.kineticcat.complexhex.casting.assemblies.complex.BelgaerAssemblyComplex
import dev.kineticcat.complexhex.casting.assemblies.complex.HoldoutAssembyComplex
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

@Suppress("unused")
class Assemblies {
    companion object {
        var ASSEMBLIES: MutableMap<ResourceLocation, AbstractAssemblyController> = HashMap()

        var CUBE = assembly("cube", IsogonalAssemblyController(8, 3, HoldoutAssembyComplex()))
        var TRIANGLE = assembly("triangle", IsogonalAssemblyController(3, 2, AssemblyComplex()))

        var RHOMBICUBOCTAHEDRON = assembly("rhombicuboctahedron", IsogonalAssemblyController(24, 4, BelgaerAssemblyComplex()))

        private fun assembly(name: String, controller: AbstractAssemblyController): AbstractAssemblyController {
            ASSEMBLIES[Complexhex.id(name)] = controller
            return controller
        }

        fun getController(resLoc: ResourceLocation): AbstractAssemblyController? {
            return ASSEMBLIES[resLoc]
        }

        fun getController(name: String): AbstractAssemblyController? {
            return ASSEMBLIES[ResourceLocation(name)]
        }

        fun findController(verts: List<Vec3>): String? {
            for ((key, value) in ASSEMBLIES) {
                if (value.testVertices(verts)) {
                    return key.toString()
                }
            }
            return null
        }
    }
}
