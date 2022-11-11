package xyz.e3ndr.jeofetch.platform.handler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import co.casterlabs.rakurai.io.IOUtil;
import xyz.e3ndr.jeofetch.types.CpuInfo;

public class LinuxPlatform implements PlatformHandler {

    @Override
    public String getKernel() throws IOException {
        try {
            InputStream result = Runtime.getRuntime().exec("uname -r").getInputStream();

            return IOUtil.readInputStreamString(result, StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
            return "Generic";
        }
    }

    @Override
    public CpuInfo getCpuInfo() throws IOException {
        return getCpuInfo_lscpu();
    }

    @Override
    public String getOsName() throws IOException {
        return System.getProperty("os.name", "Unknown");
    }

    private static CpuInfo getCpuInfo_lscpu() throws IOException {
        String[] lscpu = IOUtil.readInputStreamString(Runtime.getRuntime().exec("lscpu").getInputStream(), StandardCharsets.UTF_8).split("\n");

        String cpuModel = "Generic";
        float clockSpeed = 0;
        int coreCount = 0;

        for (String line : lscpu) {
            String[] split = line.split(":");

            if (split[0].startsWith("CPU max MHz")) {
                clockSpeed = Float.parseFloat(split[1].trim()) / 1000f;
            } else if (split[0].startsWith("CPU(s)")) {
                coreCount = Integer.parseInt(split[1].trim());
            } else if (split[0].startsWith("Model name")) {
                cpuModel = split[1].trim();
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

            String[] words = cpuModel.split("((?=\\W))");
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
