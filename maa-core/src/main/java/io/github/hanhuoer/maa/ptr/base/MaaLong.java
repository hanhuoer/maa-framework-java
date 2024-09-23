package io.github.hanhuoer.maa.ptr.base;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;

public class MaaLong extends NativeLong {

    public MaaLong() {
        super();
    }

    public MaaLong(long value) {
        super(value);
    }

    public MaaLong(long value, boolean unsigned) {
        super(value, unsigned);
    }

    public static MaaLong valueOf(long value) {
        return new MaaLong(value);
    }

    public static class Reference extends NativeLongByReference {

        public Reference() {
            super();
        }

        public Reference(NativeLong value) {
            super(value);
        }
    }
}
