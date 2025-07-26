package dev.kineticcat.complexhex.client.render.entity

import at.petrak.hexcasting.api.pigment.FrozenPigment
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.kineticcat.complexhex.Complexhex.id
import dev.kineticcat.complexhex.api.util.Value
import dev.kineticcat.complexhex.entity.ParametricSurfaceEntity
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


class ParametricSurfaceRenderer(context: EntityRendererProvider.Context): EntityRenderer<ParametricSurfaceEntity>(context) {
    val tex: ResourceLocation = id("textures/entity/holdout.png")

    override fun getTextureLocation(entity: ParametricSurfaceEntity) = tex

    override fun render(para: ParametricSurfaceEntity, f: Float, g: Float, poseStack: PoseStack, buffer: MultiBufferSource, i: Int) {
        val steps = 10.0
        var minmin: Vec3
        var minmax: Vec3
        var maxmin: Vec3
        var maxmax: Vec3
        var un: Double
        var up: Double
        var vn: Double
        var vp: Double
        for (u in 0 until steps.toInt()) {
            for (v in 0 until steps.toInt()) {
                un = u/steps
                up = (u+1)/steps
                vn = v/steps
                vp = (v+1)/steps
                minmin = getPosAtUV(para, un, vn, para.position(), para.level().gameTime) ?: Vec3(un, 0.0, vn)
                minmax = getPosAtUV(para, un, vp, para.position(), para.level().gameTime) ?: Vec3(un, 0.0, vp)
                maxmin = getPosAtUV(para, up, vn, para.position(), para.level().gameTime) ?: Vec3(up, 0.0, vn)
                maxmax = getPosAtUV(para, up, vp, para.position(), para.level().gameTime) ?: Vec3(up, 0.0, vp)
                quad(poseStack, buffer, para.pigment, minmin, minmax, maxmin, maxmax, para.level().gameTime)
            }
        }
    }
    fun getPosAtUV(para: ParametricSurfaceEntity,u: Double, v: Double, origin: Vec3, time: Long): Vec3? {
        val x = para.xpr("u", u)("v", v)("x", origin.x)("y", origin.y)("z", origin.z)("w", time.toDouble()).let { if (it is Value) it.x else return null }
        val y = para.ypr("u", u)("v", v)("x", origin.x)("y", origin.y)("z", origin.z)("w", time.toDouble()).let { if (it is Value) it.x else return null }
        val z = para.zpr("u", u)("v", v)("x", origin.x)("y", origin.y)("z", origin.z)("w", time.toDouble()).let { if (it is Value) it.x else return null }
        return Vec3(x, y, z)
    }

    fun quad(poseStack: PoseStack, buffer: MultiBufferSource, pigment: FrozenPigment, minmin: Vec3, minmax: Vec3, maxmin: Vec3, maxmax: Vec3, time: Long) {
        poseStack.pushPose()
        val pose = poseStack.last().pose()
        val normal = poseStack.last().normal()
        val vertices = buffer.getBuffer(RenderType.entityCutout(tex))
        vertex(pose, normal, vertices, minmin, pigment, time)
        vertex(pose, normal, vertices, minmax, pigment, time)
        vertex(pose, normal, vertices, maxmax, pigment, time)
        vertex(pose, normal, vertices, maxmin, pigment, time)
        poseStack.popPose()
    }
    private fun vertex(pose: Matrix4f, normal: Matrix3f, vertices: VertexConsumer, position: Vec3, pigment: FrozenPigment, time: Long) {
        vertices.vertex(pose, position.x.toFloat(), position.y.toFloat(), position.z.toFloat())
            .color(pigment.colorProvider.getColor(time.toFloat(), position))
            .uv(0f, 0f)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(LightTexture.FULL_BRIGHT)
            .normal(normal, 0f, 1f, 0f)
            .endVertex()
    }
}