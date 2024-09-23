package io.github.hanhuoer.maa.ptr;

/**
 * @author H
 */
public class MaaRecoId extends MaaId {

    public MaaRecoId() {
        super();
    }

    public MaaRecoId(long value) {
        super(value);
    }

    public static MaaRecoId valueOf(long value) {
        return new MaaRecoId(value);
    }

}
