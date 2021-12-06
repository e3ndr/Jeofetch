package xyz.e3ndr.jeofetch.system;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import co.casterlabs.rakurai.io.IOUtil;
import xyz.e3ndr.jeofetch.types.CpuInfo;

public class WindowsSystem implements ISystem {

    @Override
    public String getKernel() throws IOException {
        return "NT " + System.getProperty("os.version", "");
    }

    @Override
    public CpuInfo getCpuInfo() throws IOException {
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

    @Override
    public String getOsName() throws IOException {
        return System.getProperty("os.name");
    }

    @Override
    public String getVersion() throws IOException {
        return System.getProperty("os.version", "Unknown");
    }

//    @SuppressWarnings("unused")
//    private long getBootUpTime() {
//        try {
//            String uptimeQuery = IOUtil.readInputStreamString(Runtime.getRuntime().exec("net statistics workstation").getInputStream(), StandardCharsets.UTF_8).split("\n")[3].trim();
//
//            uptimeQuery = uptimeQuery.split(" since ")[1];
//
//            SimpleDateFormat parser = new SimpleDateFormat("MM/dd/YYYY HH:mm:ss a");
//
//            Date date = parser.parse(uptimeQuery);
//
//            return date.getTime();
//        } catch (Exception e) {
//            return -1;
//        }
//    }

}
