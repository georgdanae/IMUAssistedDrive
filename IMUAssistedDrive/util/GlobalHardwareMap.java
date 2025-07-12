package org.firstinspires.ftc.teamcode.IMUAssistedDrive.util;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class GlobalHardwareMap {
    static HardwareMap hw;

    public static void setHw(HardwareMap hw) {
        GlobalHardwareMap.hw = hw;
    }

    public static HardwareMap getHw() {
        return hw;
    }
}