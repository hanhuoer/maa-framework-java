package io.github.hanhuoer.maa.model.algorithm;

import io.github.hanhuoer.maa.model.Rect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class NeuralNetworkClassifyResult extends AlgorithmResult {

    private Rect box;

    private Double score;

    private Integer classIndex;

    private String label;

}
