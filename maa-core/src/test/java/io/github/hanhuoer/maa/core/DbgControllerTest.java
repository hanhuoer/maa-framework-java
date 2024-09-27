package io.github.hanhuoer.maa.core;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.consts.MaaDbgControllerTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class DbgControllerTest {

    private Maa maa;

    @BeforeEach
    void setUp() {
        maa = Maa.create();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void test() {
        DbgController dbgController = new DbgController(null, null,
                null, MaaDbgControllerTypeEnum.CAROUSELIMAGE,
                null, null
        );
        log.info("debug controller: {}", dbgController);
    }
}