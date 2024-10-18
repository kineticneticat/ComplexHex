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
        int colour = nix.getPigment().getColorProvider().getColor(nix.level().getGameTime()/*/4000F*/, nix.position().add(new Vec3(nix.level().getGameTime()/100f, 0, 0)));
        int black = 0;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);



        float outer = 4f / 16f;
        float inner = 3.5f / 16f;
        float hitboxSize = 4f / 16f;

        Vector3f outerSize = new Vector3f(outer, outer, outer);
        Vector3f innerSize = new Vector3f(inner, inner, inner);

        ps.pushPose();

        ps.mulPose(Axis.YP.rotationDegrees(180f - yaw));
//        ps.mulPose(Axis.XP.rotationDegrees(180 - nix.getXRot()));
        ps.mulPose(Axis.ZP.rotationDegrees(180f));

        nix.getLookAngle();

        int light = LevelRenderer.getLightColor(nix.level(), BlockPos.containing(nix.position()));

        drawCube(nix, outerSize, new Vector3f(-outerSize.x / 2f, -outerSize.y / 2f - hitboxSize / 2f, outerSize.z / 2f), ps, multiBufferSource, light, colour, true);
        drawCube(nix, innerSize, new Vector3f(-innerSize.x / 2f, -innerSize.y / 2f - hitboxSize / 2f, innerSize.z / 2f), ps, multiBufferSource, light, black, false);

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

    private void drawCube(
            NixEntity nix,
            Vector3f vec,
            Vector3f translate,
            PoseStack ps,
            MultiBufferSource buffer,
            int light,
            int colour,
            boolean inverseHull
    ) {
        ps.pushPose();
        // X is right, Y is down, Z is *in*
        // Our origin will be the lower-left corner of the scroll touching the wall
        // (so it has "negative" thickness)
        ps.translate(translate.x, translate.y, translate.z);

        float dx = vec.x, dy = vec.y, dz = -vec.z;
                // inverseHull ? 0 : vec.x
                // inverseHull ? 0 : vec.y
                // inverseHull ? 0 : -vec.z
        var last = ps.last();
        var mat = last.pose();
        var norm = last.normal();

        var verts = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(nix)));
        // Remember: CCW
        // Front face
        vertex(mat, norm, light, verts, 0xffffffff, 0, 0, inverseHull ? 0 : -vec.z, 0, 0, 0, 0, -1);
        vertex(mat, norm, light, verts, 0xffffffff, 0, dy, inverseHull ? 0 : -vec.z, 0, 1, 0, 0, -1);
        vertex(mat, norm, light, verts, 0xffffffff, dx, dy, inverseHull ? 0 : -vec.z, .5f, 1, 0, 0, -1);
        vertex(mat, norm, light, verts, 0xffffffff, dx, 0, inverseHull ? 0 : -vec.z, .5f, 0, 0, 0, -1);
        // Back face
        vertex(mat, norm, light, verts, colour, 0, 0, inverseHull ? -vec.z : 0, 0, 0, 0, 0, 1);
        vertex(mat, norm, light, verts, colour, dx, 0, inverseHull ? -vec.z : 0, .5f, 0, 0, 0, 1);
        vertex(mat, norm, light, verts, colour, dx, dy, inverseHull ? -vec.z : 0, .5f, 1, 0, 0, 1);
        vertex(mat, norm, light, verts, colour, 0, dy, inverseHull ? -vec.z : 0, 0, 1, 0, 0, 1);
        // Top face
        vertex(mat, norm, light, verts, colour, 0, inverseHull ? vec.y : 0, 0, 0, 0, 0, -1, 0);
        vertex(mat, norm, light, verts, colour, 0, inverseHull ? vec.y : 0, dz, 0, 1, 0, -1, 0);
        vertex(mat, norm, light, verts, colour, dx, inverseHull ? vec.y : 0, dz, .5f, 1, 0, -1, 0);
        vertex(mat, norm, light, verts, colour, dx, inverseHull ? vec.y : 0, 0, .5f, 0, 0, -1, 0);
        // Left face
        vertex(mat, norm, light, verts, colour, inverseHull ? vec.x : 0, 0, 0, 0, 0, -1, 0, 0);
        vertex(mat, norm, light, verts, colour, inverseHull ? vec.x : 0, dy, 0, 0, 1, -1, 0, 0);
        vertex(mat, norm, light, verts, colour, inverseHull ? vec.x : 0, dy, dz, .5f, 1, -1, 0, 0);
        vertex(mat, norm, light, verts, colour, inverseHull ? vec.x : 0, 0, dz, .5f, 0, -1, 0, 0);
        // Right face
        vertex(mat, norm, light, verts, colour, inverseHull ? 0 : vec.x, 0, dz, 0, 0, 1, 0, 0);
        vertex(mat, norm, light, verts, colour, inverseHull ? 0 : vec.x, dy, dz, 0, 1, 1, 0, 0);
        vertex(mat, norm, light, verts, colour, inverseHull ? 0 : vec.x, dy, 0, .5f, 1, 1, 0, 0);
        vertex(mat, norm, light, verts, colour, inverseHull ? 0 : vec.x, 0, 0, .5f, 0, 1, 0, 0);
        // Bottom face
        vertex(mat, norm, light, verts, colour, 0, inverseHull ? 0 : vec.y, dz, 0, 0, 0, 1, 0);
        vertex(mat, norm, light, verts, colour, 0, inverseHull ? 0 : vec.y, 0, 0, 1, 0, 1, 0);
        vertex(mat, norm, light, verts, colour, dx, inverseHull ? 0 : vec.y, 0, .5f, 1, 0, 1, 0);
        vertex(mat, norm, light, verts, colour, dx, inverseHull ? 0 : vec.y, dz, .5f, 0, 0, 1, 0);
        ps.popPose();
    }
}