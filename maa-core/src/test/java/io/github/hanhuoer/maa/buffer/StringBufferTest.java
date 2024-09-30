package io.github.hanhuoer.maa.buffer;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.jna.MaaFramework;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
class StringBufferTest {

    private Maa maa;

    @BeforeEach
    void setUp() {
        maa = Maa.create();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCase() {
        List<StringBuffer> stringBufferList = new LinkedList<>();
        IntStream.range(0, 10).forEach(i -> {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.setValue((i) + "---" + i);
            stringBufferList.add(stringBuffer);
        });

        StringBuffer first = stringBufferList.get(0);
        String str = MaaFramework.buffer().MaaStringBufferGet(first.getHandle());
        log.info("index 0 value: {}", str);
        first.close();

        StringBuffer sec = stringBufferList.get(1);
        log.info("index 1 value: {}", sec.getValue());
        MaaFramework.buffer().MaaStringBufferDestroy(sec.getHandle());

        StringBuffer thd = stringBufferList.get(2);
        Assertions.assertEquals(thd.getValue(), MaaFramework.buffer().MaaStringBufferGet(thd.getHandle()));

        // [2024-xx-xx xx:xx:xx.xxx][ERR][Px44628][Tx12177][MaaBuffer.cpp][L16][MaaDestroyStringBuffer] handle is null
        MaaFramework.buffer().MaaStringBufferDestroy(null);
    }
}