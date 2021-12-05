package xyz.e3ndr.jeofetch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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

}
