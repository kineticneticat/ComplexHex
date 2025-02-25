package dev.kineticcat.complexhex.client.render.be;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.kineticcat.complexhex.block.HexboxBlock;
import dev.kineticcat.complexhex.block.entity.HexboxBlockEntity;
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
import net.minecraft.resources.ResourceLocation;

import static dev.kineticcat.complexhex.Complexhex.id;

public class HexboxBlockEntityRenderer implements BlockEntityRenderer<HexboxBlockEntity> {

    public static ResourceLocation InertRecordTexture = id("textures/block/hexbox_inert_record.png");
    public static ResourceLocation QuenchedRecordTexture = id("textures/block/hexbox_quenched_record.png");

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
                CubeListBuilder.create().texOffs(5, 1).addBox(1, 14.5f, 1, 14, 0.1f, 14),
                PartPose.ZERO
        );
        return LayerDefinition.create(mesh, 16, 16);
    }

    private final BlockEntityRendererProvider.Context ctx;
    @Override
    public void render(HexboxBlockEntity box, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        poseStack.pushPose();
        int discType = box.getBlockState().getValue(HexboxBlock.DISC_TYPE);
        if (discType !=0) {
            poseStack.mulPose(Axis.YP.rotationDegrees(Math.floorMod(box.getLevel().getGameTime(), 360L)));
            ResourceLocation RecordTexture = discType == 1 ? InertRecordTexture : QuenchedRecordTexture;
            VertexConsumer recordConsumer = bufferSource.getBuffer(RenderType.entityCutout(RecordTexture));
            record.render(poseStack, recordConsumer, light, overlay);
        }
        poseStack.popPose();
    }
}
