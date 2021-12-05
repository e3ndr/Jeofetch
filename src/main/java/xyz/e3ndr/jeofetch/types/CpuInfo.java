package xyz.e3ndr.jeofetch.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CpuInfo {
    private String model;
    private int coreCount;
    private float clockSpeed;

}
