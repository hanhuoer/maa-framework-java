package io.github.hanhuoer.maa.ptr.base;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;

/**
 * @author H
 */
public class MaaBool extends ByReference {

    public static final MaaBool TRUE = new MaaBool(true);
    public static final MaaBool FALSE = new MaaBool(false);

    public MaaBool() {
        super(1);
    }

    public MaaBool(boolean value) {
        super(1);
        setValue(value);
    }

    public boolean getValue() {
        return Pointer.nativeValue(getPointer()) != 0;
    }

    public void setValue(boolean value) {
        getPointer().setByte(0, (byte) (value ? 1 : 0));
    }

    @Override
    public String toString() {
        return String.format("MaaBool@0x%1$x=%2$s", Pointer.nativeValue(getPointer()), getValue());
    }

    public static MaaBool valueOf(boolean value) {
        return new MaaBool(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof Boolean) return this.getValue() == (Boolean) o;
        if (!(o instanceof MaaBool)) return false;
        MaaBool other = (MaaBool) o;
        return other.getValue() == this.getValue();
    }
}