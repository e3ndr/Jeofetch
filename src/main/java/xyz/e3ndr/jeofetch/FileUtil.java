package xyz.e3ndr.jeofetch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import co.casterlabs.rakurai.io.IOUtil;

public class FileUtil {

    public static String loadResource(String path) throws IOException {
        InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(path);

        if (in == null) {
            return null;
        } else {
            return IOUtil.readInputStreamString(in, StandardCharsets.UTF_8);
        }
    }

    public static String tryReadFile(String location) {
        File file = new File(location);

        if (file.exists()) {
            try {
                byte[] bytes = Files.readAllBytes(file.toPath());

                return new String(bytes, StandardCharsets.UTF_8);
            } catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }
    }

}
