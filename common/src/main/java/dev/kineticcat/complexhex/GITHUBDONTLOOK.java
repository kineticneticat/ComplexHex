//package dev.kineticcat.complexhex;
//
//import net.minecraft.world.phys.Vec3;
//
//import java.util.function.BiFunction;
//
//public class GITHUBDONTLOOK {
//    public static void main(String[] args) {
//        Vec3 X = new Vec3(1, 0, 0);
//        Vec3 Y = new Vec3(0, 1, 0);
//        Vec3 Z = new Vec3(0, 0, 1);
//
//        BiFunction<Double, Integer, Vec3> getRingVertex = (Double ringOffset, Integer index) -> X.scale(Math.cos(Math.PI/2*index)).add(Z.scale(Math.PI/2*index)).scale(2).add(Y.scale(ringOffset));
//
//        System.out.println(getRingVertex.apply(1.0, 0));
//        System.out.println(X.scale(Math.cos(Math.PI/2*0)));
//        System.out.println(Z.scale(Math.PI/2*0));
//        System.out.println(getRingVertex.apply(1.0, 0));
//    }
//}
