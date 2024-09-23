package io.github.hanhuoer.maa.model;

import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.consts.MaaRecognitionAlgorithmEnum;
import io.github.hanhuoer.maa.model.algorithm.AlgorithmResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

/**
 * @author H
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RecognitionDetail {

    private Long recoId;
    private String name;
    List<? extends AlgorithmResult> allResultList;
    private Rect hitBox;
    List<? extends AlgorithmResult> filteredResultList;
    AlgorithmResult bestResult;
    private MaaRecognitionAlgorithmEnum algorithm;
    private JSONObject rawDetailJson;
    private String rawDetail;
    private BufferedImage raw;
    private List<BufferedImage> draws;


    public void parseRecognitionRawDetail() {
        JSONObject rawDetail = JSONObject.parseObject(this.rawDetail);
        this.rawDetailJson = rawDetail;

        this.allResultList = Collections.emptyList();
        this.filteredResultList = Collections.emptyList();
        if (rawDetail == null || rawDetail.isEmpty()) {
            return;
        }

        Class<? extends AlgorithmResult> algorithmType = algorithm.getType();
        if (algorithmType == null) {
            return;
        }

        this.allResultList = rawDetail.getList("all", algorithmType);
        this.filteredResultList = rawDetail.getList("filtered", algorithmType);
        this.bestResult = rawDetail.getObject("best", algorithmType);
    }

}
