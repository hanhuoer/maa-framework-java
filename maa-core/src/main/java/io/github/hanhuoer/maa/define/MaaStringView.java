package io.github.hanhuoer.maa.define;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import lombok.Getter;

/**
 * @author H
 */
@Getter
public class MaaStringView extends PointerType {

    private String value;

    public MaaStringView() {
        super(Pointer.NULL);
    }

    public void setValue(String value) {
        this.value = value;
        if (value != null) {
            byte[] data = Native.toByteArray(value);
            setPointer(new Memory(data.length + 1));
            getPointer().write(0, data, 0, data.length);
            getPointer().setByte(data.length, (byte) 0);
        } else {
            setPointer(Pointer.NULL);
        }
    }

    public String getValue() {
        if (getPointer() == Pointer.NULL) {
            return null;
        }
        return getPointer().getString(0);
    }

    @Override
    public String toString() {
        return getValue();
    }
}