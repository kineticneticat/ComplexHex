package dev.kineticcat.complexhex.casting.assemblies.complex;

import dev.kineticcat.complexhex.entity.AssemblyManagerEntity;
import dev.kineticcat.complexhex.entity.ComplexHexEntities;
import dev.kineticcat.complexhex.entity.HoldoutEntity;
import net.minecraft.server.level.ServerLevel;

public class HoldoutAssembyComplex extends AssemblyComplex {
    @Override
    public Boolean begin(AssemblyManagerEntity manager, ServerLevel level) {
        HoldoutEntity holdout = new HoldoutEntity(ComplexHexEntities.HOLDOUT, level);
        holdout.setPos(manager.getCentre());
        level.addFreshEntity(holdout);
        return false;
    }
}
