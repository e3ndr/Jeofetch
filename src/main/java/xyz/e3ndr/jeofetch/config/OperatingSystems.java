package xyz.e3ndr.jeofetch.config;

import xyz.e3ndr.jeofetch.types.AsciiArt;
import xyz.e3ndr.jeofetch.types.SystemInfo;

public class OperatingSystems {

    // These all are to match the properties via regex.
    private static final SystemInfo[] operatingSystems = {

            // Microsoft
            new SystemInfo("windows-square.txtart")
                .withOs("Windows 11"),

            new SystemInfo("windows-metro.txtart")
                .withOs("Windows")
                .withKernel("(6\\.2|6\\.3||10\\.0)"),

            new SystemInfo("windows-flag.txtart")
                .withOs("Windows"),

            new SystemInfo("wsl.txtart")
                .withOs("^Linux")
                .withKernel("Microsoft"),

            // Apple
            new SystemInfo("apple.txtart")
                .withOs("(Darwin|mac|Mac)"),

            // Linux
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
