package dev.kineticcat.complexhex.client.render;

import at.petrak.hexcasting.client.render.GaslightingTracker;
import dev.kineticcat.complexhex.Complexhex;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class CoolerGaslightingTracker {
    public static Map<ResourceLocation, CoolerGaslightingTracker> GASLIGHTS = new LinkedHashMap<>();
    public static CoolerGaslightingTracker getTracker(ResourceLocation resLoc) {
        return GASLIGHTS.get(resLoc);
    }
    private int GASLIGHTING_AMOUNT = 0;
    private int LOOKING_COOLDOWN_MAX;
    private int LOOKING_COOLDOWN;
    public ResourceLocation GASLIGHTING_PRED;
    public CoolerGaslightingTracker(ResourceLocation reLoc, int maxCooldown) {
        this.LOOKING_COOLDOWN_MAX = maxCooldown;
        this.LOOKING_COOLDOWN = this.LOOKING_COOLDOWN_MAX;
        this.GASLIGHTING_PRED = reLoc;
        GASLIGHTS.put(reLoc, this);
    }
    public int getGaslightingAmount() {
        LOOKING_COOLDOWN = LOOKING_COOLDOWN_MAX;
//        Complexhex.LOGGER.info(GASLIGHTING_AMOUNT % 3);
        return GASLIGHTING_AMOUNT;
    }
    public void postFrameCheckRendered() {
        if (LOOKING_COOLDOWN > 0) {
            LOOKING_COOLDOWN -= 1;
        } else {
            GASLIGHTING_AMOUNT += 1;
        }
    }

}
