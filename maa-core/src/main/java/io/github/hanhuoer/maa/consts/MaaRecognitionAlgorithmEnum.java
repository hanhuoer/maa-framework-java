package io.github.hanhuoer.maa.consts;

import io.github.hanhuoer.maa.model.algorithm.*;
import lombok.Getter;

/**
 * Recognition algorithm type.
 * default is `DirectHit`.
 *
 * @author H
 */
@Getter
public enum MaaRecognitionAlgorithmEnum {

    DIRECT_HIT("DirectHit", null),
    TEMPLATE_MATCH("TemplateMatch", TemplateMatchResult.class),
    FEATURE_MATCH("FeatureMatch", FeatureMatchResult.class),
    COLOR_MATCH("ColorMatch", ColorMatchResult.class),
    OCR("OCR", OCRResult.class),
    NEURAL_NETWORK_CLASSIFY("NeuralNetworkClassify", NeuralNetworkClassifyResult.class),
    NEURAL_NETWORK_DETECT("NeuralNetworkDetect", NeuralNetworkDetectResult.class),
    CUSTOM("Custom", CustomRecognitionResult.class),

    DEFAULT("DirectHit", MaaRecognitionAlgorithmEnum.DIRECT_HIT.type),
    ;

    private final String value;
    private final Class<? extends AlgorithmResult> type;

    MaaRecognitionAlgorithmEnum(String value, Class<? extends AlgorithmResult> type) {
        this.value = value;
        this.type = type;
    }

    public static MaaRecognitionAlgorithmEnum of(String algorithm) {
        for (MaaRecognitionAlgorithmEnum item : values()) {
            if (item.value.equals(algorithm)) return item;
        }
        return DEFAULT;
    }

}
