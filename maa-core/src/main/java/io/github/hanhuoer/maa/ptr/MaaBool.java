package io.github.hanhuoer.maa.ptr;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;

/**
 * @author H
 */
public class MaaBool extends ByReference {

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

}