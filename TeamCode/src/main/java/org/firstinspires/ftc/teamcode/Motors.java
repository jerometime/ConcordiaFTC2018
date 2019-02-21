package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
@TeleOp(name="java Linear OpMode", group="Linear Opmode")
//@Disabled
public class Motors extends LinearOpMode{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private DcMotor armMotor;

    private Servo hand;

    double leftPower;
    double rightPower;
    double armPower;
    double multiplier = 1;
    double handPosition;//done in degrees, will convert to 0-1 soon enough

    double lastDpadPress;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftMotor = hardwareMap.get(DcMotor.class, "left");//change mistake to left
        rightMotor = hardwareMap.get(DcMotor.class, "right");
        rightMotor.setDirection( DcMotor.Direction.REVERSE );

        armMotor = hardwareMap.get(DcMotor.class, "arm");

        hand = hardwareMap.get(Servo.class, "hand");

        multiplier = 1;

        lastDpadPress = getRuntime();

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        //rightDrive.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


            if (gamepad1.dpad_up && multiplier <= 0.9){//increase speed
                if (System.currentTimeMillis() - lastDpadPress > 0.2*1000){//raises the multiplier every 2 seconds
                    multiplier += 0.1;
                    lastDpadPress = System.currentTimeMillis();
                }
            }
            if (gamepad1.dpad_down && multiplier >= 0.2){//decrease speed
                if (System.currentTimeMillis() - lastDpadPress > 0.2*1000){
                    multiplier -= 0.1;
                    lastDpadPress = System.currentTimeMillis();
                }
            }

            if (gamepad1.right_bumper){
                armPower = 1;}
            else if (gamepad1.left_bumper){
                armPower = -1;}
            else{
                armPower = 0;}


            if (gamepad1.left_trigger > 0.5 && handPosition <= 179.5){
                handPosition += 0.5;
            }
            else if (gamepad1.right_trigger > 0.5 && handPosition >= 0.5){
                handPosition -= 0.5;
            }

            leftPower = gamepad1.right_stick_y * multiplier;
            rightPower = gamepad1.left_stick_y * multiplier;



            // Setup a variable for each drive wheel to save power level for telemetry


            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            leftMotor.setPower(leftPower);
            rightMotor.setPower(rightPower);
            armMotor.setPower(armPower);

            if (hand.getPosition() != handPosition/180){
                hand.setPosition(handPosition/180);
            }


            // Show the elapsed game time and wheel power.

            telemetry();
        }
    }

    public void telemetry(){
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Status", "System Time: " + System.currentTimeMillis());
        telemetry.addData("Status", "lastDpadPressed: " + lastDpadPress);
        telemetry.addData("Multiplier: ", multiplier);
        telemetry.addData("Left Motor: ", leftMotor.getPower());
        telemetry.addData("Right Motor: ", rightMotor.getPower());
        telemetry.addData("Arm Motor: ", armMotor.getPower());
        telemetry.addData("Hand Servo: ", hand.getPosition());
        telemetry.addData("HandPosition: ", handPosition);





        telemetry.update();
    }

}