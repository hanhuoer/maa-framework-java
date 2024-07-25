package io.github.hanhuoer.maa.ptr;

import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.util.CollectionUtils;
import lombok.Getter;

import java.util.ArrayList;
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
            this.handle = MaaFramework.buffer().MaaCreateStringListBuffer();
            own = true;
        }
    }

    @Override
    public void close() {
        if (this.handle == null) return;
        if (!this.own) return;
        MaaFramework.buffer().MaaDestroyStringListBuffer(this.handle);
    }

    public List<String> getValue() {
        Long count = MaaFramework.buffer().MaaGetStringListSize(this.handle);
        if (count == null || count == 0) return new ArrayList<>();

        return LongStream.rangeClosed(0, count)
                .mapToObj(i -> {
                    MaaStringBufferHandle maaStringBufferHandle = MaaFramework.buffer().MaaGetStringListAt(this.handle, i);
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

            Boolean appendResult = MaaFramework.buffer().MaaStringListAppend(this.handle, stringBuffer.getHandle());
            stringBuffer.close();
            if (!Boolean.TRUE.equals(appendResult)) return false;
        }

        return true;
    }

    public boolean append(String value) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.setValue(value);
        Boolean appendResult = MaaFramework.buffer().MaaStringListAppend(this.handle, stringBuffer.getHandle());
        stringBuffer.close();
        return Boolean.TRUE.equals(appendResult);
    }

    public boolean empty() {
        return MaaFramework.buffer().MaaIsStringListEmpty(this.handle);
    }

    public boolean remove(int index) {
        return MaaFramework.buffer().MaaStringListRemove(this.handle, index);
    }

    public boolean clear() {
        return MaaFramework.buffer().MaaClearStringList(this.handle);
    }

}
