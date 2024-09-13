package dev.kineticcat.complexhex.client.render.entity.holdoutrenderingshenannigans;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.kineticcat.complexhex.ComplexhexClient;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL30C;

;


// this whole thing and associated files only exist due to the help of "0x150" on the fabric dc server lmao
public class LaggingMaskFrameBuffer extends RenderTarget {
    private static LaggingMaskFrameBuffer instance;

    private LaggingMaskFrameBuffer(int width, int height) {
        super(false);
        RenderSystem.assertOnRenderThreadOrInit();
        this.resize(width, height, true);
        this.setClearColor(0f, 0f, 0f, 0f);
    }

    private static LaggingMaskFrameBuffer obtain() {
        if (instance == null) {
            instance = new LaggingMaskFrameBuffer(
                    Minecraft.getInstance().getMainRenderTarget().width,
                    Minecraft.getInstance().getMainRenderTarget().height);
        }
        return instance;
    }

    public static void use(Runnable r) {
        RenderTarget mainBuffer = Minecraft.getInstance().getMainRenderTarget();
        RenderSystem.assertOnRenderThreadOrInit();
        LaggingMaskFrameBuffer buffer = obtain();
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

    public static void draw() {
        RenderTarget mainBuffer = Minecraft.getInstance().getMainRenderTarget();
        LaggingMaskFrameBuffer buffer = obtain();

        ComplexhexClient.mse.setSamplerUniform("Mask", buffer);

        ComplexhexClient.mse.render(Minecraft.getInstance().getDeltaFrameTime());

        buffer.clear(false);

        mainBuffer.bindWrite(false);
    }
}