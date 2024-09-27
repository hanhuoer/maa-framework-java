package io.github.hanhuoer.maa.define;

import com.sun.jna.Structure;
import io.github.hanhuoer.maa.callbak.MaaCustomActionCallback;

import java.util.List;

/**
 * @author H
 */
public class MaaCustomActionHandle extends Structure {

    public MaaCustomActionCallback run;

    @Override
    protected List<String> getFieldOrder() {
        return List.of("run");
    }

}