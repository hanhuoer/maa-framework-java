package io.github.hanhuoer.maa.buffer;

import io.github.hanhuoer.maa.define.MaaImageBufferHandle;
import io.github.hanhuoer.maa.define.MaaImageListBufferHandle;
import io.github.hanhuoer.maa.define.MaaSize;
import io.github.hanhuoer.maa.define.base.MaaBool;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.util.CollectionUtils;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author H
 */
@Getter
public class ImageListBuffer implements AutoCloseable {

    private final MaaImageListBufferHandle handle;
    private final boolean own;
    private boolean closed;


    public ImageListBuffer() {
        this(null);
    }

    public ImageListBuffer(MaaImageListBufferHandle handle) {
        if (handle != null) {
            this.handle = handle;
            this.own = false;
        } else {
            this.handle = MaaFramework.buffer().MaaImageListBufferCreate();
            this.own = true;
        }

        if (this.handle == null) {
            throw new RuntimeException("Failed to create image list buffer.");
        }
        this.closed = false;
    }

    @Override
    public void close() {
        if (this.handle == null) return;
        if (!this.own) return;
        MaaFramework.buffer().MaaImageListBufferDestroy(this.handle);
        this.closed = true;
    }

    public List<BufferedImage> getValue(List<BufferedImage> defaultValue) {
        try {
            List<BufferedImage> valueList = getValue();
            if (CollectionUtils.isEmpty(valueList)) return defaultValue;
            return valueList;
        } catch (IOException e) {
            return defaultValue;
        }
    }

    public List<BufferedImage> getValue() throws IOException {
        if (closed) return null;
        MaaSize count = MaaFramework.buffer().MaaImageListBufferSize(this.handle);
        List<BufferedImage> result = new ArrayList<>();

        for (long i = 0; i < count.getValue(); i++) {
            MaaSize maaSize = new MaaSize(i);
            MaaImageBufferHandle maaImageBufferHandle = MaaFramework.buffer().MaaImageListBufferAt(this.handle, maaSize);
            BufferedImage img = new ImageBuffer(maaImageBufferHandle).getValue();
            result.add(img);
        }

        return result;
    }

    public boolean setValue(List<BufferedImage> value) {
        if (closed) return false;
        if (CollectionUtils.isEmpty(value)) return false;

        for (BufferedImage bufferedImage : value) {
            ImageBuffer imageBuffer = new ImageBuffer();
            imageBuffer.setValue(bufferedImage);
            MaaBool appendResult = MaaFramework.buffer().MaaImageListBufferAppend(this.handle, imageBuffer.getHandle());
            if (!MaaBool.TRUE.equals(appendResult)) return false;
        }

        return true;
    }

    public boolean append(BufferedImage value) {
        if (closed) return false;
        ImageBuffer imageBuffer = new ImageBuffer();
        imageBuffer.setValue(value);
        return MaaBool.TRUE.equals(MaaFramework.buffer().MaaImageListBufferAppend(this.handle, imageBuffer.getHandle()));
    }

    public boolean remove(int index) {
        if (closed) return false;
        return MaaBool.TRUE.equals(MaaFramework.buffer().MaaImageListBufferRemove(this.handle, MaaSize.valueOf(index)));
    }

    public boolean clear() {
        if (closed) return false;
        return MaaBool.TRUE.equals(MaaFramework.buffer().MaaImageListBufferClear(this.handle));
    }

}
