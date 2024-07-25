package io.github.hanhuoer.maa.ptr;

import com.sun.jna.ptr.LongByReference;

/**
 * @author H
 */
public class MaaSize extends LongByReference {

    public MaaSize() {
        super();
    }

    public MaaSize(long value) {
        super(value);
    }

    public static void main(String[] args) {
        MaaSize maaSize = new MaaSize();
        System.out.println(maaSize.getValue());
        System.out.println(new MaaSize(10L).getValue());
    }
}
