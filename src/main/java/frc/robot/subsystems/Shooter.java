// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.DriveConstants;

@SuppressWarnings("unused")
public class Shooter extends SubsystemBase {

  private final SparkMax m_shooterMotor;

  public Shooter() {
    m_shooterMotor = new SparkMax(DriveConstants.kShooterCanId, MotorType.kBrushless);
    // addChild("m_clawMotor", (Sendable) m_clawMotor);
  }

  public void useShooter(double shooterSpeed) {
    m_shooterMotor.set(shooterSpeed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
