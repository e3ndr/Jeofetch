package xyz.e3ndr.jeofetch;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.casterlabs.rakurai.io.IOUtil;
import xyz.e3ndr.consoleutil.ConsoleUtil;
import xyz.e3ndr.jeofetch.types.CpuInfo;
import xyz.e3ndr.jeofetch.types.SystemInfo;

public class SystemUtils {
    private static CpuInfo BLANK_CPUINFO = new CpuInfo("Generic", 1, 0);

    public static SystemInfo getSystemInfo() {
        String os = System.getProperty("os.name", "Unknown");
        String kernel = getSystemKernel();

        if (os.equals("Linux") && kernel.contains("Microsoft")) {
            os = "Linux (WSL)";
        }

        return new SystemInfo()
            .withOs(os)
            .withKernel(kernel);
    }

    public static CpuInfo getCpuInfo() {
        try {
            switch (ConsoleUtil.getPlatform()) {
                case UNIX:
                    return unixGetCpuInfo();

                case WINDOWS:
                    return windowsGetCpuInfo();

                case UNKNOWN:
                default:
                    return BLANK_CPUINFO;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return BLANK_CPUINFO;
        }
    }

    /* ------------ */
    /* Helpers */
    /* ------------ */

    @SuppressWarnings("unused")
    private static long windowsGetBootUpTime() {
        try {
            String uptimeQuery = IOUtil.readInputStreamString(Runtime.getRuntime().exec("net statistics workstation").getInputStream(), StandardCharsets.UTF_8).split("\n")[3].trim();

            uptimeQuery = uptimeQuery.split(" since ")[1];

            SimpleDateFormat parser = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss a");

            Date date = parser.parse(uptimeQuery);

            return date.getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    private static String getSystemKernel() {
        switch (ConsoleUtil.getPlatform()) {
            case UNIX: {
                try {
                    InputStream result = Runtime.getRuntime().exec("uname -r").getInputStream();

                    return IOUtil.readInputStreamString(result, StandardCharsets.UTF_8).trim();
                } catch (Exception e) {
                    return "Generic";
                }
            }

            case WINDOWS:
                return "NT " + System.getProperty("os.version", "");

            case UNKNOWN:
            default:
                return "UNKNOWN";

        }
    }

    private static CpuInfo windowsGetCpuInfo() throws IOException {
        String currentClockSpeedQuery = IOUtil.readInputStreamString(Runtime.getRuntime().exec("wmic CPU get MaxClockSpeed").getInputStream(), StandardCharsets.UTF_8).split("\n")[1].trim();
        String threadCountQuery = IOUtil.readInputStreamString(Runtime.getRuntime().exec("wmic CPU get ThreadCount").getInputStream(), StandardCharsets.UTF_8).split("\n")[1].trim();
        String nameQuery = IOUtil.readInputStreamString(Runtime.getRuntime().exec("wmic CPU get Name").getInputStream(), StandardCharsets.UTF_8).split("\n")[1].trim();

        float clockSpeed = Integer.parseInt(currentClockSpeedQuery) / 1000f;
        int coreCount = Integer.parseInt(threadCountQuery);

        String cpuModel;

        // Fix the cpu name result
        {
            nameQuery = nameQuery.split("@")[0];
            nameQuery = nameQuery.replace(" CPU ", " ");
            nameQuery = nameQuery.replace("(R)", "");
            nameQuery = nameQuery.replace("(TM)", "");
            nameQuery = nameQuery.replace("(r)", "");
            nameQuery = nameQuery.replace("(tm)", "");

            String[] words = nameQuery.split("((?=\\W))");
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

    private static CpuInfo unixGetCpuInfo() throws IOException {
        String[] lscpu = IOUtil.readInputStreamString(Runtime.getRuntime().exec("sh -c 'lscpu'").getInputStream(), StandardCharsets.UTF_8).split("\n");

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

}
