package io.github.hanhuoer.maa.ptr;

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
            this.handle = MaaFramework.buffer().MaaCreateImageListBuffer();
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
        MaaFramework.buffer().MaaDestroyImageListBuffer(this.handle);
    }

    public List<BufferedImage> getValue() throws IOException {
        Long count = MaaFramework.buffer().MaaGetImageListSize(this.handle);
        List<BufferedImage> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            MaaImageBufferHandle maaImageBufferHandle = MaaFramework.buffer().MaaGetImageListAt(this.handle, i);
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
            Boolean appendResult = MaaFramework.buffer().MaaImageListAppend(this.handle, imageBuffer.getHandle());
            if (!Boolean.TRUE.equals(appendResult)) return false;
        }

        return true;
    }

    public boolean append(BufferedImage value) {
        ImageBuffer imageBuffer = new ImageBuffer();
        imageBuffer.setValue(value);
        return Boolean.TRUE.equals(MaaFramework.buffer().MaaImageListAppend(this.handle, imageBuffer.getHandle()));
    }

    public boolean remove(int index) {
        return Boolean.TRUE.equals(MaaFramework.buffer().MaaImageListRemove(this.handle, index));
    }

    public boolean clear() {
        return Boolean.TRUE.equals(MaaFramework.buffer().MaaClearImageList(this.handle));
    }

}
