package io.github.hanhuoer.maa.util;

/**
 * @author H
 */
public class StringUtils {

    public static boolean isBlank(String content) {
        return isNull(content) || content.isEmpty() || content.isBlank();
    }

    public static boolean isEmpty(String content) {
        return isNull(content) || content.isEmpty();
    }

    public static boolean isNull(String content) {
        return null == content;
    }

    public static boolean hasText(String content) {
        return !isEmpty(content) && !isBlank(content);
    }
}
