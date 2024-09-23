package io.github.hanhuoer.maa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author H
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class NodeDetail {

    private Long nodeId;
    private String name;
    private Long times;
    private Boolean completed;
    private RecognitionDetail recognition;

}
