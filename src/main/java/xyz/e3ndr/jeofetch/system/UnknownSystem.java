package xyz.e3ndr.jeofetch.system;

import xyz.e3ndr.jeofetch.types.CpuInfo;

public class UnknownSystem implements ISystem {

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

}
