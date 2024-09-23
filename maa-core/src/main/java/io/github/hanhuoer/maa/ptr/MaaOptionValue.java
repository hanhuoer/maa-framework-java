package io.github.hanhuoer.maa.ptr;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import lombok.Getter;

/**
 * @author H
 */
@Getter
public class MaaOptionValue extends PointerType {

    private MaaOptionValueSize maaOptionValueSize;
    private long size;

    public MaaOptionValue() {
        super();
    }

    public MaaOptionValue(int intValue) {
        Pointer pointer = new Memory(Native.getNativeSize(int.class));
        pointer.setInt(0, intValue);
        setPointer(pointer);
        this.maaOptionValueSize = new MaaOptionValueSize(Native.getNativeSize(int.class));
        this.size = Native.getNativeSize(int.class);
    }

    public MaaOptionValue(boolean boolValue) {
        Pointer pointer = new Memory(Native.BOOL_SIZE);
        pointer.setByte(0, (byte) (boolValue ? 1 : 0));
        setPointer(pointer);
        this.maaOptionValueSize = new MaaOptionValueSize(Native.BOOL_SIZE);
        this.size = Native.BOOL_SIZE;
    }

    public MaaOptionValue(byte[] byteArray) {
        Pointer pointer = new Memory(byteArray.length);
        pointer.write(0, byteArray, 0, byteArray.length);
        setPointer(pointer);
        this.maaOptionValueSize = new MaaOptionValueSize(byteArray.length);
        this.size = byteArray.length;
    }

    public MaaOptionValue(String stringValue) {
        Memory memory = new Memory(stringValue.length() + 1);
        memory.setString(0, stringValue);
        setPointer(memory);
        this.maaOptionValueSize = new MaaOptionValueSize(stringValue.length());
        this.size = stringValue.length();
    }

}
