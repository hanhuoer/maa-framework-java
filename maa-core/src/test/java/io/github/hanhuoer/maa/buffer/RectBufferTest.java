package io.github.hanhuoer.maa.buffer;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.model.Rect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class RectBufferTest {

    Maa maa;

    @BeforeEach
    void setUp() {
        maa = Maa.create();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCase() {
        RectBuffer rectBuffer = new RectBuffer();
        Assertions.assertTrue(rectBuffer.setValue(List.of(0, 0, 100, 100)), "Failed.");

        Rect value = rectBuffer.getValue();
        Assertions.assertEquals(value.getX(), 0);
        Assertions.assertEquals(value.getY(), 0);
        Assertions.assertEquals(value.getW(), 100);
        Assertions.assertEquals(value.getH(), 100);

        rectBuffer.close();
        Assertions.assertTrue(rectBuffer.isClosed());

        Assertions.assertNull(rectBuffer.getValue());
    }
}