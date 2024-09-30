package io.github.hanhuoer.maa.buffer;

import io.github.hanhuoer.maa.define.MaaRectHandle;
import io.github.hanhuoer.maa.define.base.MaaBool;
import io.github.hanhuoer.maa.jna.MaaFramework;
import io.github.hanhuoer.maa.model.Rect;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * @author H
 */
@Getter
public class RectBuffer implements AutoCloseable {

    private final MaaRectHandle handle;
    private final boolean own;
    private boolean closed;


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
        this.closed = false;
    }

    @Override
    public void close() {
        if (this.handle == null) return;
        if (!this.own) return;
        MaaFramework.buffer().MaaRectDestroy(this.handle);
        this.closed = true;
    }

    public Rect getValue(Rect defaultValue) {
        return Objects.requireNonNullElse(getValue(), defaultValue);
    }

    public Rect getValue() {
        if (this.closed) return null;
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
        return setValue(rect.getX(), rect.getY(), rect.getW(), rect.getH());
    }

    public boolean setValue(int[] array) {
        if (array.length != 4) {
            throw new IllegalArgumentException("Array must have 4 elements");
        }
        return setValue(array[0], array[1], array[2], array[3]);
    }

    public boolean setValue(List<Integer> list) {
        if (list.size() != 4) {
            throw new IllegalArgumentException("List must have 4 elements");
        }
        return setValue(list.get(0), list.get(1), list.get(2), list.get(3));
    }

    private boolean setValue(Integer x, Integer y, Integer w, Integer h) {
        if (this.closed) return false;
        if (x == null) throw new IllegalArgumentException("x can not be null.");
        if (y == null) throw new IllegalArgumentException("y can not be null.");
        if (w == null) throw new IllegalArgumentException("w can not be null.");
        if (h == null) throw new IllegalArgumentException("h can not be null.");
        return MaaBool.TRUE.equals(MaaFramework.buffer().MaaRectSet(handle, x, y, w, h));
    }

}
