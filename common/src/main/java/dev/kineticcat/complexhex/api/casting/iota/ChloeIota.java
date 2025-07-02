//package dev.kineticcat.complexhex.api.casting.iota;
//
//import at.petrak.hexcasting.api.casting.iota.Iota;
//import at.petrak.hexcasting.api.casting.iota.IotaType;
//import dev.kineticcat.complexhex.Complexhex;
//import kotlin.Pair;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.server.level.ServerLevel;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Objects;
//
//public class ChloeIota extends Iota {
//
//    private static final String ID_TAG = "Id";
//    private static final String STATE_TAG = "State";
//    public ChloeIota(String id, State state) {
//        super(ComplexHexIotaTypes.CHLOE, new Pair<>(id, state));
//    }
//
//    @SuppressWarnings("unchecked") // shut up
//    public String id() {return ((Pair<String, State>) payload).component1();}
//    @SuppressWarnings("unchecked") // shut up 2: electric boogaloo
//    public State state() {return ((Pair<String, State>) payload).component2();}
//
//    @Override
//    public boolean isTruthy() {
//        return true;
//    }
//
//    @Override
//    protected boolean toleratesOther(Iota that) {
//        return typesMatch(this, that)
//                && that instanceof ChloeIota ichlota
//                && Objects.equals(this.id(), ichlota.id());
//    }
//
//    @Override
//    @NotNull
//    public Tag serialize() {
//        CompoundTag ctag = new CompoundTag();
//        ctag.putString(ID_TAG, id());
//        Complexhex.LOGGER.info(state());
//        if (state() == State.STATIC) {
//            ctag.putString(STATE_TAG, State.DEAD.name);
//        } else if (state() == State.INTERMEDIATE1) {
//            ctag.putString(STATE_TAG, State.INTERMEDIATE2.name);
//        } else if (state() == State.INTERMEDIATE2) {
//            ctag.putString(STATE_TAG, State.STATIC.name);
//        } else {
//            ctag.putString(STATE_TAG, state().name);
//        }
//        return ctag;
//    }
//    public static ChloeIota deserialise(Tag tag) {
//        CompoundTag ctag = (CompoundTag) tag;
//        State state = State.valueOf(ctag.getString(STATE_TAG));
//        String id = ctag.getString(ID_TAG);
//        return new ChloeIota(id, state);
//    }
//
//    public static IotaType<ChloeIota> TYPE = new IotaType<>() {
//        @Override
//        public ChloeIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
//            return deserialise(tag);
//        }
//
//        @Override
//        public Component display(Tag tag) {
//            return ChloeIota.display(deserialise(tag));
//        }
//
//        @Override
//        public int color() {
//            return 0;
//        }
//    };
//
//    //what the fuck is this
//    public enum State {
//        MASTER("MASTER"),
//        INTERMEDIATE1("INTERMEDIATE1"),
//        INTERMEDIATE2("INTERMEDIATE2"),
//        STATIC("STATIC"),
//        DEAD("DEAD");
//        public final String name;
//        State(String name) {
//            this.name = name;
//        }
//    }
//
//    public static Component display(ChloeIota ichlota) {
//        return switch ( ichlota.state() ) {
//            case MASTER -> Component.literal("Master: " + ichlota.id());
//            case INTERMEDIATE1, INTERMEDIATE2 -> Component.literal("Inter: " + ichlota.id());
//            case STATIC -> Component.literal("Copy: " + ichlota.id());
//            case DEAD -> Component.literal("Dead: " + ichlota.id());
//        };
//    }
//}
