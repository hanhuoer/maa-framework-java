package io.github.hanhuoer.maa;

import io.github.hanhuoer.maa.loader.AgentLibraryLoader;
import io.github.hanhuoer.maa.util.JarFileUtil;

import java.io.IOException;

/**
 * @author H
 */
public class MaaAgentLibraryLoader implements AgentLibraryLoader {

    @Override
    public void loadLibrary() throws IOException {
        JarFileUtil.copyDirectory("agent", "/agent");
    }
}
