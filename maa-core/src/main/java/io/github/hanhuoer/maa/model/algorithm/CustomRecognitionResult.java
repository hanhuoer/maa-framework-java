package io.github.hanhuoer.maa.model.algorithm;

import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.model.Rect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CustomRecognitionResult extends AlgorithmResult {

    private Rect box;

    private JSONObject detail;

    private String rawDetailStr;

}
