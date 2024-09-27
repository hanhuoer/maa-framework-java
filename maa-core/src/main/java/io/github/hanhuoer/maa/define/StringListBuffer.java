package io.github.hanhuoer.maa.define;

import io.github.hanhuoer.maa.define.base.MaaBool;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.util.CollectionUtils;
import lombok.Getter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author H
 */
@Getter
public class StringListBuffer implements AutoCloseable {

    private final MaaStringListBufferHandle handle;
    private final boolean own;
    private final List<StringBuffer> stringBuffers = new LinkedList<>();


    public StringListBuffer() {
        this(null);
    }

    public StringListBuffer(MaaStringListBufferHandle handle) {
        if (handle != null) {
            this.handle = handle;
            own = false;
        } else {
            this.handle = MaaFramework.buffer().MaaStringListBufferCreate();
            own = true;
        }
    }

    @Override
    public void close() {
        if (this.handle == null) return;
        if (!this.own) return;
        MaaFramework.buffer().MaaStringListBufferDestroy(this.handle);
    }

    public List<String> getValue() {
        MaaSize maaSize = MaaFramework.buffer().MaaStringListBufferSize(this.handle);
        if (maaSize == null) return Collections.emptyList();
        long count = maaSize.getValue();
        if (count == 0) return Collections.emptyList();

        return LongStream.range(0, count)
                .mapToObj(i -> {
                    MaaStringBufferHandle maaStringBufferHandle = MaaFramework.buffer().MaaStringListBufferAt(this.handle, new MaaSize(i));
                    StringBuffer stringBuffer = new StringBuffer(maaStringBufferHandle);
                    String value = stringBuffer.getValue();
                    stringBuffer.close();
                    return value;
                })
                .collect(Collectors.toList());
    }

    public boolean setValue(List<String> value) {
        if (CollectionUtils.isEmpty(value)) return false;

        for (String item : value) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.setValue(item);

            MaaBool appendResult = MaaFramework.buffer().MaaStringListBufferAppend(this.handle, stringBuffer.getHandle());
            stringBuffer.close();
            if (!MaaBool.TRUE.equals(appendResult)) return false;
        }

        return true;
    }

    public boolean append(String value) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.setValue(value);
        MaaBool appendResult = MaaFramework.buffer().MaaStringListBufferAppend(this.handle, stringBuffer.getHandle());
        stringBuffer.close();
        return MaaBool.TRUE.equals(appendResult);
    }

    public boolean empty() {
        return MaaFramework.buffer().MaaStringListBufferIsEmpty(this.handle).getValue();
    }

    public boolean remove(int index) {
        return MaaFramework.buffer().MaaStringListBufferRemove(this.handle, new MaaSize(index)).getValue();
    }

    public boolean clear() {
        return MaaFramework.buffer().MaaStringListBufferClear(this.handle).getValue();
    }

}
