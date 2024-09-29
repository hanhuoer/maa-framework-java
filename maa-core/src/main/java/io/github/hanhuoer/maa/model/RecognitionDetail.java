package io.github.hanhuoer.maa.model;

import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.consts.MaaRecognitionAlgorithmEnum;
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
    private Rect hitBox;
    private JSONObject bestResult;
    private List<JSONObject> allResultList;
    private List<JSONObject> filteredResultList;
    private MaaRecognitionAlgorithmEnum algorithm;
    private JSONObject rawDetailJson;
    private String rawDetail;
    private BufferedImage rawImage;
    private List<BufferedImage> drawImages;


    public void parseRecognitionRawDetail() {
        JSONObject rawDetailJson = JSONObject.parseObject(this.rawDetail);
        this.rawDetailJson = rawDetailJson;

        this.allResultList = Collections.emptyList();
        this.filteredResultList = Collections.emptyList();
        if (rawDetailJson == null || rawDetailJson.isEmpty()) {
            return;
        }

        this.allResultList = rawDetailJson.getJSONArray("all").toList(JSONObject.class);
        this.filteredResultList = rawDetailJson.getJSONArray("filtered").toList(JSONObject.class);
        this.bestResult = rawDetailJson.getJSONObject("best");
    }

}
