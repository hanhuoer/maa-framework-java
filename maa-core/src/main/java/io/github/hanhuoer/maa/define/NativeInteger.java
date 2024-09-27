package io.github.hanhuoer.maa.define;

import com.sun.jna.IntegerType;
import com.sun.jna.Native;

public class NativeInteger extends IntegerType {
    /**
     * Size of a native long, in bytes.
     */
    public static final int SIZE = Native.getNativeSize(int.class);
    private static final long serialVersionUID = 1L;

    /**
     * Create a zero-valued NativeInteger.
     */
    public NativeInteger() {
        this(0);
    }

    /**
     * Create a NativeInteger with the given value.
     */
    public NativeInteger(int value) {
        this(value, false);
    }

    /**
     * Create a NativeInteger with the given value, optionally unsigned.
     */
    public NativeInteger(int value, boolean unsigned) {
        super(SIZE, value, unsigned);
    }

    public static NativeInteger valueOf(int value) {
        return new NativeInteger(value);
    }
}