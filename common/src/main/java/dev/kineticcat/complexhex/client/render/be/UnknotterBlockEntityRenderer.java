package dev.kineticcat.complexhex.client.render.be;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.kineticcat.complexhex.block.entity.UnknotterBlockEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static dev.kineticcat.complexhex.Complexhex.id;

public class UnknotterBlockEntityRenderer implements BlockEntityRenderer<UnknotterBlockEntity> {

//    private static final Matrix3f forward = new Matrix3f().rotateXYZ((float) Math.toRadians(0), 0, (float) Math.toRadians(0));
//    private static final Matrix3f backward = new Matrix3f().rotateXYZ((float) Math.toRadians(0), 0, (float) Math.toRadians(0)).invert();

    public UnknotterBlockEntityRenderer() {
        LayerDefinition layerdef = getLayerDefinition();
        this.root = layerdef.bakeRoot();
        this.knot = root.getChild("knot");
    }

    private final ModelPart root;
    private final ModelPart knot;

    public static LayerDefinition getLayerDefinition() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild(
                "knot",
            CubeListBuilder.create(),
                PartPose.ZERO
        );

        root.getChild("knot").addOrReplaceChild(
                "1",
                CubeListBuilder.create().texOffs(38,24).addBox(-1,-1,-1, 2, 2, 3)
                ,
                PartPose.offsetAndRotation(-4.5f, -4.5f, -1.5f, 0, 0, (float) Math.PI)
        );
        root.getChild("knot").addOrReplaceChild(
                "2",
                CubeListBuilder.create().texOffs(16, 32).addBox(-1, -1, -1, 6, 2, 2)
                ,
                PartPose.offsetAndRotation(1.5f, -4.5f, -1.5f, 0, 0, (float) Math.PI)
        );
        root.getChild("knot").addOrReplaceChild(
                "3",
                CubeListBuilder.create().texOffs(16, 36).addBox(-1, -1, -1, 2, 9, 2)
                ,
                PartPose.offsetAndRotation(1.5f, 4.5f, -1.5f, 0, 0, (float) Math.PI)
        );
        root.getChild("knot").addOrReplaceChild(
                "4",
                CubeListBuilder.create().texOffs(22, 24).addBox(-1, -1, -1, 2, 2, 6)
                ,
                PartPose.offsetAndRotation(1.5f, 4.5f, 0.5f, 0, 0, (float) Math.PI)
        );
        root.getChild("knot").addOrReplaceChild(
                "5",
                CubeListBuilder.create().texOffs(0, 39).addBox(-1, -1, -1, 3, 2, 2)
                ,
                PartPose.offsetAndRotation(-0.5f, 4.5f, 4.5f, 0, 0, (float) Math.PI)
        );
        root.getChild("knot").addOrReplaceChild(
                "6",
                CubeListBuilder.create().texOffs(24, 36).addBox(-1, -1, -1, 2, 6, 2)
                ,
                PartPose.offsetAndRotation(-1.5f, 2.5f, 4.5f, 0, 0, (float) Math.PI)
        );
        root.getChild("knot").addOrReplaceChild(
                "7",
                CubeListBuilder.create().texOffs(0, 20).addBox(-1, -1, -1, 2, 2, 9)
                ,
                PartPose.offsetAndRotation(-1.5f, -1.5f, -4.5f, 0, 0, (float) Math.PI)
        );
        root.getChild("knot").addOrReplaceChild(
                "8",
                CubeListBuilder.create().texOffs(32, 32).addBox(-1, -1, -1, 6, 2, 2)
                ,
                PartPose.offsetAndRotation(4.5f, -1.5f, -4.5f, 0, 0, (float) Math.PI)
        );
        root.getChild("knot").addOrReplaceChild(
                "9",
                CubeListBuilder.create().texOffs(40, 36).addBox(-1, -1, -1, 2, 3, 2)
                ,
                PartPose.offsetAndRotation(4.5f, 1.5f, -4.5f, 0, 0, (float) Math.PI)
        );
        root.getChild("knot").addOrReplaceChild(
                "10",
                CubeListBuilder.create().texOffs(0, 31).addBox(-1, -1, -1, 2, 2, 6)
                ,
                PartPose.offsetAndRotation(4.5f, 1.5f, -2.5f, 0, 0, (float) Math.PI)
        );
        root.getChild("knot").addOrReplaceChild(
                "11",
                CubeListBuilder.create().texOffs(22, 20).addBox(-1, -1, -1, 9, 2, 2)
                ,
                PartPose.offsetAndRotation(2.5f, 1.5f, 1.5f, 0, 0, (float) Math.PI)
        );
        root.getChild("knot").addOrReplaceChild(
                "12",
                CubeListBuilder.create().texOffs(32, 36).addBox(-1, -1, -1, 2, 6, 2)
                ,
                PartPose.offsetAndRotation(-4.5f, -0.5f, 1.5f, 0, 0, (float) Math.PI)
        );


        return LayerDefinition.create(mesh,64, 64);
    }
    @Override
    public void render(UnknotterBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {

        Matrix3f forward = new Matrix3f().rotateXYZ((float) Math.toRadians(35), 0, (float) Math.toRadians(-45));
        Matrix3f backward = forward.invert(new Matrix3f());

        poseStack.pushPose();
        poseStack.mulPoseMatrix(new Matrix4f(forward));
        Vector3f offset = new Vector3f(0.5f, 8.25f/16, 0.5f).mul(backward);
        poseStack.translate(offset.x, offset.y, offset.z);

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(id("textures/block/unknot.png")));
        knot.render(poseStack, consumer, light, overlay);
        poseStack.popPose();
    }
}
