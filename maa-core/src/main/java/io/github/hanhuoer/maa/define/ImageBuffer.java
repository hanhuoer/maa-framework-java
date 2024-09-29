package io.github.hanhuoer.maa.define;

import com.sun.jna.Memory;
import io.github.hanhuoer.maa.jna.MaaFramework;
import lombok.Getter;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author H
 */
@Getter
public class ImageBuffer implements AutoCloseable {

    private final MaaImageBufferHandle handle;
    private final boolean own;


    public ImageBuffer() {
        this(null);
    }

    public ImageBuffer(MaaImageBufferHandle handle) {
        if (handle != null) {
            this.handle = handle;
            this.own = false;
        } else {
            this.handle = MaaFramework.buffer().MaaImageBufferCreate();
            this.own = true;
        }

        if (this.handle == null) {
            throw new RuntimeException("Failed to create image buffer.");
        }
    }

    @Override
    public void close() {
        if (this.handle != null && this.own) {
            MaaFramework.buffer().MaaImageBufferDestroy(this.handle);
        }
    }

    public BufferedImage getValue(BufferedImage defaultValue) {
        try {
            return getValue();
        } catch (IOException e) {
            return defaultValue;
        }
    }

    public BufferedImage getValue() throws IOException {
        MaaImageRawData rawData = MaaFramework.buffer().MaaImageBufferGetRawData(this.handle);
        if (rawData == null) {
            return null;
        }

        int width = MaaFramework.buffer().MaaImageBufferWidth(this.handle);
        int height = MaaFramework.buffer().MaaImageBufferHeight(this.handle);

        int pixels = width * height * 3;

        ByteBuffer byteBuffer = rawData.getPointer().getByteBuffer(0, pixels);
        byteBuffer.order(ByteOrder.nativeOrder());

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        byte[] imageData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        byteBuffer.get(imageData);

        return image;
    }

    public boolean setValue(BufferedImage value) {
        int width = value.getWidth();
        int height = value.getHeight();

        byte[] pixels = new byte[width * height * 3];

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = value.getRGB(x, y);
                pixels[index++] = (byte) ((pixel >> 16) & 0xFF); // Red
                pixels[index++] = (byte) ((pixel >> 8) & 0xFF);  // Green
                pixels[index++] = (byte) (pixel & 0xFF);         // Blue
            }
        }

        Memory data = new Memory(pixels.length);
        data.write(0, pixels, 0, pixels.length);

        MaaImageRawData maaImageRawData = new MaaImageRawData();
        maaImageRawData.setPointer(data);
        return MaaFramework.buffer().MaaImageBufferSetRawData(this.handle, maaImageRawData, width, height, 16).getValue(); // CV_8UC3
    }

    public boolean isEmpty() {
        return MaaFramework.buffer().MaaImageBufferIsEmpty(this.handle).getValue();
    }

    public boolean clear() {
        return MaaFramework.buffer().MaaImageBufferClear(this.handle).getValue();
    }
}
