package dev.kineticcat.complexhex;

import dev.kineticcat.complexhex.api.casting.iota.ComplexHexIotaTypes;
import dev.kineticcat.complexhex.casting.ComplexHexSpecialHandlers;
import dev.kineticcat.complexhex.casting.ComplexhexPatternRegistry;
import dev.kineticcat.complexhex.casting.actions.OpASCIIValue;
import dev.kineticcat.complexhex.casting.arithmetic.ComplexHexArithmetic;
import dev.kineticcat.complexhex.stuff.BufferScrunge;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * This is effectively the loading entrypoint for most of your code, at least
 * if you are using Architectury as intended.
 */
public class Complexhex {
    public static final String MOD_ID = "complexhex";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


    public static void init() {
        LOGGER.info("Complex Hex says 'ough'");

        ComplexhexAbstractions.initPlatformSpecific();
        ComplexHexIotaTypes.init();
        ComplexhexPatternRegistry.init();
        ComplexHexArithmetic.init();
        ComplexHexSpecialHandlers.init();

        LOGGER.info(ComplexhexAbstractions.getConfigDirectory().toAbsolutePath().normalize().toString());
    }

    /**
     * Shortcut for identifiers specific to this mod.
     */
    public static ResourceLocation id(String string) {
        return new ResourceLocation(MOD_ID, string);
    }


    //testing
    public static void main(String[] args) {
        String str = "£";
        // kotlin did it, so it's probably fine :clueless:
        Charset UTF32 = Charset.forName("UTF-32");
        ByteBuffer buf = UTF32.encode(str);
//        int num = BufferScrunge.scrungebuf(buf);
//        System.out.println(num);
//        ByteBuffer buf2 = BufferScrunge.unscrungebuf(num);
//        System.out.println(UTF32.decode(buf2));
////        System.out.println(buf2.get());
////        System.out.println(buf2.get());
////        System.out.println(buf2.get());
////        System.out.println(buf2.get());
//        System.out.println(UTF32.decode(UTF32.encode("£")));
        UTF32.
    }
}
