package dev.kineticcat.complexhex.casting.assemblies

import dev.kineticcat.complexhex.Complexhex
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

@Suppress("unused")
class Assemblies {
    companion object {
        var ASSEMBLIES: MutableMap<ResourceLocation, AbstractAssemblyController> = HashMap()

        var CUBE = assembly("cube", SimpleAssemblyController(8, 3) {entity -> Complexhex.LOGGER.info(entity) })
        var TRIANGLE = assembly("triangle", SimpleAssemblyController(3, 2) {entity -> Complexhex.LOGGER.info(entity) })

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
