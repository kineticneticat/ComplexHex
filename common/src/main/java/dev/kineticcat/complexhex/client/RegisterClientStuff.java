package dev.kineticcat.complexhex.client;

import at.petrak.hexcasting.client.render.GaslightingTracker;
import at.petrak.hexcasting.xplat.IClientXplatAbstractions;
import dev.kineticcat.complexhex.client.render.CoolerGaslightingTracker;
import dev.kineticcat.complexhex.item.ComplexHexItems;
import net.minecraft.world.item.Item;

import java.util.Objects;

import static dev.kineticcat.complexhex.Complexhex.id;

public class RegisterClientStuff {
    public static void init() {
        registerGaslight3(ComplexHexItems.BURNT_AMETHYST_SHARD);
    }
    private static void registerGaslight3(Item item) {
        IClientXplatAbstractions.INSTANCE.registerItemProperty(item,
                Objects.requireNonNull(CoolerGaslightingTracker.GASLIGHTS.get(id("burnt_variants"))).GASLIGHTING_PRED,
                (stack, level, holder, holderID) ->
                        Math.abs(GaslightingTracker.getGaslightingAmount() % 3));
    }
}
