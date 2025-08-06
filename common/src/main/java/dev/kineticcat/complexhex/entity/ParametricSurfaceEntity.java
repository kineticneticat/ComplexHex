package dev.kineticcat.complexhex.entity;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import dev.kineticcat.complexhex.api.util.Expr;
import dev.kineticcat.complexhex.api.util.Symbol;
import dev.kineticcat.complexhex.api.util.Value;
import dev.kineticcat.complexhex.api.util.Vector;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class ParametricSurfaceEntity extends Entity {
    private static final EntityDataAccessor<CompoundTag> EXPRESSION =
            SynchedEntityData.defineId(ParametricSurfaceEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<CompoundTag> PIGMENT =
            SynchedEntityData.defineId(ParametricSurfaceEntity.class, EntityDataSerializers.COMPOUND_TAG);
    public static final String EXPR_TAG = "Xpression";
    public static final String PIGMENT_TAG = "Pigment";

    public ParametricSurfaceEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public Expr getExpr() { return Expr.deserialise(entityData.get(EXPRESSION));}
    public void setExpr(Expr expr) {entityData.set(EXPRESSION, expr.serialise());}

    public FrozenPigment getPigment() {return FrozenPigment.fromNBT(entityData.get(PIGMENT));}
    public void setPigment(FrozenPigment pigment) {entityData.set(PIGMENT, pigment.serializeToNBT());}

    @Override
    protected void defineSynchedData() {
        entityData.define(EXPRESSION, new Vector(Symbol.U, Symbol.V, new Value(0.0)).serialise());
        entityData.define(PIGMENT, FrozenPigment.DEFAULT.get().serializeToNBT());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        entityData.set(EXPRESSION, compoundTag.getCompound(EXPR_TAG));
        entityData.set(PIGMENT, compoundTag.getCompound(PIGMENT_TAG));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.put(EXPR_TAG, entityData.get(EXPRESSION));
        compoundTag.put(PIGMENT_TAG, entityData.get(PIGMENT));
    }

    @Override
    public boolean shouldRender(double d, double e, double f) {
        return true;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return new AABB(position().add(-10, -10, -10), position().add(10, 10, 10));
    }
}