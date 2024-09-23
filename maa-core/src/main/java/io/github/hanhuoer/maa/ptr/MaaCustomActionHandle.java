package io.github.hanhuoer.maa.ptr;

import com.sun.jna.Structure;
import io.github.hanhuoer.maa.callbak.MaaCustomActionCallback;

import java.util.List;

/**
 * @author H
 */
public class MaaCustomActionHandle extends Structure {

    public MaaCustomActionCallback.Run action;

    @Override
    protected List<String> getFieldOrder() {
        return List.of("action");
    }

}