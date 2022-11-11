package xyz.e3ndr.jeofetch;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import xyz.e3ndr.consoleutil.ConsoleUtil;
import xyz.e3ndr.consoleutil.ansi.ConsoleAttribute;
import xyz.e3ndr.consoleutil.ansi.ConsoleColor;
import xyz.e3ndr.jeofetch.platform.LinuxDistro;
import xyz.e3ndr.jeofetch.platform.PlatformUtils;
import xyz.e3ndr.jeofetch.types.AsciiArt;
import xyz.e3ndr.jeofetch.types.CpuInfo;
import xyz.e3ndr.jeofetch.types.SystemInfo;

public class Jeofetch {
    public static final int MAX_TABLE_WIDTH = 50;
    public static final int MAX_HEIGHT = 20;

    public static final SystemInfo osInfo = PlatformUtils.getSystemInfo();
    public static final CpuInfo cpuInfo = PlatformUtils.getCpuInfo();
    public static final @Nullable LinuxDistro linuxDistro = PlatformUtils.getLinuxDistro();

    public static AsciiArt art;

    public static String USERNAME;
    public static String HOSTNAME;

    static {
        USERNAME = System.getProperty("user.name", "user");

        try {
            HOSTNAME = PlatformUtils.getSystemHandler().getHostname();
        } catch (UnknownHostException e) {
            HOSTNAME = "localhost";
        }
    }

    public static void print(Bootstrap config) throws InterruptedException, IOException {
        try {
            ConsoleUtil.clearConsole();
        } catch (IOException ignored) {}

        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();

        if (config.isHideSensitive()) {
            USERNAME = "user";
            HOSTNAME = "localhost";
        }

        if (!config.isNoArt()) {
            art = ArtTable.getArtForSystem(config.getForced());

//            if (!art.isValid()) {
//                System.err.println("Invalid art.");
//                System.exit(1);
//            }
        }

        if (!config.isNoColor()) {
            System.out.print(ConsoleAttribute.RESET.getAnsi());
        }

        char[][] display = getTable(!config.isNoColor(), config);

        // Resize the array for the picture.
        if (config.isNoArt()) {
            // Draw the result.
            for (char[] line : display) {
                if (config.isNoColor()) {
                    System.out.println(AnsiUtil.stripAnsi(new String(line)));
                } else {
                    System.out.println(line);
                }
            }
        } else {
            char[][] artDisplay = art.draw(!config.isNoColor());

            StringBuilder mergedDisplay = new StringBuilder();

            for (int i = 0; i < MAX_HEIGHT; i++) {
                String displayLine = (i < display.length) ? new String(display[i]) : "";
                int nonColoredLength = AnsiUtil.stripAnsi(displayLine).length();

                if (nonColoredLength > MAX_TABLE_WIDTH) {
                    displayLine = displayLine.substring(0, MAX_TABLE_WIDTH);
                } else if (nonColoredLength < MAX_TABLE_WIDTH) {
                    int remaining = MAX_TABLE_WIDTH - nonColoredLength;

                    displayLine = displayLine + explode(remaining);
                }

                mergedDisplay
                    .append(displayLine)
                    .append(artDisplay[i])
                    .append("\n");
            }

            if (config.isNoColor()) {
                System.out.println(AnsiUtil.stripAnsi(mergedDisplay.toString()));
            } else {
                System.out.println(mergedDisplay);
            }
        }

        System.out.println();
    }

    private static char[][] getTable(boolean enableColor, Bootstrap config) {
        List<String> table = new LinkedList<>(
            Arrays.asList(
                String.format("%s%s%s@%s%s", art.getThemeColor().getForeground(), USERNAME, ConsoleColor.TERMINAL_DEFAULT, art.getThemeColor().getForeground(), HOSTNAME),
                String.format("-------+-----------------------"),
                String.format("OS     | %s", osInfo.getOs()),
                String.format("Kernel | %s", osInfo.getKernel()),
                String.format("CPU    | %s (%d) @ %.2fGHz", cpuInfo.getModel(), cpuInfo.getCoreCount(), cpuInfo.getClockSpeed())
            )
        );

        if (linuxDistro != null) {
            table.add(
                String.format("Distro | %s", linuxDistro.name)
            );
        }

        if (config.getForced() != null) {
            table.add(
                String.format("Art    | %s%s%s", ConsoleAttribute.BOLD.getAnsi(), art.getName(), ConsoleAttribute.RESET.getAnsi())
            );
        }

        if (enableColor) {
            // Spacer
            table.add("");
            table.add("");

            // Color table
            table.add("");
            table.add("");

            // Make the table colorful.
            for (int i = 0; i < table.size(); i++) {
                String[] line = table.get(i).split("\\|");

                if (line.length > 1) {
                    table.set(i, String.format("%s%s%s|%s", art.getThemeColor().getForeground(), line[0], ConsoleColor.TERMINAL_DEFAULT, line[1]));
                }

                // Reset the color at the end of the line.
                table.set(i, table.get(i) + ConsoleColor.TERMINAL_DEFAULT);
            }

            // Generate the color table.
            ConsoleColor[] darkColors = {
                    ConsoleColor.BLACK,
                    ConsoleColor.RED,
                    ConsoleColor.GREEN,
                    ConsoleColor.YELLOW,
                    ConsoleColor.BLUE,
                    ConsoleColor.MAGENTA,
                    ConsoleColor.CYAN,
                    ConsoleColor.GRAY
            };
            ConsoleColor[] lightColors = {
                    ConsoleColor.DARK_GREY,
                    ConsoleColor.LIGHT_RED,
                    ConsoleColor.LIGHT_GREEN,
                    ConsoleColor.LIGHT_YELLOW,
                    ConsoleColor.LIGHT_BLUE,
                    ConsoleColor.LIGHT_MAGENTA,
                    ConsoleColor.LIGHT_CYAN,
                    ConsoleColor.WHITE
            };

            StringBuilder darkColorsDisplay = new StringBuilder();
            StringBuilder lightColorsDisplay = new StringBuilder();

            for (ConsoleColor color : darkColors) {
                darkColorsDisplay.append(color.getBackground()).append("  ");
            }

            for (ConsoleColor color : lightColors) {
                lightColorsDisplay.append(color.getBackground()).append("  ");
            }

            darkColorsDisplay.append(ConsoleColor.TERMINAL_DEFAULT.getBackground());
            lightColorsDisplay.append(ConsoleColor.TERMINAL_DEFAULT.getBackground());

            table.set(table.size() - 2, darkColorsDisplay.toString());
            table.set(table.size() - 1, lightColorsDisplay.toString());
        }

        char[][] tableChars = new char[table.size()][];

        for (int i = 0; i < tableChars.length; i++) {
            tableChars[i] = table.get(i).toCharArray();
        }

        return tableChars;
    }

    public static String explode(int length) {
        char[] chars = new char[length];

        for (int i = 0; i < chars.length; i++) {
            chars[i] = ' ';
        }

        return new String(chars);
    }

}
