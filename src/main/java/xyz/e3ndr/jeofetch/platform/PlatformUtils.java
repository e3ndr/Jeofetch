package xyz.e3ndr.jeofetch.platform;

import java.io.IOException;

import co.casterlabs.commons.platform.OSDistribution;
import co.casterlabs.commons.platform.Platform;
import lombok.Getter;
import xyz.e3ndr.jeofetch.Jeofetch;
import xyz.e3ndr.jeofetch.platform.handler.BsdPlatform;
import xyz.e3ndr.jeofetch.platform.handler.PlatformHandler;
import xyz.e3ndr.jeofetch.platform.handler.LinuxPlatform;
import xyz.e3ndr.jeofetch.platform.handler.UnknownPlatform;
import xyz.e3ndr.jeofetch.platform.handler.WindowsPlatform;
import xyz.e3ndr.jeofetch.types.CpuInfo;
import xyz.e3ndr.jeofetch.types.SystemInfo;

public class PlatformUtils {
    private static UnknownPlatform unknownSystemHandler = new UnknownPlatform();
    private static @Getter PlatformHandler systemHandler;

    static {
        switch (Platform.osDistribution) {
            // Windows
            case WINDOWS_9X:
            case WINDOWS_NT:
                systemHandler = new WindowsPlatform();
                break;

            // Unix
            case LINUX:
                systemHandler = new LinuxPlatform();
                break;

            case BSD:
            case MACOSX:
                systemHandler = new BsdPlatform();
                break;

            // Other
            case MS_DOS:
            case OPEN_VMS:
            case SOLARIS:
            case GENERIC:
                break;
        }
    }

    public static SystemInfo getSystemInfo() {
        String os = unknownSystemHandler.getOsName();
        String kernel = unknownSystemHandler.getKernel();

        // Try to get the current system info.
        // Otherwise we fallback on the defaults provided by the unknown handler.
        try {
            os = systemHandler.getOsName();
            kernel = systemHandler.getKernel();

            if ((Platform.osDistribution == OSDistribution.LINUX) && kernel.contains("Microsoft")) {
                os = "Linux (WSL)";
            }
        } catch (IOException ignored) {}

        return new SystemInfo()
            .withOs(os)
            .withKernel(kernel);
    }

    public static LinuxDistro getLinuxDistro() {
        if (Platform.osDistribution != OSDistribution.LINUX) {
            return null;
        }

        SystemInfo osInfo = Jeofetch.osInfo;
        for (LinuxDistro distro : LinuxDistro.values()) {
            if ((distro.osMatch != null) && !SystemInfo.regexHas(distro.osMatch, osInfo.getOs())) {
                continue;
            }

            if ((distro.kernelMatch != null) && !SystemInfo.regexHas(distro.kernelMatch, osInfo.getKernel())) {
                continue;
            }

            return distro;
        }

        return null;
    }

    public static CpuInfo getCpuInfo() {
        try {
            return systemHandler.getCpuInfo();
        } catch (IOException e) {
            return unknownSystemHandler.getCpuInfo();
        }
    }

}
