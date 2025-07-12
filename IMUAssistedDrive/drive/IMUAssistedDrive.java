package org.firstinspires.ftc.teamcode.IMUAssistedDrive.drive;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.screwdivers.util.GlobalHardwareMap;

public class IMUAssistedDrive {

    // Motors and IMU
    DcMotor leftDrive;
    DcMotor rightDrive;
    BNO055IMU imu;
    Orientation angles;

    // Control Parameters
    double kp;
    double joystickOffSet; // Used if there is joystick drift
    double speedMultiplier; // Used for robot's speed reduction

    // State
    double desiredHeading;
    double headingOffset;
    double currentHeading;
    double rawHeading;
    double error;
    double leftPower;
    double rightPower;

    // Constructor
    public IMUAssistedDrive(double kp, double joystickOffSet, double speedMultiplier) {
        this.kp = kp;
        this.joystickOffSet = joystickOffSet;
        this.speedMultiplier = speedMultiplier;
        initializeHardware();
    }

    // this method gets called at the constructor
    public void initializeHardware () {
        // imu initialization
        imu = GlobalHardwareMap.getHw().get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.accelerationIntegrationAlgorithm = null;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.calibrationData = null;
        parameters.calibrationDataFile = "";
        parameters.loggingEnabled = false;
        parameters.loggingTag = "IMU";
        imu.initialize(parameters);

        // motor initialization
        leftDrive = GlobalHardwareMap.getHw().dcMotor.get("left_motor");
        rightDrive = GlobalHardwareMap.getHw().dcMotor.get("right_motor");
        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        angles = imu.getAngularOrientation(
                AxesReference.INTRINSIC,
                AxesOrder.ZYX,
                AngleUnit.DEGREES
        );

        error = 0.0;
        desiredHeading = 0.0;
        headingOffset = round(angles.firstAngle);
    }


    public void drive (double rightStickX, double rightStickY, boolean leftBumper, double leftStickY) {
        angles = imu.getAngularOrientation(
                AxesReference.INTRINSIC,
                AxesOrder.ZYX,
                AngleUnit.DEGREES);

        rawHeading = round(angles.firstAngle);
        currentHeading = rawHeading - headingOffset;

        if ((Math.abs(rightStickX) > joystickOffSet || Math.abs(rightStickY) > joystickOffSet)) {
            // Finds the desired heading (rounded) (to degrees) based on the tan of the angle formed by the leftStickX and the the joystick
            desiredHeading = normalizeAngle(round(Math.toDegrees(Math.atan2(rightStickX, -rightStickY))));

        }

        calculateError(leftBumper, -rightStickY);
        turnToAngle(rightStickX, -rightStickY, leftBumper, -leftStickY);
    }

    void turnToAngle(double rightStickX, double rightStickY, boolean leftBumper, double leftStickY) {
        if (rightStickX != 0 || rightStickY != 0) {
            // finds the vector / hypotenuse so that it can calculate the power that will be given to the motors
            double forward = Math.hypot(rightStickX, rightStickY);

            // proportional calculation of the correction needed to be made
            double turn = error * kp;

            if (leftBumper){
                if (rightStickY < -joystickOffSet) {
                    leftPower = (-forward - turn) * speedMultiplier;
                    rightPower = (-forward + turn) * speedMultiplier;
                } else {
                    leftPower = (forward - turn) * speedMultiplier;
                    rightPower = (forward + turn) * speedMultiplier;
                }
            } else {
                leftPower = (forward - turn) * speedMultiplier;
                rightPower = (forward + turn) * speedMultiplier;
            }

            driveSetPower(leftPower, rightPower);

        } else {
            if (leftStickY < -joystickOffSet) {
                leftPower = -leftStickY;
                rightPower = -leftStickY;

                driveSetPower(leftPower, rightPower);
            } else {
                driveSetPower(0, 0);
            }
        }
    }

    void calculateError (boolean leftBumper, double rightStickY) {
        // +180 degrees so that it drives backward to the desired direction
        if (leftBumper){
            if (rightStickY < -joystickOffSet) {
                desiredHeading = normalizeAngle(desiredHeading + 180);
            }
        }

        // reverse desiredHeading cause it has the opposite sign from currentHeading
        error = normalizeAngle(round(-desiredHeading - currentHeading));
    }

    // Sets motor power
    void driveSetPower (double leftDriveSpeed, double rightDriveSpeed) {
        leftDrive.setPower(leftDriveSpeed);
        rightDrive.setPower(rightDriveSpeed);
    }

    private double normalizeAngle(double angle) {
        return ((angle + 180) % 360 + 360) % 360 - 180;
    }

    // Rounds to 2 decimals
    private double round(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    // setters
    public void setKp (double kp) {
        this.kp += kp;
    }

    // getters
    public double getKp () {
        return this.kp;
    }

    public double getCurrentHeading () {
        return this.currentHeading;
    }

    public double getDesiredHeading () {
        return this.desiredHeading;
    }

    public double getError () {
        return this.error;
    }

}
