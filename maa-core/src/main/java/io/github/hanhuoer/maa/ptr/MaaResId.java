package io.github.hanhuoer.maa.ptr;

/***
 * @author H
 */
public class MaaResId extends MaaId {

    public MaaResId() {
        super();
    }

    public MaaResId(long value) {
        super(value);
    }

    public static MaaResId valueOf(long maaId) {
        return new MaaResId(maaId);
    }
}
