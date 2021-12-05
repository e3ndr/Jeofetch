package xyz.e3ndr.jeofetch.config;

import xyz.e3ndr.jeofetch.types.AsciiArt;
import xyz.e3ndr.jeofetch.types.SystemInfo;

public class OperatingSystems {

    // These all are to match the properties via regex.
    private static final SystemInfo[] operatingSystems = {
            new SystemInfo("windows.txtart")
                .withOs("^Windows"),
            new SystemInfo("apple.txtart")
                .withOs("(Darwin|mac)"),
            new SystemInfo("wsl.txtart")
                .withOs("^Linux")
                .withKernel("Microsoft"),
            new SystemInfo("tux.txtart")
                .withOs("^Linux"),
    };

    public static AsciiArt getArtForSystem(String force) {
        if (force == null) {
            try {
                for (SystemInfo info : operatingSystems) {
                    if (info.doesMatchSystem()) {
                        return new AsciiArt(info.getArtName());
                    }
                }
            } catch (Exception e) {}

            return new AsciiArt(null);
        } else {
            return new AsciiArt(force + ".txtart");
        }
    }

}
