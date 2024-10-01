package io.github.hanhuoer.spring.boot;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.MaaOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import java.util.Objects;

@EnableConfigurationProperties(MaaProperties.class)
@Slf4j
public class MaaAutoConfiguration {

    @Bean(name = "maa")
    @ConditionalOnMissingBean(Maa.class)
    public Maa maa(MaaProperties properties) {
        MaaOptions options = toOptions(properties);
        log.debug("Maa options: {}", options);
        return Maa.create(options);
    }

    private MaaOptions toOptions(MaaProperties properties) {
        MaaOptions options = new MaaOptions();
        if (properties == null) {
            return options;
        }

        if (StringUtils.hasText(properties.getAgentDir())) {
            options.setAgentDir(properties.getAgentDir());
        }
        if (StringUtils.hasText(properties.getLibDir())) {
            options.setLibDir(properties.getLibDir());
        }
        if (StringUtils.hasText(properties.getLogDir())) {
            options.setLogDir(properties.getLogDir());
        }

        if (Objects.nonNull(properties.getSaveDraw())) {
            options.setSaveDraw(properties.getSaveDraw());
        }
        if (Objects.nonNull(properties.getRecording())) {
            options.setRecording(properties.getRecording());
        }
        if (Objects.nonNull(properties.getShowHitDraw())) {
            options.setShowHitDraw(properties.getShowHitDraw());
        }
        if (Objects.nonNull(properties.getDebugMessage())) {
            options.setDebugMessage(properties.getDebugMessage());
        }
        if (Objects.nonNull(properties.getKillAdbOnShutdown())) {
            options.setKillAdbOnShutdown(properties.getKillAdbOnShutdown());
        }

        if (Objects.nonNull(properties.getStdoutLevel())) {
            options.setStdoutLevel(properties.getStdoutLevel());
        }

        return options;
    }

}
