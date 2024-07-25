package io.github.hanhuoer.maa.ptr;

import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.model.Rect;
import lombok.Getter;

import java.util.List;

/**
 * @author H
 */
@Getter
public class RectBuffer implements AutoCloseable {

    private final MaaRectHandle handle;
    private final boolean own;


    public RectBuffer() {
        this(null);
    }

    public RectBuffer(MaaRectHandle handle) {
        if (handle != null) {
            this.handle = handle;
            own = false;
        } else {
            this.handle = MaaFramework.buffer().MaaCreateRectBuffer();
            own = true;
        }
    }

    @Override
    public void close() {
        if (this.handle == null) return;
        if (!this.own) return;
        MaaFramework.buffer().MaaDestroyRectBuffer(this.handle);
    }

    public Rect getValue() {
        Integer x = MaaFramework.buffer().MaaGetRectX(this.handle);
        Integer y = MaaFramework.buffer().MaaGetRectY(this.handle);
        Integer w = MaaFramework.buffer().MaaGetRectW(this.handle);
        Integer h = MaaFramework.buffer().MaaGetRectH(this.handle);

        return new Rect()
                .setX(x)
                .setY(y)
                .setW(w)
                .setH(h);
    }

    public boolean setValue(Rect rect) {
        return MaaFramework.buffer().MaaSetRect(
                handle, rect.getX(), rect.getY(), rect.getW(), rect.getH());
    }

    public boolean setValue(int[] array) {
        if (array.length != 4) {
            throw new IllegalArgumentException("Array must have 4 elements");
        }
        return MaaFramework.buffer().MaaSetRect(
                handle, array[0], array[1], array[2], array[3]);
    }

    public boolean setValue(List<Integer> list) {
        if (list.size() != 4) {
            throw new IllegalArgumentException("List must have 4 elements");
        }
        return MaaFramework.buffer().MaaSetRect(
                handle, list.get(0), list.get(1), list.get(2), list.get(3));
    }

}
