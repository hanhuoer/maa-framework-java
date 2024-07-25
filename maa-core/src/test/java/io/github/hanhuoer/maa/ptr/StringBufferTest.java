package io.github.hanhuoer.maa.ptr;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.jna.MaaFramework;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
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
    void close() {
    }

    @Test
    void getValue() {
    }

    @Test
    void testGetValue() {
    }

    @Test
    void setValue() {
    }

    @Test
    void empty() {
    }

    @Test
    void clear() {
    }

    @Test
    void getHandle() {
    }

    @Test
    void isOwn() {
    }

    @Test
    void closeTest() {
        List<StringBuffer> stringBufferList = new LinkedList<>();
        IntStream.range(0, 10).forEach(i -> {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.setValue((i) + "---" + i);
            stringBufferList.add(stringBuffer);
        });

        StringBuffer first = stringBufferList.get(0);
        String str = MaaFramework.buffer().MaaGetString(first.getHandle());
        log.info("index 0 value: {}", str);

        first.close();
        str = MaaFramework.buffer().MaaGetString(first.getHandle());
        log.info("index 0 value: {}", str);

        StringBuffer sec = stringBufferList.get(1);
        log.info("index 1 value: {}", sec.getValue());
        MaaFramework.buffer().MaaDestroyStringBuffer(sec.getHandle());
        log.info("index 1 value: {}", sec.getValue());
        log.info("index 1 value: {}", MaaFramework.buffer().MaaGetString(sec.getHandle()));

        StringBuffer thd = stringBufferList.get(2);
        log.info("index 2 value: {}", MaaFramework.buffer().MaaGetString(thd.getHandle()));
        log.info("index 2 value: {}", MaaFramework.buffer().MaaGetString(thd.getHandle()));
        MaaFramework.buffer().MaaDestroyStringBuffer(thd.getHandle());
        log.info("index 2 value: {}", MaaFramework.buffer().MaaGetString(thd.getHandle()));
        log.info("index 2 value: {}", MaaFramework.buffer().MaaGetString(thd.getHandle()));

        // [2024-xx-xx xx:xx:xx.xxx][ERR][Px44628][Tx12177][MaaBuffer.cpp][L16][MaaDestroyStringBuffer] handle is null
        MaaFramework.buffer().MaaDestroyStringBuffer(null);
    }
}