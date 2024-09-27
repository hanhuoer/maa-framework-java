package io.github.hanhuoer.maa.define;

import io.github.hanhuoer.maa.jna.MaaFramework;
import lombok.Getter;

/**
 * @author H
 */
@Getter
public class StringBuffer implements AutoCloseable {

    private final MaaStringBufferHandle handle;
    private final boolean own;


    public StringBuffer() {
        this(null);
    }

    public StringBuffer(MaaStringBufferHandle handle) {
        if (handle != null) {
            this.handle = handle;
            own = false;
        } else {
            this.handle = MaaFramework.buffer().MaaStringBufferCreate();
            own = true;
        }
    }

    @Override
    public void close() {
        if (this.handle == null) return;
        if (!this.own) return;
        MaaFramework.buffer().MaaStringBufferDestroy(this.handle);
    }

    public String getValue() {
        return getValue(false);
    }

    /**
     * @param close true if auto close
     */
    public String getValue(boolean close) {
        if (this.handle == null) throw new RuntimeException("MaaStringBufferHandle is null");
        String value = MaaFramework.buffer().MaaStringBufferGet(this.handle);
        Long valueLen = MaaFramework.buffer().MaaStringBufferSize(this.handle).getValue();
//        return handle.getPointer().getString(valueLen - 1, StandardCharsets.UTF_8.name());
        if (close) {
            close();
        }
        return value;
    }

    public boolean setValue(String value) {
        if (this.handle == null) throw new RuntimeException("MaaStringBufferHandle is null");
        return MaaFramework.buffer().MaaStringBufferSet(this.handle, value).getValue();
    }

    public boolean empty() {
        if (this.handle == null) throw new RuntimeException("MaaStringBufferHandle is null");
        return MaaFramework.buffer().MaaStringBufferIsEmpty(this.handle).getValue();
    }

    public boolean clear() {
        if (this.handle == null) throw new RuntimeException("MaaStringBufferHandle is null");
        return MaaFramework.buffer().MaaStringBufferClear(this.handle).getValue();
    }

}
