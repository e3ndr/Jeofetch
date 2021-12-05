package xyz.e3ndr.jeofetch;

import java.io.IOException;

import lombok.Getter;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import xyz.e3ndr.consoleutil.ConsoleUtil;
import xyz.e3ndr.consoleutil.input.InputKey;
import xyz.e3ndr.consoleutil.input.KeyHook;
import xyz.e3ndr.consoleutil.input.KeyListener;

@Getter
@Command(name = "print", mixinStandardHelpOptions = true, version = ":^)", description = "Prints a neofetch-esque report")
public class Bootstrap implements Runnable {

    @Option(names = {
            "-H",
            "--hide"
    }, description = "Hides sensitive information")
    private boolean hideSensitive = false;

    @Option(names = {
            "-na",
            "--no-art"
    }, description = "Disables art")
    private boolean noArt = false;

    @Option(names = {
            "-nc",
            "--no-color"
    }, description = "Disables color and formatting")
    private boolean noColor = false;

    @Option(names = {
            "-s",
            "--stay"
    }, description = "Stays on screen until you press a key")
    private boolean stay = false;

    @Option(names = {
            "-f",
            "--force"
    }, description = "Forces a piece of art to be on screen")
    private String forced = null;

    public static void main(String[] args) {
//        try {
//            ConsoleUtil.summonConsoleWindow();
//        } catch (IOException | InterruptedException e) {}

        new CommandLine(new Bootstrap()).execute(args);
    }

    @Override
    public void run() {
        if (System.getProperty("StartedWithConsole", "").equals("true")) {
            try {
                this.stay = true;
                ConsoleUtil.setTitle("Jeofetch");
            } catch (IOException | InterruptedException e) {}
        }

        try {
            Jeofetch.print(this);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        if (this.stay) {
            KeyHook.addListener(new KeyListener() {
                @Override
                public void onKey(char key, boolean alt, boolean control) {
                    System.exit(0);
                }

                @Override
                public void onKey(InputKey key) {
                    System.exit(0);
                }
            });

            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {}
        }
    }

}
