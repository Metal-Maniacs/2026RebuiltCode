package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import static edu.wpi.first.units.Units.Inches;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Distance;
//import edu.wpi.first.units.measure.AngularVelocity;
import frc.robot.Constants;

@SuppressWarnings("unused")
class Limelight extends SubsystemBase {

    private PoseEstimate pEstimate = new PoseEstimate();
    private Pose2d pose = new Pose2d();
    private double poseX;
    private double poseY;
    private double poseZ;

    private double yaw; // rotation in directions a compass would point to 

    public Limelight() {
        LimelightHelpers.SetRobotOrientation(Constants.limelightName, yaw, 0, 0, 0, 0, 0);
    }

    public PoseEstimate getEstimate() { // Returns the most recent pose estimate
        return pEstimate;
    }

    public boolean isUpdateSus(PoseEstimate e) {
        return (e.tagCount == 0);
    }

    public void update() { // Updates pose estimate
        PoseEstimate currentEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue(Constants.limelightName);
        // We want to rotate the percieved position of the robot, because our Limelight is on a rotating turret
        final double centerDist = Units.inchesToMeters(2.0); // The typical distance between the camera and the center of the bot
        currentEstimate.pose.rotateAround(new Translation2d(centerDist, Rotation2d.fromDegrees(180)), new Rotation2d());
        if (currentEstimate != null && !isUpdateSus(currentEstimate)) {
            pEstimate = currentEstimate;
            pose = currentEstimate.pose;
        }
    }

    @Override
    public void periodic() {
        update();
    }

    @Override
    public void simulationPeriodic() {
        //periodic();
        // This method will be called once per scheduler run during simulation
    }
}
  