package dev.kineticcat.complexhex.client;

import at.petrak.hexcasting.client.render.GaslightingTracker;
import at.petrak.hexcasting.xplat.IClientXplatAbstractions;
import dev.kineticcat.complexhex.item.ComplexHexItems;
import net.minecraft.world.item.Item;

public class RegisterClientStuff {
    public static void init() {
        registerGaslight3(ComplexHexItems.BURNT_AMETHYST);
    }
    private static void registerGaslight3(Item item) {
        IClientXplatAbstractions.INSTANCE.registerItemProperty(item,
                GaslightingTracker.GASLIGHTING_PRED, (stack, level, holder, holderID) ->
                        Math.abs(GaslightingTracker.getGaslightingAmount() % 3));
    }
}
