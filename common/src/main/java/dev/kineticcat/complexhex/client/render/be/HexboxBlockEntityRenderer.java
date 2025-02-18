package dev.kineticcat.complexhex.client.render.be;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.block.HexboxBlock;
import dev.kineticcat.complexhex.block.entity.HexboxBlockEntity;
import dev.kineticcat.complexhex.client.RegisterClientStuff;
import dev.kineticcat.complexhex.client.render.ComplexHexGaslighting;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import dev.kineticcat.complexhex.Complexhex;

import static dev.kineticcat.complexhex.Complexhex.id;

public class HexboxBlockEntityRenderer implements BlockEntityRenderer<HexboxBlockEntity> {

    public static ResourceLocation RecordTexture = id("textures/block/hexbox_record.png");

    private final ModelPart root;
    private final ModelPart record;
    public HexboxBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.ctx = ctx;
        LayerDefinition layerdef = getLayerDefinition();
        this.root = layerdef.bakeRoot();
        this.record = root.getChild("record");
    }
    public static LayerDefinition getLayerDefinition() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "record",
                CubeListBuilder.create().addBox(1, 15, 1, 14, 0.1f, 14).texOffs(8,8),
                PartPose.ZERO
        );
        return LayerDefinition.create(mesh, 16, 16);
    }

    private final BlockEntityRendererProvider.Context ctx;
    @Override
    public void render(HexboxBlockEntity box, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        poseStack.pushPose();
        VertexConsumer recordConsumer = bufferSource.getBuffer(RenderType.entityCutout(RecordTexture));
        record.render(poseStack, recordConsumer, 15, overlay);
        poseStack.popPose();
    }
}
