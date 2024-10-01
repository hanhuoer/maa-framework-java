package io.github.hanhuoer.spring.boot;

import io.github.hanhuoer.maa.Maa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
@SpringBootTest(classes = MaaApplication.class)
@EnableMaaFramework
public class MaaAutoConfigurationTest2 {

    @Autowired
    private Maa maa;

    @Test
    public void testApp() {
        Assertions.assertNotNull(maa);
        System.out.println("maa: " + maa);
        System.out.println("maa options: " + maa.getOptions());
        System.out.println("maa version: " + maa.getMaaFramework().getUtility().MaaVersion());
    }

}