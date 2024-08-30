package dev.kineticcat.complexhex.entity;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import static dev.kineticcat.complexhex.api.util.utilStuffLmao.tagAsVec3;
import static dev.kineticcat.complexhex.api.util.utilStuffLmao.vec3AsTag;

public class NixEntity extends Entity {
    public NixEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        noPhysics = false;
    }

    private static final String TAG_PIGMENT = "pigment";
    private static final String TAG_ACCELERATION = "acceleration";
    private static final EntityDataAccessor<CompoundTag> PIGMENT =
            SynchedEntityData.defineId(NixEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<Vector3f> ACCELERATION =
            SynchedEntityData.defineId(NixEntity.class, EntityDataSerializers.VECTOR3);
    public FrozenPigment getPigment() {return FrozenPigment.fromNBT(entityData.get(PIGMENT));}
    public void setPigment(FrozenPigment pigment) {entityData.set(PIGMENT, pigment.serializeToNBT());}
    public Vec3 getAcceleration() {return new Vec3(entityData.get(ACCELERATION));}
    public void setAcceleration(Vec3 acc) {entityData.set(ACCELERATION, acc.toVector3f());}

    @Override
    protected void defineSynchedData() {
        entityData.define(PIGMENT, FrozenPigment.DEFAULT.get().serializeToNBT());
        entityData.define(ACCELERATION, Vec3.ZERO.toVector3f());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        entityData.set(PIGMENT, compoundTag.getCompound(TAG_PIGMENT));
        entityData.set(ACCELERATION, tagAsVec3(compoundTag.getCompound(TAG_ACCELERATION)).toVector3f());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.put(TAG_PIGMENT, entityData.get(PIGMENT));
        compoundTag.put(TAG_ACCELERATION, vec3AsTag(new Vec3(entityData.get(ACCELERATION))));
    }

    @Override
    public void tick() {
        move(0.01f);

    }
    public void move(double dt) {
        // ds = v*dt + 1/2 * a * dt^2
        setPos(
              position().add(
                      getDeltaMovement().scale(dt)
              ).add(
                     getAcceleration().scale(Math.pow(dt, 2))
              )
        );

        // dv = a*dt
        setDeltaMovement(
                getDeltaMovement().add(
                        getAcceleration().scale(dt)
                )
        );

        lookAt(EntityAnchorArgument.Anchor.EYES, position().add(getDeltaMovement()));
    }
}
