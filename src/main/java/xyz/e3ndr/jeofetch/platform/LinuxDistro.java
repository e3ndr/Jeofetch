package xyz.e3ndr.jeofetch.platform;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LinuxDistro {
    UBUNTU("Ubuntu", "^Ubuntu", null),
    WSL("WSL", "^Linux", "microsoft"),

    ;

    public final String name;
    public final String osMatch;
    public final String kernelMatch;

}
