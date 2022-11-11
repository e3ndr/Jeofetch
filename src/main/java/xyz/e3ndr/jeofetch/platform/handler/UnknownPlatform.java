package xyz.e3ndr.jeofetch.platform.handler;

import xyz.e3ndr.jeofetch.types.CpuInfo;

public class UnknownPlatform implements PlatformHandler {

    @Override
    public String getKernel() {
        return "Unknown";
    }

    @Override
    public CpuInfo getCpuInfo() {
        return new CpuInfo("Generic CPU", 1, 0);
    }

    @Override
    public String getOsName() {
        return System.getProperty("os.name", "Unknown");
    }

    @Override
    public String getVersion() {
        return "Unknown";
    }

}
