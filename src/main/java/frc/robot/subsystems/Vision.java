package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;

//@SuppressWarnings("unused")
class Vision extends SubsystemBase {

    PoseEstimate pEstimate;
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
  