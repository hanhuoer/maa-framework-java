package io.github.hanhuoer.maa.define;

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
            this.handle = MaaFramework.buffer().MaaRectCreate();
            own = true;
        }
    }

    @Override
    public void close() {
        if (this.handle == null) return;
        if (!this.own) return;
        MaaFramework.buffer().MaaRectDestroy(this.handle);
    }

    public Rect getValue() {
        Integer x = MaaFramework.buffer().MaaRectGetX(this.handle);
        Integer y = MaaFramework.buffer().MaaRectGetY(this.handle);
        Integer w = MaaFramework.buffer().MaaRectGetW(this.handle);
        Integer h = MaaFramework.buffer().MaaRectGetH(this.handle);

        return new Rect()
                .setX(x)
                .setY(y)
                .setW(w)
                .setH(h);
    }

    public boolean setValue(Rect rect) {
        return MaaFramework.buffer().MaaRectSet(
                handle, rect.getX(), rect.getY(), rect.getW(), rect.getH()).getValue();
    }

    public boolean setValue(int[] array) {
        if (array.length != 4) {
            throw new IllegalArgumentException("Array must have 4 elements");
        }
        return MaaFramework.buffer().MaaRectSet(
                handle, array[0], array[1], array[2], array[3]).getValue();
    }

    public boolean setValue(List<Integer> list) {
        if (list.size() != 4) {
            throw new IllegalArgumentException("List must have 4 elements");
        }
        return MaaFramework.buffer().MaaRectSet(
                handle, list.get(0), list.get(1), list.get(2), list.get(3)).getValue();
    }

}
