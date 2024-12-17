package dev.kineticcat.complexhex.entity;

import at.petrak.hexcasting.api.utils.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static dev.kineticcat.complexhex.api.util.utilStuffLmao.tagAsVec3s;

public class HoldoutEntity extends Entity {
    public HoldoutEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    static final float TrailDt = 0.01f;

    private static final EntityDataAccessor<ListTag> TRAILTVALUES = SynchedEntityData.defineId(
            HoldoutEntity.class,
            AssemblyManagerEntity.LIST_TAG
    );
    private static final  EntityDataAccessor<ListTag> TRAILCURVES = SynchedEntityData.defineId(
            HoldoutEntity.class,
            AssemblyManagerEntity.LIST_TAG
    );
    private static final  EntityDataAccessor<ListTag> TRAILSTARTDIRECTIONS = SynchedEntityData.defineId(
            HoldoutEntity.class,
            AssemblyManagerEntity.LIST_TAG
    );

    private static final String TAG_TRAILTVALUES = "TrailTValues";
    private static final String TAG_TRAILCURVES = "TrailCurves";
    private static final String TAG_TRAILSTARTDIRECTIONS = "TrailStartDirections";

    public List<Float> getTrailTValues() {return entityData.get(TRAILTVALUES).stream().map(NBTHelper::getAsFloat).toList();}
    public List<Vec3> getTrailCurves() { return tagAsVec3s(entityData.get(TRAILCURVES));}
    public List<Vec3> getTrailStartDirections() { return tagAsVec3s(entityData.get(TRAILSTARTDIRECTIONS)); }

    @Override
    protected void defineSynchedData() {
        entityData.define(TRAILTVALUES, newFloatList());
        entityData.define(TRAILCURVES, new ListTag());
        entityData.define(TRAILSTARTDIRECTIONS, new ListTag());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        entityData.set(TRAILTVALUES, compoundTag.getList(TAG_TRAILTVALUES, Tag.TAG_FLOAT));
        entityData.set(TRAILCURVES, compoundTag.getList(TAG_TRAILCURVES, Tag.TAG_COMPOUND));
        entityData.set(TRAILSTARTDIRECTIONS, compoundTag.getList(TAG_TRAILSTARTDIRECTIONS, Tag.TAG_FLOAT));

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.put(TAG_TRAILTVALUES, entityData.get(TRAILTVALUES));
        compoundTag.put(TAG_TRAILCURVES, entityData.get(TRAILCURVES));
        compoundTag.put(TAG_TRAILSTARTDIRECTIONS, entityData.get(TRAILSTARTDIRECTIONS));
    }

    @Override
    public void tick() {
        super.tick();
//        TrailTValues = TrailTValues.stream().map(x-> x+TrailDt).toList();
    }
}
