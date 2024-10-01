package io.github.hanhuoer.spring.boot;

import io.github.hanhuoer.maa.consts.MaaLoggingLevelEunm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = MaaProperties.PREFIX)
public class MaaProperties {

    public static final String PREFIX = "maa";

    private String agentDir = "resources/maa/agent";
    private String libDir = "resources/maa/lib";
    private String logDir = "resources/maa/debug";
    private MaaLoggingLevelEunm stdoutLevel = MaaLoggingLevelEunm.INFO;
    private Boolean saveDraw = false;
    private Boolean recording = false;
    private Boolean showHitDraw = false;
    private Boolean debugMessage = false;
    private Boolean killAdbOnShutdown = true;

}
