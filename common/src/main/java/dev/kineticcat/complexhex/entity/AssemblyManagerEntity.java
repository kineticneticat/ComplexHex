package dev.kineticcat.complexhex.entity;

import at.petrak.hexcasting.api.utils.NBTHelper;
import dev.kineticcat.complexhex.Complexhex;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class AssemblyManagerEntity extends Entity {

    private final String TAG_NODES = "Nodes";

    private List<Vec3> Nodes = new ArrayList<Vec3>();

    public AssemblyManagerEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void addNode(Vec3 vec) {
        Complexhex.LOGGER.info(vec);
        Nodes.add(vec);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        Complexhex.LOGGER.info(compoundTag);
        ListTag NodesTag = NBTHelper.getList(compoundTag, TAG_NODES, Tag.TAG_LIST);
        NodesTag = NodesTag == null ? new ListTag() : NodesTag;
        List<Vec3> out = new ArrayList<>();
        for (Tag tag : NodesTag) {
            CompoundTag ctag = NBTHelper.getAsCompound(tag);
            out.add(new Vec3(
                    ctag.getDouble("X"),
                    ctag.getDouble("Y"),
                    ctag.getDouble("Z")
            ));
        }
        Nodes = out;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        ListTag NodesTag = new ListTag();
        for (Vec3 vec : Nodes) {
            CompoundTag ctag = new CompoundTag();
            ctag.putDouble("X", vec.x);
            ctag.putDouble("Y", vec.y);
            ctag.putDouble("Z", vec.z);
            NodesTag.add(ctag);
        }
        compoundTag.put(TAG_NODES, NodesTag);
        Complexhex.LOGGER.info(compoundTag);

    }
}
