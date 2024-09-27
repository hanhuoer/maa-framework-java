package io.github.hanhuoer.maa.define;

/**
 * @author H
 */
public class MaaNodeId extends MaaId {

    public MaaNodeId() {
        super();
    }

    public MaaNodeId(long value) {
        super(value);
    }

    public static MaaNodeId valueOf(long value) {
        return new MaaNodeId(value);
    }

}
