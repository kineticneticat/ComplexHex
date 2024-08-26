package dev.kineticcat.complexhex.casting.assemblies;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.kineticcat.complexhex.Complexhex.id;

@SuppressWarnings("unused")
public class Assemblies {

    public static Map<ResourceLocation, AbstractAssemblyController> ASSEMBLIES = new HashMap<>();

    public static AbstractAssemblyController CUBE = assembly("cube", new SimpleAssemblyController(8, 3));
    public static AbstractAssemblyController TRIANGLE = assembly("triangle", new SimpleAssemblyController(3, 2));

    private static AbstractAssemblyController assembly(String name, AbstractAssemblyController controller) {
        ASSEMBLIES.put(id(name), controller);
        return controller;
    }

    public static AbstractAssemblyController getController(ResourceLocation resLoc) {
        return ASSEMBLIES.get(resLoc);
    }
    public static AbstractAssemblyController getController(String name) {
        return ASSEMBLIES.get(new ResourceLocation(name));
    }
    public static String findController(List<Vec3> verts) {
        for (Map.Entry<ResourceLocation, AbstractAssemblyController> entry : ASSEMBLIES.entrySet()) {
            if (entry.getValue().testVertices(verts)) {
                return entry.getKey().toString();
            }
        }
        return null;
    }


}
