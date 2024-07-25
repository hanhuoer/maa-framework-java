package io.github.hanhuoer.maa.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author H
 */
@Slf4j
public class JarFileUtil {

    public static final String BASE_DIR = new File(Objects.toString(System.getProperty("user.dir")), "resources/maa").getPath();
    static File tempDir = null;

    private JarFileUtil() {
    }

    public static synchronized void copyFileFromJar(String filePath, String targetDir) throws IOException {
        copyFileFromJar(filePath, targetDir, false, false);
    }

    /**
     * @param filePath     resources 下的文件路径
     * @param targetDir    目标文件夹
     * @param load         是否加载
     * @param deleteOnExit 是否退出后删除
     */
    public static synchronized void copyFileFromJar(String filePath, String targetDir, boolean load, boolean deleteOnExit) throws IOException {

        // 获取文件名并校验
        String[] parts = filePath.split("/");
        String filename = (parts.length > 1) ? parts[parts.length - 1] : null;
        if (filename == null || filename.length() < 3) {
            throw new IllegalArgumentException("文件名少于 3 个字符");
        }
        // 检查目标文件夹是否存在
        if (tempDir == null) {
            tempDir = new File(BASE_DIR, targetDir);
            if (!tempDir.exists() && !tempDir.mkdirs()) {
                throw new IOException("无法在目录创建文件夹" + tempDir);
            }
        }
        // 在临时文件夹下创建文件
        File temp = new File(tempDir, filename.startsWith("/") ? filename : "/" + filename);
        if (!temp.exists()) {
            // 从jar包中复制文件到系统临时文件夹
            try (InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(filePath)) {
                if (is != null) {
                    Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {
                    throw new NullPointerException();
                }
            } catch (IOException e) {
                Files.delete(temp.toPath());
                throw new IOException("无法复制文件 " + filePath + " 到 " + temp.getAbsolutePath(), e);
            } catch (NullPointerException e) {
                throw new FileNotFoundException("文件 " + filePath + " 在 Jar 中未找到.");
            }
        }
        // 加载临时文件夹中的动态库
        if (load) {
            System.load(temp.getAbsolutePath());
//            System.loadLibrary(temp.getAbsolutePath());
        }
        // JVM结束时删除临时文件和临时文件夹
        if (deleteOnExit) {
            temp.deleteOnExit();
            tempDir.deleteOnExit();
        }
        log.debug("将文件 {} 复制到 {}, 加载此文件: {}, JVM 是否退出时删除此文件: {}", filePath, targetDir, load, deleteOnExit);
    }

    public static void copyDirectory(String sourceDirPath, String targetDirPath) throws IOException {
        File sourceDir = new File(BASE_DIR, sourceDirPath);
        File targetDir = new File(BASE_DIR, targetDirPath);
        if (sourceDir.exists() && sourceDir.isDirectory()) {
            copyDirectoryFromFS(sourceDir, targetDir);
        } else {
            copyDirectoryFromJar(sourceDirPath, targetDirPath);
        }
    }

    private static void copyDirectoryFromFS(File sourceDir, File targetDir) throws IOException {
        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                throw new IOException("无法创建目标目录 " + targetDir);
            }
        }
        File[] files = sourceDir.listFiles();
        if (files != null) {
            for (File file : files) {
                File targetFile = new File(targetDir, file.getName());
                if (file.isDirectory()) {
                    copyDirectoryFromFS(file, targetFile);
                } else {
                    Files.copy(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    public static void copyDirectoryFromJar(String jarFilePath, String outputDir) throws IOException {
        URL jarUrl = Thread.currentThread().getContextClassLoader().getResource(jarFilePath);
        if (jarUrl == null) {
            throw new FileNotFoundException("资源路径 " + jarFilePath + " 在 JAR 中未找到.");
        }

        if (jarUrl.getProtocol().equals("jar")) {
            JarURLConnection jarConnection = (JarURLConnection) jarUrl.openConnection();
            try (JarFile jarFile = jarConnection.getJarFile()) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(jarFilePath)) {
                        String fileName = entryName.substring(jarFilePath.length());
                        File outFile = new File(BASE_DIR, outputDir + File.separator + fileName);
                        if (entry.isDirectory()) {
                            Files.createDirectories(outFile.toPath());
                        } else {
                            try (InputStream is = jarFile.getInputStream(entry)) {
                                Files.copy(is, outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            }
                        }
                    }
                }
            }
        } else if (jarUrl.getProtocol().equals("file")) {
            File sourceDir = new File(jarUrl.getPath());
            File targetDir = new File(BASE_DIR, outputDir);
            copyDirectoryFromFS(sourceDir, targetDir);
        } else {
            throw new UnsupportedOperationException("不支持的URL协议: " + jarUrl.getProtocol());
        }
    }

}
