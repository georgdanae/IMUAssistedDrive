# IMUAssistedDrive

## What it does

**IMUAssistedDrive** is a lightweight Java class designed for **field-centric control** on **tank drive robots** in FTC/FGC. It allows precise movement using **only one joystick**, with automatic heading correction powered by the **BNO055 IMU** and a simple **proportional control algorithm**.

Even though tank drives are not traditionally field-centric, this system reorients driver input based on the robot’s current heading, giving the driver an intuitive, field-relative control experience.

### Key Features
- 🧭 True **field-centric control** for tank drive systems  
- 🎮 Uses only **one joystick** for both direction and speed  
- 📐 **Proportional heading correction** with tunable `kp`  
- 🛞 Designed specifically for non-holonomic tank drives  
- 🔁 Reversible control with `left_bumper` toggle

---

## How it works

1. The joystick’s direction is used to calculate a **desired heading** (via `atan2`).
2. The IMU continuously reports the **current heading** of the robot.
3. The system calculates the **heading error** and applies proportional correction (`error * kp`).
4. Motor power is adjusted accordingly to rotate the robot toward the desired direction.
5. If `left_bumper` is pressed and the joystick is headed down, the robot drives backwards while maintaining heading alignment.

By constantly adjusting heading based on the IMU and driver input, the robot behaves consistently **relative to the field**, no matter how it’s rotated.

---

## Usage

## Usage

### Prerequisites
- Robot with a **tank drive** system
- A configured **BNO055 IMU** named `"imu"`
- Motors named `"left_motor"` and `"right_motor"`
- A `GlobalHardwareMap` utility (or replace with your own hardware map reference)

## Usage and Integration

This package is designed for FTC or FGC robots using a tank drive system and the BNO055 IMU. It enables intuitive, field-centric control using only a single joystick. The core of the system is a proportional correction algorithm that keeps the robot aligned with the driver's intended direction.

To use this package, your hardware must include two drive motors named "left_motor" and "right_motor", as well as a properly configured BNO055 IMU named "imu". Additionally, your code must support accessing these components through a utility class like GlobalHardwareMap, or an equivalent hardware access method in your OpMode.

To integrate this package into your project, you can either:
- Place the `IMUAssistedDrive` package inside `TeamCode/src/main/java/org/firstinspires/ftc/teamcode` in the official FTC SDK when using Android Studio.
- Upload each file seperatelly and via the OnBot Java interface when using OnBot Java.


To use the class inside an OpMode, first initialize it:
```java
IMUAssistedDrive drive = new IMUAssistedDrive(
    0.02,  // kp (proportional gain)
    0.05,  // joystick deadzone
    0.8    // speed multiplier
);

Call the drive method inside your loop:
```java
drive.drive(
    gamepad1.right_stick_x,
    gamepad1.right_stick_y,
    gamepad1.left_bumper,
    gamepad1.left_stick_y
);

Tune **kp** and **speedMultiplier** as needed to fit your team's robot and driving style

