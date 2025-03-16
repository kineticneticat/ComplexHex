package dev.kineticcat.complexhex.casting.assemblies.complex;

import dev.kineticcat.complexhex.entity.AssemblyManagerEntity;
import net.minecraft.server.level.ServerLevel;

public class AssemblyComplex {
    public Boolean begin(AssemblyManagerEntity manager, ServerLevel level) {return false;}
    public Boolean end() {return false;}
    public Boolean tick() {return false;}
}
