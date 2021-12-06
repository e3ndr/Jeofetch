package xyz.e3ndr.jeofetch;

import java.io.IOException;

import xyz.e3ndr.jeofetch.system.ISystem;
import xyz.e3ndr.jeofetch.system.MacOSSystem;
import xyz.e3ndr.jeofetch.system.UnixSystem;
import xyz.e3ndr.jeofetch.system.UnknownSystem;
import xyz.e3ndr.jeofetch.system.WindowsSystem;
import xyz.e3ndr.jeofetch.types.CpuInfo;
import xyz.e3ndr.jeofetch.types.SystemInfo;

public class SystemUtils {
    private static UnknownSystem unknownSystemHandler = new UnknownSystem();
    private static ISystem systemHandler;

    static {
        String name = System.getProperty("os.name", "").toLowerCase();

        // Get the current system handler OR unknown.
        if (name.contains("mac") || name.contains("darwin")) {
            systemHandler = new MacOSSystem();
        } else if (name.contains("linux")) {
            systemHandler = new UnixSystem();
        } else if (name.contains("windows")) {
            systemHandler = new WindowsSystem();
        } else {
            systemHandler = unknownSystemHandler;
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

            if (os.equals("Linux") && kernel.contains("Microsoft")) {
                os = "Linux (WSL)";
            }
        } catch (IOException ignored) {}

        return new SystemInfo()
            .withOs(os)
            .withKernel(kernel);
    }

    public static CpuInfo getCpuInfo() {
        try {
            return systemHandler.getCpuInfo();
        } catch (IOException e) {
            return unknownSystemHandler.getCpuInfo();
        }
    }

}
