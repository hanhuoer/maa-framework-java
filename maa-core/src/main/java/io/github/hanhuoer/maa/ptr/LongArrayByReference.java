package io.github.hanhuoer.maa.ptr;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author H
 */
@Getter
public class LongArrayByReference extends ByReference {

    protected static final int DATA_SIZE = Native.getNativeSize(long.class);
    protected int count = 0;


    public LongArrayByReference() {
        this(0L);
    }

    public LongArrayByReference(int count) {
        super(DATA_SIZE * count);
        this.count = count;
    }

    public LongArrayByReference(long value) {
        this(1);
        setValue(value);
    }

    public long getValue() {
        return getPointer().getLong(0);
    }

    public void setValue(long value) {
        getPointer().setLong(0, value);
    }

    public long getValue(int i) {
        return getValues()[i];
    }

    public long[] getValues() {
        int idx = 0;
        long[] result = new long[count];
        for (int i = 0; i < count; i++) {
            result[i] = getPointer().getLong(idx);
            idx += DATA_SIZE;
        }
        return result;
    }

    public void setValues(Long... values) {
        int idx = 0;
        for (Long value : values) {
            getPointer().setLong(idx, value);
            idx += DATA_SIZE;
        }
    }

    public List<Long> getValueList() {
        int idx = 0;
        List<Long> result = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            result.add(getPointer().getLong(idx));
            idx += DATA_SIZE;
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("long@0x%1$x=0x%2$x (%2$d)", Pointer.nativeValue(getPointer()), getValue());
    }
}
