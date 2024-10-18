package dev.kineticcat.complexhex.client.render.entity.holdoutrenderingshenannigans;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.kineticcat.complexhex.entity.HoldoutEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static dev.kineticcat.complexhex.Complexhex.id;

public class HoldoutRenderer extends EntityRenderer<HoldoutEntity> {

    public static final ResourceLocation tex = id("textures/entity/holdout.png");

    public HoldoutRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull HoldoutEntity entity) {return tex;}

    @Override
    public void render(HoldoutEntity holdout, float yaw, float partialTick, PoseStack ps, MultiBufferSource multiBufferSource, int packedLight) {
        Vec3 pos = holdout.position();

        RenderSystem.enableDepthTest();
        LaggingMaskRenderTarget.draw(holdout.level().getGameTime());

        if (!(multiBufferSource instanceof MultiBufferSource.BufferSource buffer)) return;
        LaggingMaskRenderTarget.use(() -> {
//            Renderer3d.renderFilled(ps, Color.WHITE, new Vec3(0, 70, 0), new Vec3(2, 2, 2));


            int colour = 0xffffffff;
            int black = 0;



            float inner = 1f;
    //        float hitboxSize = 4f / 16f;

            Vector3f innerSize = new Vector3f(inner, inner, inner);

            ps.pushPose();

            ps.mulPose(Axis.YP.rotationDegrees(180f - yaw));
    //        ps.mulPose(Axis.XP.rotationDegrees(180 - nix.getXRot()));
            ps.mulPose(Axis.ZP.rotationDegrees(180f));

            int light = LevelRenderer.getLightColor(holdout.level(), BlockPos.containing(holdout.position()));
            drawCube(holdout, innerSize, new Vector3f(-innerSize.x / 2f, -innerSize.y / 2f, innerSize.z / 2f), ps, buffer, 16, 0x00_ffffff);
            ps.popPose();

        });

        super.render(holdout, yaw, partialTick, ps, multiBufferSource, packedLight);
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
            HoldoutEntity holdout,
            Vector3f vec,
            Vector3f translate,
            PoseStack ps,
            MultiBufferSource.BufferSource buffer,
            int light,
            int colour
    ) {
        ps.pushPose();
        // X is right, Y is down, Z is *in*
        // Our origin will be the lower-left corner of the scroll touching the wall
        // (so it has "negative" thickness)
        ps.translate(translate.x, translate.y, translate.z);

        float dx = vec.x, dy = vec.y, dz = -vec.z;
        // vec.x
        // vec.y
        // -vec.z
        var last = ps.last();
        var mat = last.pose();
        var norm = last.normal();

        var verts = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(holdout)));
        // Remember: CCW
        // Front face
        vertex(mat, norm, light, verts, colour, 0, 0, dz, 0, 0, 0, 0, -1);
        vertex(mat, norm, light, verts, colour, 0, dy, dz, 0, 1, 0, 0, -1);
        vertex(mat, norm, light, verts, colour, dx, dy, dz, .5f, 1, 0, 0, -1);
        vertex(mat, norm, light, verts, colour, dx, 0, dz, .5f, 0, 0, 0, -1);
        // Back face
        vertex(mat, norm, light, verts, colour, 0, 0, 0, 0, 0, 0, 0, 1);
        vertex(mat, norm, light, verts, colour, dx, 0, 0, .5f, 0, 0, 0, 1);
        vertex(mat, norm, light, verts, colour, dx, dy, 0, .5f, 1, 0, 0, 1);
        vertex(mat, norm, light, verts, colour, 0, dy, 0, 0, 1, 0, 0, 1);
        // Top face
        vertex(mat, norm, light, verts, colour, 0, 0, 0, 0, 0, 0, -1, 0);
        vertex(mat, norm, light, verts, colour, 0, 0, dz, 0, 1, 0, -1, 0);
        vertex(mat, norm, light, verts, colour, dx, 0, dz, .5f, 1, 0, -1, 0);
        vertex(mat, norm, light, verts, colour, dx, 0, 0, .5f, 0, 0, -1, 0);
        // Left face
        vertex(mat, norm, light, verts, colour, 0, 0, 0, 0, 0, -1, 0, 0);
        vertex(mat, norm, light, verts, colour, 0, dy, 0, 0, 1, -1, 0, 0);
        vertex(mat, norm, light, verts, colour, 0, dy, dz, .5f, 1, -1, 0, 0);
        vertex(mat, norm, light, verts, colour, 0, 0, dz, .5f, 0, -1, 0, 0);
        // Right face
        vertex(mat, norm, light, verts, colour, dx, 0, dz, 0, 0, 1, 0, 0);
        vertex(mat, norm, light, verts, colour, dx, dy, dz, 0, 1, 1, 0, 0);
        vertex(mat, norm, light, verts, colour, dx, dy, 0, .5f, 1, 1, 0, 0);
        vertex(mat, norm, light, verts, colour, dx, 0, 0, .5f, 0, 1, 0, 0);
        // Bottom face
        vertex(mat, norm, light, verts, colour, 0, dy, dz, 0, 0, 0, 1, 0);
        vertex(mat, norm, light, verts, colour, 0, dy, 0, 0, 1, 0, 1, 0);
        vertex(mat, norm, light, verts, colour, dx, dy, 0, .5f, 1, 0, 1, 0);
        vertex(mat, norm, light, verts, colour, dx, dy, dz, .5f, 0, 0, 1, 0);
        ps.popPose();

        buffer.endLastBatch();

    }

//    public RenderType holdout() {
//        return COPY;
//    }
//
//    private static final RenderStateShard.TransparencyStateShard HOLDOUT_TRANSPARENCY =
//            new RenderStateShard.TransparencyStateShard("holdout_transparency", () -> {
//        RenderSystem.enableBlend();
//        RenderSystem.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
//    }, () -> {});
//
//    private static final RenderType HOLDOUT = RenderType.create(
//            id("holdout").toString(),
//            DefaultVertexFormat.NEW_ENTITY,
//            VertexFormat.Mode.QUADS,
//            256,
//            RenderType.CompositeState.builder()
//                    .setTransparencyState(HOLDOUT_TRANSPARENCY)
//                    .setShaderState(RenderStateShard.NO_SHADER)
//                    .createCompositeState(false)
//    );
//    private static final RenderType COPY =
//            RenderType.create(
//                    "holdout",
//                    DefaultVertexFormat.NEW_ENTITY,
//                    VertexFormat.Mode.QUADS,
//                    256,
//                    true,
//                    false,
//                    RenderType.CompositeState.builder()
//                            .setShaderState(RenderType.RENDERTYPE_TEXT_SEE_THROUGH_SHADER)
//                            .setTextureState( new RenderStateShard.TextureStateShard(tex, false, false))
//                            .setTransparencyState(HOLDOUT_TRANSPARENCY)
//                            .setLightmapState(RenderType.NO_LIGHTMAP)
//                            .setOverlayState(RenderType.NO_OVERLAY)
//                            .createCompositeState(true)
//            );
}
