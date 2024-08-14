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
            EntityType.Builder.of((EntityType.EntityFactory<AssemblyManagerEntity>) AssemblyManagerEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(32)
                    .updateInterval(1)
                    .build(id("assembly/manager").toString()));

    private static <T extends Entity> EntityType<T> register (String id, EntityType<T> type) {
        var old = ENTITIES.put(id(id), type);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + id);
        }
        return type;
    }
}