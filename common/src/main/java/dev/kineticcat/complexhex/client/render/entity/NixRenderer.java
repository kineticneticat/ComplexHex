package dev.kineticcat.complexhex.client.render.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.kineticcat.complexhex.entity.NixEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static dev.kineticcat.complexhex.Complexhex.id;

public class NixRenderer extends EntityRenderer<NixEntity> {

    public NixRenderer (EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(NixEntity entity) {
        return id("textures/entity/nix.png");
    }

    @Override
    public void render(NixEntity nix, float yaw, float partialTick, PoseStack ps, MultiBufferSource multiBufferSource, int packedLight) {
        RandomSource levelRandom = nix.level().random;
        int colour = nix.getPigment().getColorProvider().getColor(nix.level().getGameTime()/*/4000F*/, nix.position().add(new Vec3(nix.level().getGameTime()/100f, 0, 0)));
        int black = 0;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Vector3f outer = new Vector3f(.25f, .25f, .25f);
        Vector3f inner = new Vector3f(.125f, .125f, .125f);

        ps.pushPose();

        ps.mulPose(Axis.YP.rotationDegrees(180f - yaw));
        ps.mulPose(Axis.ZP.rotationDegrees(180f));

        int light = LevelRenderer.getLightColor(nix.level(), BlockPos.containing(nix.position()));

        {
            ps.pushPose();
            // X is right, Y is down, Z is *in*
            // Our origin will be the lower-left corner of the scroll touching the wall
            // (so it has "negative" thickness)
            ps.translate(-3 / 2f, -3 / 2f, 1/32f);

            float dx = outer.x, dy = outer.y, dz = -outer.z;
            var last = ps.last();
            var mat = last.pose();
            var norm = last.normal();

            var verts = multiBufferSource.getBuffer(RenderType.entityCutout(this.getTextureLocation(nix)));
            // Remember: CCW
            // Front face
            vertex(mat, norm, light, verts, colour, 0, 0, dz, 0, 0, 0, 0, 1);
            vertex(mat, norm, light, verts, colour, 0, dy, dz, 0, 1, 0, 0, 1);
            vertex(mat, norm, light, verts, colour, dx, dy, dz, 1, 1, 0, 0, 1);
            vertex(mat, norm, light, verts, colour, dx, 0, dz, 1, 0, 0, 0, 1);
            // Back face
            vertex(mat, norm, light, verts, colour, 0, 0, 0, 0, 0, 0, 0, -1);
            vertex(mat, norm, light, verts, colour, dx, 0, 0, 1, 0, 0, 0, -1);
            vertex(mat, norm, light, verts, colour, dx, dy, 0, 1, 1, 0, 0, -1);
            vertex(mat, norm, light, verts, colour, 0, dy, 0, 0, 1, 0, 0, -1);
            // Top face
            vertex(mat, norm, light, verts, colour, 0, 0, 0, 0, 0, 0, 1, 0);
            vertex(mat, norm, light, verts, colour, 0, 0, dz, 0, 0, 0, 1, 0);
            vertex(mat, norm, light, verts, colour, dx, 0, dz, 1, 0, 0, 1, 0);
            vertex(mat, norm, light, verts, colour, dx, 0, 0, 1, 0, 0, 1, 0);
            // Left face
            vertex(mat, norm, light, verts, colour, 0, 0, 0, 0, 0, 1, 0, 0);
            vertex(mat, norm, light, verts, colour, 0, dy, 0, 0, 1, 1, 0, 0);
            vertex(mat, norm, light, verts, colour, 0, dy, dz, 0, 1, 1, 0, 0);
            vertex(mat, norm, light, verts, colour, 0, 0, dz, 0, 0, 1, 0, 0);
            // Right face
            vertex(mat, norm, light, verts, colour, dx, 0, dz, 1, 0, -1, 0, 0);
            vertex(mat, norm, light, verts, colour, dx, dy, dz, 1, 1, -1, 0, 0);
            vertex(mat, norm, light, verts, colour, dx, dy, 0, 1, 1, -1, 0, 0);
            vertex(mat, norm, light, verts, colour, dx, 0, 0, 1, 0, -1, 0, 0);
            // Bottom face
            vertex(mat, norm, light, verts, colour, 0, dy, dz, 0, 1, 0, -1, 0);
            vertex(mat, norm, light, verts, colour, 0, dy, 0, 0, 1, 0, -1, 0);
            vertex(mat, norm, light, verts, colour, dx, dy, 0, 1, 1, 0, -1, 0);
            vertex(mat, norm, light, verts, colour, dx, dy, dz, 1, 1, 0, -1, 0);

            ps.popPose();
        }
        ps.popPose();
        super.render(nix, yaw, partialTick, ps, multiBufferSource, packedLight);
    }

    private static void vertex(
            Matrix4f mat,
            Matrix3f normal,
            int light,
            VertexConsumer verts,
            int colour,
            float x, float y, float z,
            float u, float v,
            float nx, float ny, float nz) {
        verts.vertex(mat, x, y, z)
                .color(colour)
                .uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(normal, nx, ny, nz)
                .endVertex();
    }
}