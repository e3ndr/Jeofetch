package xyz.e3ndr.jeofetch;

import xyz.e3ndr.jeofetch.platform.LinuxDistro;
import xyz.e3ndr.jeofetch.types.AsciiArt;
import xyz.e3ndr.jeofetch.types.SystemInfo;

public class ArtTable {

    // These all are to match the properties via regex.
    private static final SystemInfo[] operatingSystems = {

            // Microsoft
            new SystemInfo("microsoft/windows-square.txtart")
                .withOs("Windows 11"),

            new SystemInfo("microsoft/windows-metro.txtart")
                .withOs("Windows")
                .withKernel("(6\\.2|6\\.3||10\\.0)"),

            new SystemInfo("microsoft/windows-flag.txtart")
                .withOs("Windows"),

            // Apple
            new SystemInfo("apple/apple.txtart")
                .withOs("(Darwin|mac|Mac)"),

            // Linux
            new SystemInfo("microsoft/wsl.txtart")
                .withDistro(LinuxDistro.WSL),

            new SystemInfo("linux/ubuntu.txtart")
                .withDistro(LinuxDistro.UBUNTU),

            new SystemInfo("linux/tux.txtart")
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
