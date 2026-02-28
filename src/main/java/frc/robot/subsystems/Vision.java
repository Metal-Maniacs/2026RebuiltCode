package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.units.measure.AngularVelocity;
//import frc.robot.Constants;

class Vision extends SubsystemBase {

    PoseEstimate pEstimateRight = new PoseEstimate();
    PoseEstimate pEstimateLeft = new PoseEstimate();
    PoseEstimate pEstimateBack = new PoseEstimate();
    Pose2d pRight = new Pose2d(), pLeft = new Pose2d(), pBack = new Pose2d();
    LimelightHelpers helper;

    public Vision() {

    }

    @Override
    public void periodic() {

    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}
  