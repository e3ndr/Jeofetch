package xyz.e3ndr.jeofetch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import xyz.e3ndr.consoleutil.ConsoleUtil;
import xyz.e3ndr.consoleutil.platform.JavaPlatform;
import xyz.e3ndr.jeofetch.system.ISystem;
import xyz.e3ndr.jeofetch.system.UnixSystem;
import xyz.e3ndr.jeofetch.system.UnknownSystem;
import xyz.e3ndr.jeofetch.system.WindowsSystem;
import xyz.e3ndr.jeofetch.types.CpuInfo;
import xyz.e3ndr.jeofetch.types.SystemInfo;

public class SystemUtils {
    private static Map<JavaPlatform, ISystem> systemHandlers = new HashMap<>();

    private static UnknownSystem unknownSystemHandler = new UnknownSystem();
    private static ISystem systemHandler;

    static {
        systemHandlers.put(JavaPlatform.WINDOWS, new WindowsSystem());
        systemHandlers.put(JavaPlatform.UNIX, new UnixSystem());
        systemHandlers.put(JavaPlatform.UNKNOWN, unknownSystemHandler);

        // Get the current system handler OR unknown.
        systemHandler = systemHandlers.getOrDefault(ConsoleUtil.getPlatform(), unknownSystemHandler);
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
