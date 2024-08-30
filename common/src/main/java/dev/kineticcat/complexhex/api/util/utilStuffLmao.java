package dev.kineticcat.complexhex.api.util;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation;
import at.petrak.hexcasting.api.utils.NBTHelper;
import dev.kineticcat.complexhex.entity.AssemblyManagerEntity;
import dev.kineticcat.complexhex.entity.ComplexHexEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;

public class utilStuffLmao {

    public static CompoundTag vec3AsTag(Vec3 vec) {
        CompoundTag ctag = new CompoundTag();
        ctag.putDouble("X", vec.x);
        ctag.putDouble("Y", vec.y);
        ctag.putDouble("Z", vec.z);
        return ctag;
    }

    public static ListTag vec3sAsTag(List<Vec3> vecs) {
        ListTag ltag = new ListTag();
        for (Vec3 vec : vecs) {
            ltag.add(vec3AsTag(vec));
        }
        return ltag;
    }

    public static Vec3 tagAsVec3(CompoundTag ctag) {
        return new Vec3(
                ctag.getDouble("X"),
                ctag.getDouble("Y"),
                ctag.getDouble("Z")
        );
    }
    public static List<Vec3> tagAsVec3s(ListTag ltag) {
        List<Vec3> out = new ArrayList<>();
        for (Tag tag : ltag) {
            out.add(tagAsVec3(NBTHelper.getAsCompound(tag)));
        }
        return out;
    }

}
