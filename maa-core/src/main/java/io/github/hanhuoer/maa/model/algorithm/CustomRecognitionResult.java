package io.github.hanhuoer.maa.model.algorithm;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import io.github.hanhuoer.maa.model.Rect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CustomRecognitionResult extends AlgorithmResult {

    private Rect box;

    private Object detail;

    public static List<AlgorithmResult> parse(JSONArray jsonArray) {
        List<AlgorithmResult> resultList = new ArrayList<>();
        boolean isDetailTypeString = checkDetailTypeIfString(jsonArray);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject itemJson = jsonArray.getJSONObject(i);
            resultList.add(parse(itemJson, isDetailTypeString));
        }

        return resultList;
    }

    public static AlgorithmResult parse(JSONObject jsonObject) {
        if (jsonObject == null) return null;
        return parse(jsonObject, checkDetailTypeIfString(jsonObject));
    }

    public static AlgorithmResult parse(JSONObject jsonObject, boolean isDetailTypeString) {
        Rect box = jsonObject.getObject("box", Rect.class);
        Object detail = null;

        if (isDetailTypeString) {
            // string type
            detail = jsonObject.getString("detail");
        } else {
            // json type
            try {
                detail = jsonObject.getJSONObject("detail");
            } catch (JSONException e) {
                detail = jsonObject.getString("detail");
            }
        }
        CustomRecognitionResult result = new CustomRecognitionResult();
        result.setBox(box);
        result.setDetail(detail);
        result.setRawJson(jsonObject);
        return result;
    }

    /**
     * @param jsonArray json arr.
     * @return true if string.
     */
    private static boolean checkDetailTypeIfString(JSONArray jsonArray) {
        if (jsonArray == null || jsonArray.isEmpty()) {
            return true;
        }

        return checkDetailTypeIfString(jsonArray.getJSONObject(0));
    }

    private static boolean checkDetailTypeIfString(JSONObject jsonObject) {
        try {
            jsonObject.getJSONObject("detail");
            return false;
        } catch (JSONException e) {
            return true;
        }
    }

}
