package io.github.hanhuoer.spring.boot;

import io.github.hanhuoer.maa.Maa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@SpringBootTest(classes = MaaApplication.class)
public class MaaAutoConfigurationTest {

    @Autowired(required = false)
    private Maa maa;

    @Test
    public void testApp() {
        Assertions.assertNull(maa);
    }

}