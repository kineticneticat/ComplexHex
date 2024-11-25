package dev.kineticcat.complexhex.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static dev.kineticcat.complexhex.Complexhex.id;

public class ComplexHexEntities {
    public static void registerEntities (BiConsumer<EntityType<?>, ResourceLocation> r) {
        for (var e : ENTITIES.entrySet()) {
            r.accept(e.getValue(), e.getKey());
        }
    }
    private static final Map<ResourceLocation, EntityType<?>> ENTITIES = new LinkedHashMap<>();

    public static final EntityType<AssemblyManagerEntity> ASSEMBLY_MANAGER  = register(
            "assembly/manager",
            EntityType.Builder.of(AssemblyManagerEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(32)
                    .updateInterval(1)
                    .build(id("assembly/manager").toString())
    );
    public static final EntityType<NixEntity> NIX = register(
            "nix",
            EntityType.Builder.of(NixEntity::new, MobCategory.MISC)
                    .sized(.25f, .25f)
                    .clientTrackingRange(32)
                    .updateInterval(1)
                    .build(id("nix").toString())
    );

    public static final EntityType<HoldoutEntity> HOLDOUT = register(
            "holdout",
            EntityType.Builder.of(HoldoutEntity::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .clientTrackingRange(32)
                    .updateInterval(1)
                    .build(id("holdout").toString())
    );
    public static final EntityType<LaTeXEntity> LATEX = register(
            "latex",
            EntityType.Builder.of(LaTeXEntity::new, MobCategory.MISC)
                    .sized(.5f, .5f)
                    .clientTrackingRange(32)
                    .updateInterval(1)
                    .build(id("latex").toString())

    );
    private static <T extends Entity> EntityType<T> register (String id, EntityType<T> type) {
        var old = ENTITIES.put(id(id), type);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + id);
        }
        return type;
    }
}