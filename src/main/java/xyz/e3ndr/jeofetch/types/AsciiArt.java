package xyz.e3ndr.jeofetch.types;

import org.jetbrains.annotations.Nullable;

import lombok.Getter;
import xyz.e3ndr.consoleutil.ansi.ConsoleColor;
import xyz.e3ndr.fastloggingframework.FastLoggingFramework;
import xyz.e3ndr.fastloggingframework.logging.FastLogger;
import xyz.e3ndr.fastloggingframework.logging.LogLevel;
import xyz.e3ndr.jeofetch.FileUtil;
import xyz.e3ndr.jeofetch.Jeofetch;

public class AsciiArt {
    private static char[][] blankArt;

    private @Getter String name;
    private @Getter boolean isValid;
    private @Getter ConsoleColor themeColor = ConsoleColor.RED;

    private ConsoleColor backgroundColor;
    private String[] artLines;

    static {
        blankArt = new char[Jeofetch.MAX_HEIGHT][];
        String ch40 = Jeofetch.explode(Jeofetch.MAX_HEIGHT * 2);

        for (int i = 0; i < blankArt.length; i++) {
            blankArt[i] = ch40.toCharArray();
        }
    }

    public AsciiArt(@Nullable String name) {
        this.name = name;

        if (this.name != null) {
            try {
                String resource = FileUtil.loadResource("art/" + this.name);

                if (resource == null) {
                    this.name = "-Invalid-";
                    return;
                }

                String[] artFile = resource.replace("\r", "").split("----", 3);
                String[] declarations = artFile[0].toUpperCase().split("\n");
                String[] colorTable = artFile[1].toUpperCase().split("\n");
                String artLines = artFile[2];

                for (String declaration : declarations) {
                    String[] split = declaration.trim().split(":");

                    if (split.length > 1) {
                        if (split[0].equals("THEME")) {
                            this.themeColor = ConsoleColor.valueOf(split[1].trim());
                        } else if (split[0].equals("BACKGROUND")) {
                            this.backgroundColor = ConsoleColor.valueOf(split[1].trim());
                        }
                    }
                }

                for (String colorDeclaration : colorTable) {
                    String[] split = colorDeclaration.trim().split(":");

                    if (split.length > 1) {
                        int index = Integer.parseInt(split[0]);
                        ConsoleColor color = ConsoleColor.valueOf(split[1].trim());

                        artLines = artLines.replace("{" + index + "}", color.getForeground());
                    }
                }

                this.artLines = artLines.replace("{_}", "").split("\n");
                this.isValid = true;
            } catch (Exception e) {
                FastLogger.logStatic(LogLevel.SEVERE, "An error occurred whilst loading art:");
                FastLogger.logException(e);
                FastLoggingFramework.close();
                System.exit(1);
            }
        }
    }

    public char[][] draw(boolean showColor) {
        if (this.isValid) {
            char[][] display = new char[20][];

            for (int i = 0; i < display.length; i++) {
                if (i < this.artLines.length) {
                    char[] line = new StringBuilder()
                        .append(this.getBackgroundColor())
                        .append(this.artLines[i])
                        .append("\033[49m\033[39m").toString().toCharArray();

                    display[i] = line;
                } else {
                    display[i] = new char[0];
                }
            }

            return display;
        } else {
            return blankArt;
        }
    }

    private String getBackgroundColor() {
        if (this.backgroundColor == null) {
            return "\033[49m";
        } else {
            return this.backgroundColor.getBackground();
        }
    }

    @Override
    public String toString() {
        return String.format("AsciiArt(name = %s)", this.name);
    }

}
