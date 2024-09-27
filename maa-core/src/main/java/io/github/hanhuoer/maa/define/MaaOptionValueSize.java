package io.github.hanhuoer.maa.define;

import io.github.hanhuoer.maa.define.base.MaaLong;
import lombok.Getter;

/**
 * @author H
 */
@Getter
public class MaaOptionValueSize extends MaaLong {

    public MaaOptionValueSize() {
        super();
    }

    public MaaOptionValueSize(long value) {
        super(value);
    }

    public static MaaOptionValueSize valueOf(long value) {
        return new MaaOptionValueSize(value);
    }

    public long getValue() {
        return this.longValue();
    }

}
