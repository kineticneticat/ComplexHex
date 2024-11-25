package dev.kineticcat.complexhex.client.render.entity.holdoutrenderingshenannigans;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL30C;

public class LaggingMaskRenderTarget extends RenderTarget {
    private static LaggingMaskRenderTarget instance;
    private final int depth;

    private LaggingMaskRenderTarget(int width, int height, int depth) {
        super(true);
        this.depth = depth;
        RenderSystem.assertOnRenderThreadOrInit();
        this.resize(width, height, true);
        this.setClearColor(0f, 0f, 0f, 0f);
    }

    private void setTexFilter(int filterMode, boolean force) {
        RenderSystem.assertOnRenderThreadOrInit();
        if (force || filterMode != this.filterMode) {
            this.filterMode = filterMode;
            GlStateManager._bindTexture(this.colorTextureId);
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_MIN_FILTER, filterMode);
            GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_MAG_FILTER, filterMode);
            GlStateManager._bindTexture(0);
        }
    }

    @Override
    public void createBuffers(int width, int height, boolean getError) {
        RenderSystem.assertOnRenderThreadOrInit();
        int i = RenderSystem.maxSupportedTextureSize();
        if (width <= 0 || width > i || height <= 0 || height > i) {
            throw new IllegalArgumentException("Window " + width + "x" + height + " size out of bounds (max. size: " + i + ")");
        }
        this.viewWidth = width;
        this.viewHeight = height;
        this.width = width;
        this.height = height;
        this.frameBufferId = GlStateManager.glGenFramebuffers();
        this.colorTextureId = TextureUtil.generateTextureId();
        if (this.useDepth) {
            this.depthBufferId = depth;
        }
        this.setTexFilter(GlConst.GL_NEAREST, true);
        GlStateManager._bindTexture(this.colorTextureId);
        GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_WRAP_S, GlConst.GL_CLAMP_TO_EDGE);
        GlStateManager._texParameter(GlConst.GL_TEXTURE_2D, GlConst.GL_TEXTURE_WRAP_T, GlConst.GL_CLAMP_TO_EDGE);
        GlStateManager._texImage2D(GlConst.GL_TEXTURE_2D, 0, GlConst.GL_RGBA8, this.width, this.height, 0, GlConst.GL_RGBA, GlConst.GL_UNSIGNED_BYTE, null);
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, this.frameBufferId);
        GlStateManager._glFramebufferTexture2D(GlConst.GL_FRAMEBUFFER, GlConst.GL_COLOR_ATTACHMENT0, GlConst.GL_TEXTURE_2D, this.colorTextureId, 0);
        if (this.useDepth) {
            GlStateManager._glFramebufferTexture2D(GlConst.GL_FRAMEBUFFER, GlConst.GL_DEPTH_ATTACHMENT, GlConst.GL_TEXTURE_2D, this.depthBufferId, 0);
        }
        this.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
        this.checkStatus();
        this.clear(getError);
        this.unbindRead();
    }

    @Override
    public void destroyBuffers() {
        RenderSystem.assertOnRenderThreadOrInit();
        this.unbindRead();
        this.unbindWrite();
        this.depthBufferId = -1; // dont free it
        if (this.colorTextureId > -1) {
            TextureUtil.releaseTextureId(this.colorTextureId);
            this.colorTextureId = -1;
        }
        if (this.frameBufferId > -1) {
            GlStateManager._glBindRenderbuffer(GlConst.GL_FRAMEBUFFER, 0);
            GlStateManager._glDeleteRenderbuffers(this.frameBufferId);
            this.frameBufferId = -1;
        }
    }

    public static LaggingMaskRenderTarget obtain() {
        if (instance == null) {
            RenderTarget fb = Minecraft.getInstance().getMainRenderTarget();
            instance = new LaggingMaskRenderTarget(fb.width,
                    fb.height, fb.getDepthTextureId());
        }
        return instance;
    }

    public static void use(Runnable r) {
        RenderTarget mainBuffer = Minecraft.getInstance().getMainRenderTarget();
        RenderSystem.assertOnRenderThreadOrInit();
        LaggingMaskRenderTarget buffer = obtain();
        if (buffer.width != mainBuffer.width || buffer.height != mainBuffer.height) {
            buffer.resize(mainBuffer.width, mainBuffer.height, false);
        }

        GlStateManager._glBindFramebuffer(GL30C.GL_DRAW_FRAMEBUFFER, buffer.frameBufferId);

        buffer.bindWrite(true);
        r.run();
        buffer.unbindWrite();

        GlStateManager._glBindFramebuffer(GL30C.GL_DRAW_FRAMEBUFFER, mainBuffer.frameBufferId);

        mainBuffer.bindWrite(false);
    }

    public static void draw(long time) {
        RenderTarget mainBuffer = Minecraft.getInstance().getMainRenderTarget();
        LaggingMaskRenderTarget buffer = obtain();

//        ComplexhexClient.mse.setSamplerUniform("Mask", buffer);
//        ComplexhexClient.mse.setUniformValue("time", time);
//
//        ComplexhexClient.mse.render(Minecraft.getInstance().getDeltaFrameTime());

        buffer.clear(false);

        mainBuffer.bindWrite(false);
    }
}