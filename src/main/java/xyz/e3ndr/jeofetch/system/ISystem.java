package xyz.e3ndr.jeofetch.system;

import java.io.IOException;

import xyz.e3ndr.jeofetch.types.CpuInfo;

public interface ISystem {

    public String getKernel() throws IOException;

    public CpuInfo getCpuInfo() throws IOException;

    public String getOsName() throws IOException;

}
