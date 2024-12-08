package io.github.hanhuoer.maa.component;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.VerRsrc.VS_FIXEDFILEINFO;
import com.sun.jna.platform.win32.Version;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import java.util.ArrayList;
import java.util.List;

public class FileVersion {

    public static String getVersion(String filePath) {
        IntByReference dwDummy = new IntByReference();
        dwDummy.setValue(0);

        int versionLength = Version.INSTANCE.GetFileVersionInfoSize(filePath, dwDummy);

        if (versionLength > 0) {
            // will hold the bytes of the FileVersionInfo struct
            byte[] bufferarray = new byte[versionLength];
            // allocates space on the heap (== malloc in C/C++)
            Pointer lpData = new Memory(bufferarray.length);
            // will contain the address of a pointer to the requested version information
            PointerByReference lplpBuffer = new PointerByReference();
            // will contain a pointer to the size of the requested data pointed to by lplpBuffer.
            IntByReference puLen = new IntByReference();

            // reads versionLength bytes from the executable file into the FileVersionInfo struct buffer
            boolean fileInfoResult = Version.INSTANCE.GetFileVersionInfo(filePath, 0, versionLength, lpData);

            // retrieve file description for language and code page "i"
            boolean verQueryVal = Version.INSTANCE.VerQueryValue(lpData, "\\", lplpBuffer, puLen);

            // contains version information for a file. This information is
            // language and code page independent
            VS_FIXEDFILEINFO lplpBufStructure = new VS_FIXEDFILEINFO(lplpBuffer.getValue());
            lplpBufStructure.read();

            int v1 = (lplpBufStructure.dwFileVersionMS).intValue() >>> 16;
            int v2 = (lplpBufStructure.dwFileVersionMS).intValue() & 0xffff;
            int v3 = (lplpBufStructure.dwFileVersionLS).intValue() >>> 16;
            int v4 = (lplpBufStructure.dwFileVersionLS).intValue() & 0xffff;

            return v1 + "." + v2 + "." + v3 + "." + v4;
        } else {
            System.out.println("No version info available");
        }
        return null;
    }

    public static void main(String[] args) {
        String filePath = "D:\\Tools\\Coder\\Developer_tools\\Java\\jdk-17.0.8\\bin\\msvcp140.dll";
        System.out.println(getVersion(filePath));
    }

    /**
     * The structure as implemented by the MSDN article
     */
    public static class LANGANDCODEPAGE extends Structure {
        /**
         * The language contained in the translation table
         **/
        public short wLanguage;
        /**
         * The code page contained in the translation table
         **/
        public short wCodePage;

        public LANGANDCODEPAGE(Pointer p) {
            useMemory(p);
        }

        public LANGANDCODEPAGE(Pointer p, int offset) {
            useMemory(p, offset);
        }

        public static int sizeOf() {
            return 4;
        }

        // newer versions of JNA require a field order to be set
        @Override
        protected List<String> getFieldOrder() {
            List<String> fieldOrder = new ArrayList<>();
            fieldOrder.add("wLanguage");
            fieldOrder.add("wCodePage");
            return fieldOrder;
        }
    }

}