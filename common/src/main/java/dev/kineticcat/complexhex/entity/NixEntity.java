package dev.kineticcat.complexhex.entity;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class NixEntity extends Entity {
    public NixEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    private static final String TAG_PIGMENT = "pigment";
    private static final EntityDataAccessor<CompoundTag> PIGMENT =
            SynchedEntityData.defineId(NixEntity.class, EntityDataSerializers.COMPOUND_TAG);
    public FrozenPigment getPigment() {return FrozenPigment.fromNBT(entityData.get(PIGMENT));}
    public void setPigment(FrozenPigment pigment) {entityData.set(PIGMENT, pigment.serializeToNBT());}

    @Override
    protected void defineSynchedData() {
        entityData.define(PIGMENT, FrozenPigment.DEFAULT.get().serializeToNBT());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        entityData.set(PIGMENT, compoundTag.getCompound(TAG_PIGMENT));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.put(TAG_PIGMENT, entityData.get(PIGMENT));
    }
}
