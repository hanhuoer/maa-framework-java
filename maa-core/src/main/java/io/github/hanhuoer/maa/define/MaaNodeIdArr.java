package io.github.hanhuoer.maa.define;

/**
 * @author H
 */
public class MaaNodeIdArr extends LongArrayByReference {

    public MaaNodeIdArr() {
        this(0L);
    }

    public MaaNodeIdArr(int count) {
        super(count);
    }

    public MaaNodeIdArr(long value) {
        super(value);
    }
}
