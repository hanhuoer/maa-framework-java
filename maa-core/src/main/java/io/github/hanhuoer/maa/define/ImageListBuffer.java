package io.github.hanhuoer.maa.define;

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
    }

    @Override
    public void close() {
        if (this.handle == null) return;
        if (!this.own) return;
        MaaFramework.buffer().MaaImageListBufferDestroy(this.handle);
    }

    public List<BufferedImage> getValue() throws IOException {
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
        ImageBuffer imageBuffer = new ImageBuffer();
        imageBuffer.setValue(value);
        return MaaBool.TRUE.equals(MaaFramework.buffer().MaaImageListBufferAppend(this.handle, imageBuffer.getHandle()));
    }

    public boolean remove(int index) {
        return MaaBool.TRUE.equals(MaaFramework.buffer().MaaImageListBufferRemove(this.handle, MaaSize.valueOf(index)));
    }

    public boolean clear() {
        return MaaBool.TRUE.equals(MaaFramework.buffer().MaaImageListBufferClear(this.handle));
    }

}
