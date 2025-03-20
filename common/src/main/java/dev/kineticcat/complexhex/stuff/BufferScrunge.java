package dev.kineticcat.complexhex.stuff;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class BufferScrunge {
    public static int scrungebuf(ByteBuffer buf) {
        return    BufferScrunge.stupid(buf.get())<<24
                | BufferScrunge.stupid(buf.get())<<16
                | BufferScrunge.stupid(buf.get())<<8
                | BufferScrunge.stupid(buf.get());
    }
    public static ByteBuffer unscrungebuf(int num) {
        return ByteBuffer.wrap(new byte[]{
                (byte) dumbass(0b11111111 & num >> 24),
                (byte) dumbass(0b11111111 & num >> 16),
                (byte) dumbass(0b11111111 & num >> 8 ),
                (byte) dumbass(0b11111111 & num      )
        });
    }
    public static int stupid(int a) {return a<0 ? a+256 : a;}
    public static int dumbass(int a) {return a>128 ? a-256 : a;}
}
