package xyz.e3ndr.jeofetch.system;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import co.casterlabs.rakurai.io.IOUtil;
import xyz.e3ndr.jeofetch.types.CpuInfo;

public class MacOSSystem implements ISystem {

    @Override
    public String getKernel() throws IOException {
        try {
            InputStream result = Runtime.getRuntime().exec("uname -r").getInputStream();

            return IOUtil.readInputStreamString(result, StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
        	e.printStackTrace();
            return "Generic";
        }
    }

    @Override
    public CpuInfo getCpuInfo() throws IOException {
        return getCpuInfo_sysctl();
    }

    @Override
    public String getOsName() throws IOException {
        return System.getProperty("os.name", "Unknown");
    }

    public String getHostname() throws UnknownHostException {
    	String hostname = InetAddress.getLocalHost().getHostName();
    	
    	if (hostname.endsWith(".local")) {
    		return hostname.substring(0, hostname.length() - ".local".length());
    	} else {
    		return hostname;
    	}
    }

    private static CpuInfo getCpuInfo_sysctl() throws IOException {
        String[] sysctl = IOUtil.readInputStreamString(Runtime.getRuntime().exec("sysctl -a").getInputStream(), StandardCharsets.UTF_8).split("\n");

        String cpuModel = "Generic";
        float clockSpeed = 0;
        int coreCount = 0;

        for (String line : sysctl) {
            String[] split = line.split(":");

            if (split.length > 1) {
	            String key = split[0].trim();
	            String value = split[1].trim();
	            
	            if (key.equals("machdep.cpu.thread_count")) {
	                coreCount = Integer.parseInt(split[1].trim());
	            } else if (key.equals("machdep.cpu.brand_string")) {
	                cpuModel = value;
	                clockSpeed = Float.parseFloat(value.split("\\@")[1].split("GHz")[0].trim()); // ICK
            }
            }
        }

        // Fix the cpu name result
        {
            cpuModel = cpuModel.split("@")[0];
            cpuModel = cpuModel.replace(" CPU ", " ");
            cpuModel = cpuModel.replace("(R)", "");
            cpuModel = cpuModel.replace("(TM)", "");
            cpuModel = cpuModel.replace("(r)", "");
            cpuModel = cpuModel.replace("(tm)", "");
            cpuModel = cpuModel.replace('-', ' ');

            String[] words = cpuModel.split("(?=\\W)");
            List<String> trueWords = new ArrayList<>(words.length);

            for (int i = 0; i < words.length; i++) {
                String word = words[i].trim();

                if (!word.isEmpty()) {
                    trueWords.add(word);
                }
            }

            cpuModel = String.join(" ", trueWords);
        }

        return new CpuInfo(cpuModel, coreCount, clockSpeed);
    }

    @Override
    public String getVersion() throws IOException {
        return null;
    }

}
