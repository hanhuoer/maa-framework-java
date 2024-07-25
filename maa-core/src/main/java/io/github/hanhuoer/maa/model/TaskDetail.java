package io.github.hanhuoer.maa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author H
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TaskDetail {

    private Long taskId;
    private String entry;
    private List<NodeDetail> nodeDetails;

}
