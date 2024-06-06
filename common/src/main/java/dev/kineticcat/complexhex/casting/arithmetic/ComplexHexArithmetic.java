package dev.kineticcat.complexhex.casting.arithmetic;

import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic;
import at.petrak.hexcasting.common.lib.hex.HexArithmetics;
import dev.kineticcat.complexhex.Complexhex;
import dev.kineticcat.complexhex.casting.arithmetic.complex.ComplexArithmetic;
import dev.kineticcat.complexhex.casting.arithmetic.quaternion.QuaternionArithmetic;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

public class ComplexHexArithmetic {
    public static final Logger LOGGER = LogManager.getLogger(Complexhex.MOD_ID);

    private static final Map<ResourceLocation, Arithmetic> ARITHMETICS = new LinkedHashMap<>();

    public static void init() {
        for (Map.Entry<ResourceLocation, Arithmetic> entry : ARITHMETICS.entrySet()) {
            Registry.register(HexArithmetics.REGISTRY, entry.getKey(), entry.getValue());
        }
    }

    public static ComplexArithmetic COMPLEX = make(ComplexArithmetic.INSTANCE.arithName(), ComplexArithmetic.INSTANCE);
    public static QuaternionArithmetic QUATERNION = make(QuaternionArithmetic.INSTANCE.arithName(), QuaternionArithmetic.INSTANCE);

    private static <T extends Arithmetic> T make(String name, T arithmetic) {
        var old = ARITHMETICS.put(new ResourceLocation(Complexhex.MOD_ID, name), arithmetic);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return arithmetic;
    }

}
