package dev.kineticcat.complexhex.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class LaTeXEntity extends Entity {
    public LaTeXEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    private static final EntityDataAccessor<String> LATEX =
            SynchedEntityData.defineId(LaTeXEntity.class, EntityDataSerializers.STRING);
    private static final String LATEX_TAG = "LaTeX";
    public String getLaTeX() {return entityData.get(LATEX);}

    @Override
    protected void defineSynchedData() {
        entityData.define(LATEX, "");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        entityData.set(LATEX, compoundTag.getString(LATEX_TAG));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putString(entityData.get(LATEX), LATEX_TAG);
    }
}
