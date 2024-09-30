package io.github.hanhuoer.maa.buffer;

import io.github.hanhuoer.maa.Maa;
import io.github.hanhuoer.maa.define.MaaSize;
import io.github.hanhuoer.maa.define.MaaStringBufferHandle;
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
    void testCase() {
        List<StringBuffer> stringBufferList = new LinkedList<>();
        IntStream.range(0, 10).forEach(i -> {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.setValue((i) + "---" + i);
            stringBufferList.add(stringBuffer);
        });


        StringListBuffer stringListBuffer = new StringListBuffer();
        stringBufferList.forEach(item -> {
            Boolean b = MaaFramework.buffer().MaaStringListBufferAppend(stringListBuffer.getHandle(), item.getHandle()).getValue();
            log.info("append result: {}, value: {}", b, item.getValue());
        });


        MaaStringBufferHandle firstHandle = MaaFramework.buffer().MaaStringListBufferAt(stringListBuffer.getHandle(), new MaaSize(0));
        StringBuffer first = new StringBuffer(firstHandle);
        log.info("before destroy; index 0 value: {}", first.getValue());

        // 销毁原始 stringBuffer 影响不到 stringBufferList 的内容
        MaaFramework.buffer().MaaStringBufferDestroy(stringBufferList.get(0).getHandle());
        log.info("after destroy; index 0 value: {}", first.getValue());
        log.info("after destroy; index 0 value: {}", new StringBuffer(MaaFramework.buffer().MaaStringListBufferAt(stringListBuffer.getHandle(), new MaaSize(0))).getValue());

        stringListBuffer.close();
    }
}