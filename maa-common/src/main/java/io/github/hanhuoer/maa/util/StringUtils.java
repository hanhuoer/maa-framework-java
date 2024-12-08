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

    public static int compareVersion(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        for (int i = 0; i < v1.length || i < v2.length; ++i) {
            int x = 0, y = 0;
            if (i < v1.length) {
                x = Integer.parseInt(v1[i]);
            }
            if (i < v2.length) {
                y = Integer.parseInt(v2[i]);
            }
            if (x > y) {
                return 1;
            }
            if (x < y) {
                return -1;
            }
        }
        return 0;
    }

}
