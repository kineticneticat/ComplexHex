package dev.kineticcat.complexhex.client.render.entity;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kineticcat.complexhex.entity.LaTeXEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.*;
import java.awt.image.BufferedImage;

import static dev.kineticcat.complexhex.Complexhex.id;

public class LaTeXRenderer extends EntityRenderer<LaTeXEntity> {

//    private DynamicTexture texture;
    protected LaTeXRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(LaTeXEntity entity) {
        return id("textures/entity/latex.png");
    }

    @Override
    public void render(LaTeXEntity latexEntity, float yaw, float partialTick, PoseStack ps, MultiBufferSource multiBufferSource, int packedLight) {
        if (texture == null) {
            String LaTeX = latexEntity.getLaTeX();
            TeXFormula formula = new TeXFormula(LaTeX);
            BufferedImage bufImg = (BufferedImage) formula.createBufferedImage(TeXFormula.SANSSERIF, 10, Color.BLACK, null);
            NativeImage wawa = new NativeImage(bufImg.getWidth(), bufImg.getHeight(), true);
            for (int y = 0; y < bufImg.getHeight(); y++)
                for (int x = 0; x < bufImg.getWidth(); x++)
                    wawa.setPixelRGBA(x, y, bufImg.getRGB(x, y));
            DynamicTexture texture = new DynamicTexture(wawa);
            ResourceLocation resLoc = TextureManager.register("", texture);
        }

    }

}
