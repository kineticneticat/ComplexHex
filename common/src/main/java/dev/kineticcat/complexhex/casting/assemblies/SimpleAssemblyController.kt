package dev.kineticcat.complexhex.casting.assemblies

import com.mojang.datafixers.util.Pair
import dev.kineticcat.complexhex.Complexhex
import dev.kineticcat.complexhex.entity.AssemblyManagerEntity
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import kotlin.math.pow
import kotlin.math.sqrt

class SimpleAssemblyController(val vertCount: Int, val edgesPerVertex: Int) : AbstractAssemblyController() {
    override fun genEdges(verts: List<Vec3>): List<Edge> {
        val edges: MutableList<Edge> = ArrayList()
        // this as an abomination
        for (i in verts.indices) {
            while (Edge.countConnectionsToVertex(i, edges) < edgesPerVertex) {
                // i sure hope no edge will be longer than this :)
                var shortestEdge = Pair<Double, Edge?>(1.0E18, null)
                for (k in verts.indices) {
                    if (i == k) continue
                    val edge = Edge(i, k)
                    if (edges.contains(edge)) continue
                    val length = edge.length(verts)
                    if (length < shortestEdge.first) {
                        shortestEdge = Pair(length, edge)
                    }
                }
                if (shortestEdge.second != null) {edges.add(shortestEdge.second!!)}
                Complexhex.LOGGER.info(Edge.countConnectionsToVertex(i, edges))
            }
        }
        return edges

    }

    override fun testVertices(verts: List<Vec3>): Boolean {
        if (verts.size != vertCount) return false

        val centre = AssemblyManagerEntity.centre(verts)
        val dists = verts.map {v -> v.subtract(centre).length() }
        val avgdist = dists.reduce{a, b -> a+b} / dists.size
        val sd = sqrt(dists.map{a -> (a - avgdist).pow(2.0) }.reduce{ a, b -> a+b} / dists.size)
        return sd <= 1

    }

    override fun isEntityWithinBounds(entity: Entity, centre: Vec3, radius: Double): Boolean {
        return entity.eyePosition.subtract(centre).length() < radius
    }

    override fun applyEffectToEntity(entity: Entity) {
        TODO("Not yet implemented")
    }
}