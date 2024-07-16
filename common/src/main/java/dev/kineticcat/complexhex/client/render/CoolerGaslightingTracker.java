package dev.kineticcat.complexhex.client.render;

import at.petrak.hexcasting.client.render.GaslightingTracker;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class CoolerGaslightingTracker {
    public static final Map<ResourceLocation, CoolerGaslightingTracker> GASLIGHTS = new LinkedHashMap<>();
    private int GASLIGHTING_AMOUNT = 0;
    private int LOOKING_COOLDOWN_MAX;
    private int LOOKING_COOLDOWN;
    public ResourceLocation GASLIGHTING_PRED;
    public CoolerGaslightingTracker(int maxCooldown, ResourceLocation reLoc) {
        this.LOOKING_COOLDOWN_MAX = maxCooldown;
        this.LOOKING_COOLDOWN = this.LOOKING_COOLDOWN_MAX;
        this.GASLIGHTING_PRED = reLoc;
    }
    public int getGaslightingAmount() {
        LOOKING_COOLDOWN = LOOKING_COOLDOWN_MAX;
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
