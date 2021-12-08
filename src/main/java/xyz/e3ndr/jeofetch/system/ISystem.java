package xyz.e3ndr.jeofetch.system;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import xyz.e3ndr.jeofetch.types.CpuInfo;

public interface ISystem {

    public String getKernel() throws IOException;

    public CpuInfo getCpuInfo() throws IOException;

    public String getOsName() throws IOException;

    public String getVersion() throws IOException;
    
    default String getHostname() throws UnknownHostException {
    	return InetAddress.getLocalHost().getHostName();
    }

}
