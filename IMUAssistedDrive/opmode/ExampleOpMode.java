package org.firstinspires.ftc.teamcode.IMUAssistedDrive.opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.teamgreece.drive.IMUAssistedDrive;
import org.firstinspires.ftc.teamcode.teamgreece.util.GlobalHardwareMap;


@TeleOp (name = "IMUOpMode", group = "Danae")
public class ExampleOpMode extends OpMode {

    IMUAssistedDrive drivetrain;
    ElapsedTime buttonTimer;

    @Override
    public void init() {
        GlobalHardwareMap.setHw(hardwareMap);
        buttonTimer = new ElapsedTime();
        drivetrain = new IMUAssistedDrive(0.2, 0.05, 0.7);

        telemetry.addLine("Drivetrain initialization complete");
    }

    @Override
    public void loop() {
        // used for easier kp tuning
        if (buttonTimer.milliseconds() > 200) {
            if (gamepad1.dpad_up) {
                drivetrain.setKp(0.01);
                buttonTimer.reset();
            }
            else if (gamepad1.dpad_down) {
                drivetrain.setKp(-0.01);
                buttonTimer.reset();
            }
        }


        drivetrain.drive(
                gamepad1.right_stick_x,
                gamepad1.right_stick_y,
                gamepad1.left_bumper,
                gamepad1.left_stick_y
        );

        telemetryData();
    }

    void telemetryData () {
        telemetry.addData("Kp value", drivetrain.getKp());
        telemetry.addData("Current Heading ", drivetrain.getCurrentHeading());
        telemetry.addData("Desired Heading", drivetrain.getDesiredHeading());
        telemetry.addData("Error", drivetrain.getError());
        telemetry.update();

    }
}