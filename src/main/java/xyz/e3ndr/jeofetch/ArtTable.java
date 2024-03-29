package xyz.e3ndr.jeofetch;

import xyz.e3ndr.jeofetch.platform.LinuxDistro;
import xyz.e3ndr.jeofetch.types.AsciiArt;
import xyz.e3ndr.jeofetch.types.SystemInfo;

public class ArtTable {

    // @formatter:off
    private static final SystemInfo[] operatingSystems = {
        // Windows
        new SystemInfo("windows/square.txtart")
            .withOs("Windows 11"),

        new SystemInfo("windows/metro.txtart")
            .withOs("Windows")
            .withKernel("(6\\.2|6\\.3||10\\.0)"),

        new SystemInfo("windows/flag.txtart")
            .withOs("Windows"),

        // BSD
        new SystemInfo("bsd/apple.txtart")
            .withOs("(Darwin|mac|Mac)"),

        // Linux
        new SystemInfo("linux/wsl.txtart")
            .withDistro(LinuxDistro.WSL),

        new SystemInfo("linux/ubuntu.txtart")
            .withDistro(LinuxDistro.UBUNTU),

        new SystemInfo("linux/tux.txtart")
            .withOs("^Linux"),

    };
    // @formatter:on

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
