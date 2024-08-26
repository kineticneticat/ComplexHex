package dev.kineticcat.complexhex.casting.assemblies

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.phys.Vec3
import net.minecraft.world.entity.Entity

abstract class AbstractAssemblyController {

    @JvmRecord
    data class Edge(val A: Int, val B: Int) {
        fun asTag(): CompoundTag {
            val ctag = CompoundTag()
            ctag.putInt("A", A)
            ctag.putInt("B", B)
            return ctag
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            val (A1, B1) = other as Edge
            return (A1 == A && B1 == B) || (A1 == B && B1 == A)
        }

        fun hasEndpoint(p: Int): Boolean {
            return A == p || B == p
        }

        fun length(verts: List<Vec3>): Double {
            return verts[A].subtract(verts[B]).length()
        }

        override fun hashCode(): Int {
            return (31 * A) + B
        }

        fun lerp(verts: List<Vec3>, t: Double): Vec3 {
            val f = ( if (t < 0.0)  0.0 else ( if ( t>1.0 ) 1.0 else t ) )
            return verts[A].scale(1.0-f).add(verts[B].scale(f))
        }

        companion object {
            fun fromTag(ctag: CompoundTag): Edge {
                return Edge(ctag.getInt("A"), ctag.getInt("B"))
            }

            fun listAsTag(edges: List<Edge>): ListTag {
                val ltag = ListTag()
                for (edge in edges) {
                    ltag.add(edge.asTag())
                }
                return ltag
            }

            fun tagAsList(ltag: ListTag): List<Edge> {
                return ltag.stream().map { tag: Tag ->
                    fromTag(
                        tag as CompoundTag
                    )
                }.toList()
            }
        }
    }


    abstract fun genEdges(verts: List<Vec3>): List<Edge>

    abstract fun testVertices(verts: List<Vec3>): Boolean

    abstract fun isEntityWithinBounds(entity: Entity, centre: Vec3, radius: Double): Boolean
    abstract fun applyEffectToEntity(entity: Entity)
}