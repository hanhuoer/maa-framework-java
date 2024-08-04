package io.github.hanhuoer.maa.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RecognitionResult {

    private boolean success;
    private Rect rect;
    private String detail;

}