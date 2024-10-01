package io.github.hanhuoer.spring.boot;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration(MaaAutoConfiguration.class)
public @interface EnableMaaFramework {
}
