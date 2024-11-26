package dev.kineticcat.complexhex.entity;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import dev.kineticcat.complexhex.Complexhex;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import static dev.kineticcat.complexhex.api.util.utilStuffLmao.tagAsVec3;
import static dev.kineticcat.complexhex.api.util.utilStuffLmao.vec3AsTag;

public class NixEntity extends Entity {
    public NixEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        noPhysics = false;
    }

    private static final double dt = 0.01;

    private static final String TAG_PIGMENT = "pigment";
    private static final String TAG_ACCELERATION = "acceleration";
    private static final String TAG_AGE = "age";
    private static final EntityDataAccessor<CompoundTag> PIGMENT =
            SynchedEntityData.defineId(NixEntity.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<Vector3f> ACCELERATION =
            SynchedEntityData.defineId(NixEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Integer> AGE =
            SynchedEntityData.defineId(NixEntity.class, EntityDataSerializers.INT);

    public FrozenPigment getPigment() {return FrozenPigment.fromNBT(entityData.get(PIGMENT));}
    public void setPigment(FrozenPigment pigment) {entityData.set(PIGMENT, pigment.serializeToNBT());}
    public Vec3 getAcceleration() {return new Vec3(entityData.get(ACCELERATION));}
    public void setAcceleration(Vec3 acc) {entityData.set(ACCELERATION, acc.toVector3f());}

    public Integer getAge() {return entityData.get(AGE);}
    public void setAge(Integer age) {entityData.set(AGE, age);}

    @Override
    protected void defineSynchedData() {
        entityData.define(PIGMENT, FrozenPigment.DEFAULT.get().serializeToNBT());
        entityData.define(ACCELERATION, Vec3.ZERO.toVector3f());
        entityData.define(AGE, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        entityData.set(PIGMENT, compoundTag.getCompound(TAG_PIGMENT));
        entityData.set(ACCELERATION, tagAsVec3(compoundTag.getCompound(TAG_ACCELERATION)).toVector3f());
        entityData.set(AGE, compoundTag.getInt(TAG_AGE));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.put(TAG_PIGMENT, entityData.get(PIGMENT));
        compoundTag.put(TAG_ACCELERATION, vec3AsTag(new Vec3(entityData.get(ACCELERATION))));
        compoundTag.putInt(TAG_AGE, entityData.get(AGE));
    }

    @Override
    public void tick() {
//        age();
        harm();
        move();
    }

    public void age() {
        Integer current = getAge();
        // so you can have it sit there untill you use it
        if (getAcceleration().lengthSqr() != 0) {setAge(current+1);}
        if (current+1 > 300) {
            this.kill();
        }
    }

    public void harm() {
//        HitResult hitResult = level().clip(new ClipContext(this.position(), this.getDeltaMovement(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this ));
//        Complexhex.LOGGER.info(hitResult);
//        if (hitResult.getType() != HitResult.Type.ENTITY) {
//            return;
//        }
//        // due to above, it must be an entity therefore shut up
//        //noinspection DataFlowIssue
//        EntityHitResult ehr = (EntityHitResult) hitResult;

        EntityHitResult ehr = ProjectileUtil.getEntityHitResult(
                this.level(),
                this,
                this.position(),
                this.position().add(this.getDeltaMovement().scale(dt)),
                this.getBoundingBox().expandTowards(this.getDeltaMovement().scale(dt)).inflate(1.0),
                Entity::canBeHitByProjectile);
        if (ehr == null) return;
        Entity hit = ehr.getEntity();
        Complexhex.LOGGER.info(hit);
        DamageSource damageSource = damageSources().generic();
        float amt = damage();
        Complexhex.LOGGER.info("amt: %f, speed: %f".formatted(amt, this.getDeltaMovement().scale(dt).length()));
        hit.hurt(damageSource, amt);
    }

    public float damage() {
        // 1 second of full acceleration per 2 hearts of damage
        return (float) (getDeltaMovement().length() * 0.1f);
    }

    public void move() {
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
