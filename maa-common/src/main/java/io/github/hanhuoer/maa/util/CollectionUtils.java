package io.github.hanhuoer.maa.util;

import java.util.Collection;

/**
 * @author H
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        if (collection == null) return true;
        return collection.isEmpty();
    }

}
