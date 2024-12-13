package dev.kineticcat.complexhex.util;

import java.awt.*;
import java.util.Random;
import java.util.UUID;

public class DefinetlyNotACopyOfItemUUIDPigment {
    public static int[] colourPlease(UUID owner) {
        var rand = new Random(owner.getLeastSignificantBits() ^ owner.getMostSignificantBits());
        var hue1 = rand.nextFloat();
        var saturation1 = rand.nextFloat(0.4f, 0.8f);
        var brightness1 = rand.nextFloat(0.7f, 1.0f);
        var hue2 = rand.nextFloat();
        var saturation2 = rand.nextFloat(0.7f, 1.0f);
        var brightness2 = rand.nextFloat(0.2f, 0.7f);

        var col1 = Color.HSBtoRGB(hue1, saturation1, brightness1);
        var col2 = Color.HSBtoRGB(hue2, saturation2, brightness2);
        return new int[]{col1, col2};
    }
}
