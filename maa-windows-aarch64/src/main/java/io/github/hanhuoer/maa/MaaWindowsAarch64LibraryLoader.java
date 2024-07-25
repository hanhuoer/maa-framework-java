package io.github.hanhuoer.maa;

import io.github.hanhuoer.maa.loader.MaaLibraryLoader;
import io.github.hanhuoer.maa.util.JarFileUtil;

import java.io.IOException;

/**
 * @author H
 */
public class MaaWindowsAarch64LibraryLoader implements MaaLibraryLoader {

    @Override
    public void loadLibrary() throws IOException {
        JarFileUtil.copyDirectory("win_aarch64", "/lib");
    }

    @Override
    public String getMaaFrameworkName() {
        return "MaaFramework.dll";
    }

    @Override
    public String getMaaToolkitName() {
        return "MaaToolkit.dll";
    }
}
