package io.github.hanhuoer.maa.define;

import com.sun.jna.Structure;
import io.github.hanhuoer.maa.callbak.MaaCustomRecognitionCallback;

import java.util.List;

/**
 * struct MaaCustomRecognizerAPI;
 * typedef struct MaaCustomRecognizerAPI* MaaCustomRecognizerHandle;
 *
 * @author H
 */
public class MaaCustomRecognitionHandle extends Structure {

    public MaaCustomRecognitionCallback analyze;

    @Override
    protected List<String> getFieldOrder() {
        return List.of("analyze");
    }

}