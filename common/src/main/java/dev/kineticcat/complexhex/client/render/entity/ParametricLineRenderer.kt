package dev.kineticcat.complexhex.client.render.entity

import at.petrak.hexcasting.api.pigment.FrozenPigment
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.kineticcat.complexhex.Complexhex.id
import dev.kineticcat.complexhex.api.util.Value
import dev.kineticcat.complexhex.entity.ParametricLineEntity
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3
import org.joml.Matrix3f
import org.joml.Matrix4f
import kotlin.math.cos
import kotlin.math.sin


class ParametricLineRenderer(context: EntityRendererProvider.Context): EntityRenderer<ParametricLineEntity>(context) {
    val tex: ResourceLocation = id("textures/entity/holdout.png")

    override fun getTextureLocation(entity: ParametricLineEntity) = tex

    override fun render(para: ParametricLineEntity, f: Float, g: Float, poseStack: PoseStack, multiBufferSource: MultiBufferSource, i: Int) {
        val steps = 100.0
        var start: Vec3
        var end: Vec3
        for (t in 0 until steps.toInt()) {
            start = getPosAtT(para, t/steps, para.position(), para.level().gameTime) ?: Vec3(t/steps, t/steps, t/steps)
            end = getPosAtT(para, (t+1)/steps, para.position(), para.level().gameTime) ?: Vec3((t+1)/steps, (t+1)/steps, (t+1)/steps)
            line(poseStack, multiBufferSource.getBuffer(RenderType.entityCutout(tex)), start, end, para.pigment, 0.05)
        }
    }
    fun getPosAtT(para: ParametricLineEntity, t: Double, origin: Vec3, time: Long): Vec3? {
        val x = para.xpr("t", t)("x", origin.x)("y", origin.y)("z", origin.z)("w", time.toDouble()).let { if (it is Value) it.x else return null }
        val y = para.ypr("t", t)("x", origin.x)("y", origin.y)("z", origin.z)("w", time.toDouble()).let { if (it is Value) it.x else return null }
        val z = para.zpr("t", t)("x", origin.x)("y", origin.y)("z", origin.z)("w", time.toDouble()).let { if (it is Value) it.x else return null }
        return Vec3(x, y, z)
    }
    @Suppress("SameParameterValue")
    private fun line(poseStack: PoseStack, vertices: VertexConsumer, start: Vec3, end: Vec3, pigment: FrozenPigment, thickness: Double) {
        val direction = end.subtract(start).normalize()
        var perpendicular = direction.cross(Vec3(1.0, 0.0, 0.0))
        if (direction.dot(Vec3(1.0, 0.0, 0.0)) > 0.99 || direction.dot(Vec3(1.0, 0.0, 0.0)) < -0.99)
            perpendicular = direction.cross(Vec3(0.0, 1.0, 0.0))

        val pose = poseStack.last().pose()
        val norm = poseStack.last().normal()

        val perpendicularCrossProduct = perpendicular.cross(direction)
        val directionDotProduct = direction.scale(direction.dot(perpendicular))

        for (i in 0 until 6) {
            val startAngle = i * 2 * Math.PI / 6
            val endAngle = (i + 1) % 2 * Math.PI
            val a = perpendicular.scale(cos(startAngle)).add(perpendicularCrossProduct.scale(sin(startAngle))).add(directionDotProduct.scale(1 - cos(startAngle))).normalize().scale(thickness)
            val b = perpendicular.scale(cos(endAngle)).add(perpendicularCrossProduct.scale(sin(endAngle))).add(directionDotProduct.scale(1 - cos(endAngle))).normalize().scale(thickness)

            vertex(pose, norm, vertices, start.add(a), pigment)
            vertex(pose, norm, vertices, start.add(b), pigment)
            vertex(pose, norm, vertices, end.add(b), pigment)
            vertex(pose, norm, vertices, end.add(a), pigment)
        }
    }
    private fun vertex(pose: Matrix4f, norm: Matrix3f, vertices: VertexConsumer, position: Vec3, pigment: FrozenPigment) {
        vertices.vertex(pose, position.x.toFloat(), position.y.toFloat(), position.z.toFloat())
            .color(pigment.colorProvider.getColor(0f, position))
            .uv(0f, 0f)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(LightTexture.FULL_BRIGHT)
            .normal(norm, 0f, 1f, 0f)
            .endVertex()
    }
}