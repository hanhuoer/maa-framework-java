package io.github.hanhuoer.maa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author H
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Rect {

    private int x;
    private int y;
    private int w;
    private int h;

    public Rect(int[] arr) {
        if (arr == null) throw new IllegalArgumentException("arr can not be null.");
        if (arr.length != 4) throw new IllegalArgumentException("the number of arr must be 4.");
        x = arr[0];
        y = arr[1];
        w = arr[2];
        h = arr[3];
    }

    public Rect(List<Integer> list) {
        if (list == null) throw new IllegalArgumentException("list can not be null.");
        if (list.size() != 4) throw new IllegalArgumentException("the number of list must be 4.");
        x = list.get(0);
        y = list.get(1);
        w = list.get(2);
        h = list.get(3);
    }

    public int[] toArray() {
        return new int[]{x, y, w, h};
    }

    public List<Integer> toList() {
        return List.of(x, y, w, h);
    }

    public int[] roi() {
        return toArray();
    }

    public int get(int index) {
        return toArray()[index];
    }


}
