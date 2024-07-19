package dev.kineticcat.complexhex.fabric;

import dev.kineticcat.complexhex.ComplexhexClient;
import dev.kineticcat.complexhex.client.RegisterClientStuff;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType

/**
 * Fabric client loading entrypoint.
 */
object ComplexhexClientFabric : ClientModInitializer {

    override fun onInitializeClient() {
        ComplexhexClient.init();

        RegisterClientStuff.registerBlockEntityRenderers(object :
            RegisterClientStuff.BlockEntityRendererRegisterererer {
            override fun <T : BlockEntity?> registerBlockEntityRenderer(
                type: BlockEntityType<T>?,
                berp: BlockEntityRendererProvider<in T>?
            ) {
                BlockEntityRendererRegistry.register(type, berp)
            }
            }
        );
        ModelLoadingRegistry.INSTANCE.registerModelProvider(RegisterClientStuff::onModelRegister)

    }
}
