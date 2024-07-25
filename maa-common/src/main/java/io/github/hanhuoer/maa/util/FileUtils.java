package io.github.hanhuoer.maa.util;

import java.io.File;

/**
 * @author H
 */
public class FileUtils {

    public static File joinUserDir(String... children) {
        return join(userDir().getAbsolutePath(), children);
    }

    /**
     * 拼接目录方法
     *
     * @param parent   起始目录
     * @param children 子目录们
     * @return 拼接后的 File 对象
     */
    public static File join(String parent, String... children) {
        if (parent == null || parent.isEmpty()) {
            throw new IllegalArgumentException("the parent path can not be empty.");
        }

        if (children == null || children.length == 0) {
            return new File(parent);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(parent);

        for (String child : children) {
            sb.append(File.separator).append(child);
        }

        return new File(sb.toString());
    }

    /**
     * 获取当前运行目录
     */
    public static File userDir() {
        return new File(System.getProperty("user.dir"));
    }

    public static void main(String[] args) {
        File file = join(System.getProperty("user.dir"), "resources", "maa");
        System.out.println(file);
        System.out.println(file.getAbsoluteFile());
    }

}
