package dev.kineticcat.complexhex.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class HoldoutEntity extends Entity {
    public HoldoutEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    private static final EntityDataAccessor<ListTag> TRAILTVALUES = SynchedEntityData.defineId(
            HoldoutEntity.class,
            AssemblyManagerEntity.LIST_TAG
    );
    private static final  EntityDataAccessor<ListTag> TRAILSOURCES = SynchedEntityData.defineId(
            HoldoutEntity.class,
            AssemblyManagerEntity.LIST_TAG
    );
    private static final  EntityDataAccessor<ListTag> TRAILSTARTDIRECTIONS = SynchedEntityData.defineId(
            HoldoutEntity.class,
            AssemblyManagerEntity.LIST_TAG
    );

    private static final String TAG_TRAILTVAULES = "TrailTValues";
    private static final String TAG_TRAILSOURCES = "TrailSources";
    private static final String TAG_TRAILSTARTDIRECTIONS = "TrailStartDirections";

    private Float[] TrailTValues;
    private Vec3[] TrailSources;
    private Vec3[] TrailStartDirections;

    @Override
    protected void defineSynchedData() {
        entityData.define(TRAILTVALUES, newFloatList());
        entityData.define(TRAILTVALUES, new ListTag());
        entityData.define(TRAILTVALUES, new ListTag());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        ListTag lt = new ListTag();
    }
}
