package io.github.hanhuoer.maa.define;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.NativeLongByReference;

/**
 * @author H
 */
public class MaaSize extends NativeLong {

    public MaaSize() {
        super();
    }

    public MaaSize(long value) {
        super(value);
    }

    public static MaaSize valueOf(long value) {
        return new MaaSize(value);
    }

    public long getValue() {
        return this.longValue();
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
