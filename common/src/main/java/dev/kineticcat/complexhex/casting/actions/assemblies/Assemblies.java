package dev.kineticcat.complexhex.casting.actions.assemblies;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.kineticcat.complexhex.Complexhex.id;

@SuppressWarnings("unused")
public class Assemblies {

    public static Map<ResourceLocation, AssemblyController> ASSEMBLIES = new HashMap<>();

    public static AssemblyController CUBE = assembly("cube", AssemblyController.Companion.simple(6, 3));
    public static AssemblyController TRIANGLE = assembly("triangle", AssemblyController.Companion.simple(3, 2));

    private static AssemblyController assembly(String name, AssemblyController controller) {
        ASSEMBLIES.put(id(name), controller);
        return controller;
    }

    public static AssemblyController getController(ResourceLocation resLoc) {
        return ASSEMBLIES.get(resLoc);
    }
    public static AssemblyController getController(String name) {
        return ASSEMBLIES.get(new ResourceLocation(name));
    }
    public static String findController(List<Vec3> verts) {
        for (Map.Entry<ResourceLocation, AssemblyController> entry : ASSEMBLIES.entrySet()) {
            if (entry.getValue().getTest().invoke(verts)) {
                return entry.getKey().toString();
            }
        }
        return null;
    }


}
