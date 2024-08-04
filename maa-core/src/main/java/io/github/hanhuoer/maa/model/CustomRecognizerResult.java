package io.github.hanhuoer.maa.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author H
 */
@Data
@Accessors(chain = true)
public class CustomRecognizerResult {

    private boolean success;
    private Rect box;
    private String detail;

    public CustomRecognizerResult(boolean success, Rect box, String detail) {
        this.success = success;
        this.box = box;
        this.detail = detail;
    }

}