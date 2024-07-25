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
class StringListBufferTest {

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
    void setValue() {
    }

    @Test
    void append() {
    }

    @Test
    void empty() {
    }

    @Test
    void remove() {
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
    void getStringBuffers() {
    }

    @Test
    void test() {
        List<StringBuffer> stringBufferList = new LinkedList<>();
        IntStream.range(0, 10).forEach(i -> {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.setValue((i) + "---" + i);
            stringBufferList.add(stringBuffer);
        });


        StringListBuffer stringListBuffer = new StringListBuffer();
        stringBufferList.forEach(item -> {
            Boolean b = MaaFramework.buffer().MaaStringListAppend(stringListBuffer.getHandle(), item.getHandle());
            log.info("append result: {}, value: {}", b, item.getValue());
        });


        MaaStringBufferHandle firstHandle = MaaFramework.buffer().MaaGetStringListAt(stringListBuffer.getHandle(), 0);
        StringBuffer first = new StringBuffer(firstHandle);
        log.info("before destroy; index 0 value: {}", first.getValue());

        // 销毁原始 stringBuffer 影响不到 stringBufferList 的内容
        MaaFramework.buffer().MaaDestroyStringBuffer(stringBufferList.get(0).getHandle());
        log.info("after destroy; index 0 value: {}", first.getValue());
        log.info("after destroy; index 0 value: {}", new StringBuffer(MaaFramework.buffer().MaaGetStringListAt(stringListBuffer.getHandle(), 0)).getValue());

        stringListBuffer.close();
    }
}