package dev.kineticcat.complexhex.client.render.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kineticcat.complexhex.entity.NixEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import static dev.kineticcat.complexhex.Complexhex.id;
import static dev.kineticcat.complexhex.api.FunniesKt.nextColour;

public class NixRenderer extends EntityRenderer<NixEntity> {

    public NixRenderer (EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(NixEntity entity) {
        return id("textures/entity/nix.png");
    }

    @Override
    public void render(NixEntity entity, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight) {
        super.render(entity, yaw, partialTick, poseStack, multiBufferSource, packedLight);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        int colour = nextColour(entity.getPigment(), RandomSource.create());
        VertexConsumer consumer = multiBufferSource.getBuffer(RenderType.entitySolid(getTextureLocation(entity)));
        Vec3 pos = entity.getPosition(partialTick);
        poseStack.pushPose();
        poseStack.translate(pos.x, pos.y, pos.z);
        PoseStack.Pose last = poseStack.last();
        Matrix4f mat = last.pose();
        Matrix3f norm = last.normal();
        // pain
        vertex(mat, norm, packedLight, colour, consumer, 1, 0, 1, 3, 0, 0, -1, 0);
        vertex(mat, norm, packedLight, colour, consumer, -1, 0, 1, 0, 0, 0, -1, 0);
        vertex(mat, norm, packedLight, colour, consumer, -1, 0, -1, 3, 3, 0, -1, 0);
        vertex(mat, norm, packedLight, colour, consumer, 1, 0, -1, 0, 3, 0, -1, 0);
        poseStack.popPose();
    }

    private static void vertex(
            Matrix4f mat,
            Matrix3f normal,
            int light,
            int colour,
            VertexConsumer verts,
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