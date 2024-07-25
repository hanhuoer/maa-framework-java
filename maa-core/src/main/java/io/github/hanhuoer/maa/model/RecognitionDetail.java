package io.github.hanhuoer.maa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.awt.image.BufferedImage;
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
    private String detail;
    private BufferedImage raw;
    private List<BufferedImage> draws;

}
