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
5. If `left_bumper` is pressed, the robot drives backwards while maintaining heading alignment.

By constantly adjusting heading based on the IMU and driver input, the robot behaves consistently **relative to the field**, no matter how it’s rotated.

---

## Usage

### Prerequisites
- Robot with a **tank drive** system
- A configured **BNO055 IMU** named `"imu"`
- Motors named `"left_motor"` and `"right_motor"`
- A `GlobalHardwareMap` utility (or replace with your own hardware map reference)

### Integration
1. Add the `IMUAssistedDrive` class to your project inside your drive or utils package.
2. In your OpMode, initialize:
   ```java
   IMUAssistedDrive drive = new IMUAssistedDrive(
       0.02,  // kp - proportional constant
       0.05,  // joystick deadzone
       0.8    // speed multiplier
   );
