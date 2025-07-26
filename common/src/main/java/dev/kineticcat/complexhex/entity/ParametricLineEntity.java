package dev.kineticcat.complexhex.entity;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import dev.kineticcat.complexhex.api.util.Expr;
import dev.kineticcat.complexhex.api.util.Symbol;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class ParametricLineEntity extends Entity {
    private static final EntityDataAccessor<CompoundTag> XPRESSION =
            SynchedEntityData.defineId(ParametricLineEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<CompoundTag> YPRESSION =
            SynchedEntityData.defineId(ParametricLineEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<CompoundTag> ZPRESSION =
            SynchedEntityData.defineId(ParametricLineEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<CompoundTag> PIGMENT =
            SynchedEntityData.defineId(ParametricLineEntity.class, EntityDataSerializers.COMPOUND_TAG);
    public static final String XPR_TAG = "Xpression";
    public static final String YPR_TAG = "Ypression";
    public static final String ZPR_TAG = "Zpression";
    public static final String PIGMENT_TAG = "Pigment";

    public ParametricLineEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public Expr getXpr() { return Expr.deserialise(entityData.get(XPRESSION));}
    public Expr getYpr() { return Expr.deserialise(entityData.get(YPRESSION));}
    public Expr getZpr() { return Expr.deserialise(entityData.get(ZPRESSION));}
    public void setXpr(Expr expr) {entityData.set(XPRESSION, expr.serialise());}
    public void setYpr(Expr expr) {entityData.set(YPRESSION, expr.serialise());}
    public void setZpr(Expr expr) {entityData.set(ZPRESSION, expr.serialise());}


    public FrozenPigment getPigment() {return FrozenPigment.fromNBT(entityData.get(PIGMENT));}
    public void setPigment(FrozenPigment pigment) {entityData.set(PIGMENT, pigment.serializeToNBT());}

    @Override
    protected void defineSynchedData() {
        entityData.define(XPRESSION, Symbol.T.serialise());
        entityData.define(YPRESSION, Symbol.T.serialise());
        entityData.define(ZPRESSION, Symbol.T.serialise());
        entityData.define(PIGMENT, FrozenPigment.DEFAULT.get().serializeToNBT());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        entityData.set(XPRESSION, compoundTag.getCompound(XPR_TAG));
        entityData.set(YPRESSION, compoundTag.getCompound(YPR_TAG));
        entityData.set(ZPRESSION, compoundTag.getCompound(ZPR_TAG));
        entityData.set(PIGMENT, compoundTag.getCompound(PIGMENT_TAG));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.put(XPR_TAG, entityData.get(XPRESSION));
        compoundTag.put(YPR_TAG, entityData.get(YPRESSION));
        compoundTag.put(ZPR_TAG, entityData.get(ZPRESSION));
        compoundTag.put(PIGMENT_TAG, entityData.get(PIGMENT));
    }
}