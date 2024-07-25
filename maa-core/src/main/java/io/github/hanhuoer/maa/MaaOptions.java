package io.github.hanhuoer.maa;

import io.github.hanhuoer.maa.consts.MaaLoggingLevelEunm;
import io.github.hanhuoer.maa.util.FileUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author H
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MaaOptions {

    private String agentDir = FileUtils.joinUserDir("resources", "maa", "agent").getAbsolutePath();
    private String libDir = FileUtils.joinUserDir("resources", "maa", "lib").getAbsolutePath();
    private String logDir = FileUtils.joinUserDir("resources", "debug").getAbsolutePath();
    private Boolean saveDraw = Boolean.FALSE;
    private Boolean recording = Boolean.FALSE;
    private MaaLoggingLevelEunm stdoutLevel = MaaLoggingLevelEunm.INFO;
    private Boolean showHitDraw = Boolean.FALSE;
    private Boolean debugMessage = Boolean.FALSE;

}
