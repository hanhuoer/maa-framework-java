package io.github.hanhuoer.maa.ptr;

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
            this.handle = MaaFramework.buffer().MaaCreateStringBuffer();
            own = true;
        }
    }

    @Override
    public void close() {
        if (this.handle == null) return;
        if (!this.own) return;
        MaaFramework.buffer().MaaDestroyStringBuffer(this.handle);
    }

    public String getValue() {
        return getValue(false);
    }

    /**
     * @param close true if auto close
     */
    public String getValue(boolean close) {
        if (this.handle == null) throw new RuntimeException("MaaStringBufferHandle is null");
        String value = MaaFramework.buffer().MaaGetString(this.handle);
        Long valueLen = MaaFramework.buffer().MaaGetStringSize(this.handle);
//        return handle.getPointer().getString(valueLen - 1, StandardCharsets.UTF_8.name());
        if (close) {
            close();
        }
        return value;
    }

    public boolean setValue(String value) {
        if (this.handle == null) throw new RuntimeException("MaaStringBufferHandle is null");
        return MaaFramework.buffer().MaaSetString(this.handle, value);
    }

    public boolean empty() {
        if (this.handle == null) throw new RuntimeException("MaaStringBufferHandle is null");
        return MaaFramework.buffer().MaaIsStringEmpty(this.handle);
    }

    public boolean clear() {
        if (this.handle == null) throw new RuntimeException("MaaStringBufferHandle is null");
        return MaaFramework.buffer().MaaClearString(this.handle);
    }

}
