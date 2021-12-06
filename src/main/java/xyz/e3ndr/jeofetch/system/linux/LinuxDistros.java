package xyz.e3ndr.jeofetch.system.linux;

import lombok.Getter;

@Getter
public enum LinuxDistros {
    UBUNTU("Ubuntu"),

    ;

    private String name = this.name();

    private LinuxDistros(String name) {
        this.name = name;
    }

}
