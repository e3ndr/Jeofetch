package xyz.e3ndr.jeofetch.types;

import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;
import lombok.With;
import xyz.e3ndr.jeofetch.Jeofetch;

@Value
@ToString
@AllArgsConstructor
public class SystemInfo {
    private @With String os;
    private @With String kernel;
    private @With long lastBootUpTime;

    private @With String artName;

    public SystemInfo() {
        this(null);
    }

    public SystemInfo(String artName) {
        this(null, null, -1, artName);
    }

    /**
     * Used to compare against the system's os info and return true or false.
     */
    public boolean doesMatchSystem() {
        SystemInfo osInfo = Jeofetch.osInfo;

        if ((this.os != null) && !regexHas(this.os, osInfo.os)) {
            return false;
        }

        if ((this.kernel != null) && !regexHas(this.kernel, osInfo.kernel)) {
            return false;
        }

        return true;
    }

    private boolean regexHas(String regex, String str) {
        return Pattern.compile(regex).matcher(str).find();
    }

}
