package xyz.e3ndr.jeofetch;

public class AnsiUtil {

    public static String stripAnsi(String str) {
        return str.replaceAll("\\e\\[[\\d;]*[^\\d;]", "");
    }

}
