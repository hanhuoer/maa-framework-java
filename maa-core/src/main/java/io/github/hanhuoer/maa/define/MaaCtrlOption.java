package io.github.hanhuoer.maa.define;

import lombok.Getter;

/**
 * @author H
 */
@Getter
public class MaaCtrlOption extends MaaOption {

    public MaaCtrlOption() {
        super();
    }

    public MaaCtrlOption(int value) {
        super(value);
    }

    public static MaaCtrlOption valueOf(int value) {
        return new MaaCtrlOption(value);
    }

}
