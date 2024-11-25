package dev.kineticcat.complexhex.client.render.entity;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kineticcat.complexhex.entity.LaTeXEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.*;
import java.awt.image.BufferedImage;

import static dev.kineticcat.complexhex.Complexhex.id;

public class LaTeXRenderer extends EntityRenderer<LaTeXEntity> {

    private DynamicTexture texture;
    protected LaTeXRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(LaTeXEntity entity) {
        return id("textures/entity/latex.png");
    }

    @Override
    public void render(LaTeXEntity latexEntity, float yaw, float partialTick, PoseStack ps, MultiBufferSource multiBufferSource, int packedLight) {
        String LaTeX = latexEntity.getLaTeX();
        TeXFormula formula = new TeXFormula(LaTeX);
        BufferedImage image = (BufferedImage) formula.createBufferedImage(TeXFormula.SANSSERIF, 10, Color.BLACK, null);
        DynamicTexture tex = new DynamicTexture(new NativeImage(image.getWidth(), image.getHeight(), false));
        tex.
    }

}
