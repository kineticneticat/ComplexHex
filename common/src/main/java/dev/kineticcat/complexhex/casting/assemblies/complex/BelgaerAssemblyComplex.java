package dev.kineticcat.complexhex.casting.assemblies.complex;

import dev.kineticcat.complexhex.entity.AssemblyManagerEntity;
import net.minecraft.server.level.ServerLevel;

public class BelgaerAssemblyComplex extends AssemblyComplex {
    @Override
    public Boolean begin(AssemblyManagerEntity manager, ServerLevel level) {

        return false;
    }
}
