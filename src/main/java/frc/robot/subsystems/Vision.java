package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import edu.wpi.first.math.geometry.Pose2d;
//import edu.wpi.first.units.measure.AngularVelocity;
import frc.robot.Constants;

class Vision extends SubsystemBase {

    PoseEstimate pEstimate = new PoseEstimate();
    Pose2d pose = new Pose2d();

    public Vision() {

    }

    public PoseEstimate getEstimate() { // Returns the most recent pose estimate
        return pEstimate;
    }

    public boolean isUpdateSus(PoseEstimate e) {
        return (e.tagCount == 0);
    }

    public void update() { // Updates pose estimate
        PoseEstimate currentEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue(Constants.limelightName);
        if (currentEstimate != null && !isUpdateSus(currentEstimate)) {
            pEstimate = currentEstimate;
            pose = currentEstimate.pose;
        }
    }

    @Override
    public void periodic() {

    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}
  