package io.github.hanhuoer.maa.ptr;

/***
 * @author H
 */
public class MaaTaskId extends MaaId {

    public MaaTaskId() {
        super();
    }

    public MaaTaskId(long value) {
        super(value);
    }

    public static MaaTaskId valueOf(long value) {
        return new MaaTaskId(value);
    }

}
