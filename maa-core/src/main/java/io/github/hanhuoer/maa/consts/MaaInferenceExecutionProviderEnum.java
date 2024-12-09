package io.github.hanhuoer.maa.consts;

import io.github.hanhuoer.maa.define.MaaResOption;
import lombok.Getter;

/**
 * @author H
 */
@Getter
public enum MaaInferenceExecutionProviderEnum {
    AUTO(0),
    CPU(1),
    DIRECT_ML(2),
    CORE_ML(3),
    CUDA(4),
    ;

    private final int value;

    MaaInferenceExecutionProviderEnum(int value) {
        this.value = value;
    }

    public MaaResOption value() {
        return new MaaResOption(this.value);
    }

}
